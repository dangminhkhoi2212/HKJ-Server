package com.server.hkj.repository;

import com.server.hkj.domain.User;
import com.server.hkj.domain.UserExtra;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserExtra entity.
 */
@Repository
public interface UserExtraRepository extends JpaRepository<UserExtra, Long> {
    Optional<UserExtra> findOneByUserLogin(String login);

    @Query(
        value = "select ux from UserExtra ux join ux.user u join u.authorities a where a.name = :role",
        countQuery = "select count(ux) from UserExtra ux join ux.user u join u.authorities a where a.name = :role"
    )
    Page<UserExtra> getAllByRole(Pageable pageable, String role);
}
