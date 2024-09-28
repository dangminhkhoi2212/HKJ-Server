package com.server.hkj.service;

import com.server.hkj.domain.Authority;
import com.server.hkj.domain.User;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.repository.UserExtraRepository;
import com.server.hkj.repository.UserRepository;
import com.server.hkj.service.dto.AccountDTO;
import com.server.hkj.service.dto.UserExtraDTO;
import com.server.hkj.service.mapper.AccountMapper;
import com.server.hkj.service.mapper.UserExtraMapper;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.UserExtra}.
 */
@Service
@Transactional
public class UserExtraService {

    private static final Logger log = LoggerFactory.getLogger(UserExtraService.class);

    private final UserExtraRepository userExtraRepository;

    private final UserExtraMapper userExtraMapper;

    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    public UserExtraService(
        UserExtraRepository userExtraRepository,
        UserExtraMapper userExtraMapper,
        UserRepository userRepository,
        AccountMapper accountMapper
    ) {
        this.userExtraRepository = userExtraRepository;
        this.userExtraMapper = userExtraMapper;
        this.userRepository = userRepository;
        this.accountMapper = accountMapper;
    }

    /**
     * Save a userExtra.
     *
     * @param userExtraDTO the entity to save.
     * @return the persisted entity.
     */
    public UserExtraDTO save(UserExtraDTO userExtraDTO) {
        log.debug("Request to save UserExtra : {}", userExtraDTO);
        UserExtra userExtra = userExtraMapper.toEntity(userExtraDTO);
        userExtra = userExtraRepository.save(userExtra);
        return userExtraMapper.toDto(userExtra);
    }

    /**
     * Update a userExtra.
     *
     * @param userExtraDTO the entity to save.
     * @return the persisted entity.
     */
    public UserExtraDTO update(UserExtraDTO userExtraDTO) {
        log.debug("Request to update UserExtra : {}", userExtraDTO);
        UserExtra userExtra = userExtraMapper.toEntity(userExtraDTO);
        userExtra.setIsPersisted();
        userExtra = userExtraRepository.save(userExtra);
        return userExtraMapper.toDto(userExtra);
    }

    /**
     * Partially update a userExtra.
     *
     * @param userExtraDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserExtraDTO> partialUpdate(UserExtraDTO userExtraDTO) {
        log.debug("Request to partially update UserExtra : {}", userExtraDTO);

        return userExtraRepository
            .findById(userExtraDTO.getId())
            .map(existingUserExtra -> {
                userExtraMapper.partialUpdate(existingUserExtra, userExtraDTO);

                return existingUserExtra;
            })
            .map(userExtraRepository::save)
            .map(userExtraMapper::toDto);
    }

    /**
     * Get one userExtra by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserExtraDTO> findOne(Long id) {
        log.debug("Request to get UserExtra : {}", id);
        return userExtraRepository.findById(id).map(userExtraMapper::toDto);
    }

    /**
     * Delete the userExtra by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserExtra : {}", id);
        userExtraRepository.deleteById(id);
    }

    public Page<UserExtraDTO> findAll(Pageable pageable) {
        return userExtraRepository.findAll(pageable).map(userExtraMapper::toDto);
    }

    public Page<AccountDTO> getUsersByRole(Pageable pageable, String role) {
        return userExtraRepository.getAllByRoleAndNotInHire(pageable, role).map(this::accountMapper);
    }

    public AccountDTO accountMapper(UserExtra userExtra) {
        return accountMapper.toDto(userExtra);
    }

    public UserExtra setDetails(UserExtra userExtra, String phone, String address) {
        userExtra.setPhone(phone);
        userExtra.setAddress(address);
        return userExtra;
    }

    public UserExtraDTO syncAccount(AccountDTO accountDTO) {
        User user = new User();
        user.setId(accountDTO.getUserId());
        user.setLogin(accountDTO.getLogin());
        user.setFirstName(accountDTO.getFirstName());
        user.setLastName(accountDTO.getLastName());
        user.setEmail(accountDTO.getEmail());
        user.setAuthorities(
            accountDTO
                .getAuthorities()
                .stream()
                .map(authority -> {
                    Authority auth = new Authority();
                    auth.setName(authority);
                    return auth;
                })
                .collect(Collectors.toSet())
        );
        User savedUser = userRepository.save(user);
        UserExtra userExtra = new UserExtra();
        userExtra.setPhone(accountDTO.getPhone());
        userExtra.setAddress(accountDTO.getAddress());
        userExtra.setUser(savedUser);
        UserExtra savedUserExtra = userExtraRepository.save(userExtra);
        return userExtraMapper.toDto(savedUserExtra);
    }
}
