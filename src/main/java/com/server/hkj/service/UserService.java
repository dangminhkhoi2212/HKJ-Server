package com.server.hkj.service;

import com.server.hkj.config.Constants;
import com.server.hkj.domain.Authority;
import com.server.hkj.domain.User;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.repository.AuthorityRepository;
import com.server.hkj.repository.UserExtraRepository;
import com.server.hkj.repository.UserRepository;
import com.server.hkj.security.AuthoritiesConstants;
import com.server.hkj.security.SecurityUtils;
import com.server.hkj.service.dto.AdminUserDTO;
import com.server.hkj.service.dto.UserDTO;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    @Inject
    private final UserExtraRepository userExtraRepository;

    private final CacheManager cacheManager;

    public UserService(
        UserRepository userRepository,
        AuthorityRepository authorityRepository,
        CacheManager cacheManager,
        UserExtraRepository userExtraRepository
    ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.userExtraRepository = userExtraRepository;
        this.cacheManager = cacheManager;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl, String phoneNumber) {
        // #dang minh khoi
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);

                userRepository.save(user);
                this.clearUserCaches(user);
                // log.info();("Changed Information for User: {}", user);
                // Retrieve the associated ApplicationUser and update the phone number
                // Check if UserExtra already exists for the user
                if (phoneNumber != null) {
                    Optional<UserExtra> userExtraOptional = userExtraRepository.findOneByUserLogin(user.getLogin());
                    UserExtra userExtra;
                    if (userExtraOptional.isPresent()) {
                        userExtra = userExtraOptional.get(); // Retrieve existing UserExtra
                        // log.info();("Updating existing UserExtra for user: {}", user.getLogin());
                    } else {
                        userExtra = new UserExtra(); // Create a new UserExtra if not exists
                        userExtra.setUser(user);
                        // log.info();("Creating new UserExtra for user: {}", user.getLogin());
                    }

                    // Update phone number
                    userExtra.setPhone(phoneNumber);
                    userExtraRepository.save(userExtra);
                }
            });
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).toList();
    }

    private UserExtra syncUserWithIdP(Map<String, Object> details, User user) {
        // save authorities in to sync user roles/groups between IdP and JHipster's local database
        Collection<String> dbAuthorities = getAuthorities();
        Collection<String> userAuthorities = user.getAuthorities().stream().map(Authority::getName).toList();
        for (String authority : userAuthorities) {
            if (!dbAuthorities.contains(authority) && AuthoritiesConstants.AUTHORITIES.contains(authority)) {
                log.info("Saving authority '{}' in local database", authority);
                Authority authorityToSave = new Authority();
                authorityToSave.setName(authority);
                authorityRepository.save(authorityToSave);
            }
        }
        // log.info();("details: {}", details);
        // save account in to sync users between IdP and JHipster's local database
        Optional<User> existingUser = userRepository.findOneByLogin(user.getLogin());
        if (existingUser.isPresent()) {
            // if IdP sends last updated information, use it to determine if an update should happen
            if (details.get("updated_at") != null) {
                Instant dbModifiedDate = existingUser.orElseThrow().getLastModifiedDate();
                Instant idpModifiedDate;
                if (details.get("updated_at") instanceof Instant) {
                    idpModifiedDate = (Instant) details.get("updated_at");
                } else {
                    idpModifiedDate = Instant.ofEpochSecond((Integer) details.get("updated_at"));
                }
                if (idpModifiedDate.isAfter(dbModifiedDate)) {
                    // log.info();("Updating user '{}' in local database", user.getLogin());
                    updateUser(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getLangKey(),
                        user.getImageUrl(),
                        details.get("phone_number") != null ? details.get("phone_number").toString() : ""
                    );
                }
                // no last updated info, blindly update
            } else {
                log.info("Updating user '{}' in local database", user.getLogin());
                updateUser(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getLangKey(),
                    user.getImageUrl(),
                    details.get("phone_number") != null ? details.get("phone_number").toString() : ""
                );
            }
        } else {
            log.info("Saving user '{}' in local database", user.getLogin());
            userRepository.save(user);
            this.clearUserCaches(user);
        }

        UserExtra userExtra = new UserExtra();
        userExtra.setUser(user);
        userExtra.setPhone(details.get("phone_number") != null ? details.get("phone_number").toString() : "");
        return userExtra;
    }

    /**
     * Returns the user from an OAuth 2.0 login or resource server with JWT.
     * Synchronizes the user in the local repository.
     *
     * @param authToken the authentication token.
     * @return the user from the authentication.
     */
    @Transactional
    public AdminUserDTO getUserFromAuthentication(AbstractAuthenticationToken authToken) {
        log.info("Fetching user for authentication token {}", authToken);
        Map<String, Object> attributes;
        if (authToken instanceof OAuth2AuthenticationToken) {
            attributes = ((OAuth2AuthenticationToken) authToken).getPrincipal().getAttributes();
        } else if (authToken instanceof JwtAuthenticationToken) {
            attributes = ((JwtAuthenticationToken) authToken).getTokenAttributes();
        } else {
            throw new IllegalArgumentException("AuthenticationToken is not OAuth2 or JWT!");
        }
        UserExtra userExtra = getUser(attributes);
        User user = userExtra.getUser();
        user.setAuthorities(
            authToken
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(AuthoritiesConstants.AUTHORITIES::contains)
                .map(authority -> {
                    Authority auth = new Authority();
                    auth.setName(authority);
                    return auth;
                })
                .collect(Collectors.toSet())
        );
        UserExtra userExtraSync = syncUserWithIdP(attributes, user);
        return new AdminUserDTO(userExtraSync);
    }

    private static UserExtra getUser(Map<String, Object> details) {
        User user = new User();
        UserExtra userExtra = new UserExtra();
        Boolean activated = Boolean.TRUE;
        String sub = String.valueOf(details.get("sub"));
        String username = null;
        if (details.get("preferred_username") != null) {
            username = ((String) details.get("preferred_username")).toLowerCase();
        }
        // handle resource server JWT, where sub claim is email and uid is ID
        if (details.get("uid") != null) {
            user.setId((String) details.get("uid"));
            user.setLogin(sub);
        } else {
            user.setId(sub);
        }
        if (username != null) {
            user.setLogin(username);
        } else if (user.getLogin() == null) {
            user.setLogin(user.getId());
        }
        if (details.get("given_name") != null) {
            user.setFirstName((String) details.get("given_name"));
        } else if (details.get("name") != null) {
            user.setFirstName((String) details.get("name"));
        }
        if (details.get("family_name") != null) {
            user.setLastName((String) details.get("family_name"));
        }
        // if (details.get("phone_number") != null) {
        //     user.setEmail((String) details.get("family_name"));
        // }
        if (details.get("email_verified") != null) {
            activated = (Boolean) details.get("email_verified");
        }
        if (details.get("email") != null) {
            user.setEmail(((String) details.get("email")).toLowerCase());
        } else if (sub.contains("|") && (username != null && username.contains("@"))) {
            // special handling for Auth0
            user.setEmail(username);
        } else {
            user.setEmail(sub);
        }
        if (details.get("langKey") != null) {
            user.setLangKey((String) details.get("langKey"));
        } else if (details.get("locale") != null) {
            // trim off country code if it exists
            String locale = (String) details.get("locale");
            if (locale.contains("_")) {
                locale = locale.substring(0, locale.indexOf('_'));
            } else if (locale.contains("-")) {
                locale = locale.substring(0, locale.indexOf('-'));
            }
            user.setLangKey(locale.toLowerCase());
        } else {
            // set langKey to default if not specified by IdP
            user.setLangKey(Constants.DEFAULT_LANGUAGE);
        }
        if (details.get("picture") != null) {
            user.setImageUrl((String) details.get("picture"));
        }
        user.setActivated(activated);
        userExtra.setPhone((String) details.get("phone_number"));
        userExtra.setUser(user);
        return userExtra;
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
}
