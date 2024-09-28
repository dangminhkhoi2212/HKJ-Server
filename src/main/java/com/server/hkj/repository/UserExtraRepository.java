package com.server.hkj.repository;

import com.server.hkj.domain.UserExtra;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserExtra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserExtraRepository extends JpaRepository<UserExtra, Long>, JpaSpecificationExecutor<UserExtra> {
    Optional<UserExtra> findOneByUserLogin(String login);

    @Query(
        value = "select ux from UserExtra ux join ux.user u join u.authorities a where a.name = :role",
        countQuery = "select count(ux) from UserExtra ux join ux.user u join u.authorities a where a.name = :role"
    )
    Page<UserExtra> getAllByRole(Pageable pageable, String role);

    @Query(
        value = """
        SELECT ux FROM UserExtra ux
        JOIN ux.user u
        JOIN u.authorities a
        LEFT JOIN HkjHire h ON h.employee.id = ux.id
        WHERE a.name = :role AND h.id IS NULL
        """,
        countQuery = """
        SELECT COUNT(ux) FROM UserExtra ux
        JOIN ux.user u
        JOIN u.authorities a
        LEFT JOIN HkjHire h ON h.employee.id = ux.id
        WHERE a.name = :role AND h.id IS NULL
        """
    )
    Page<UserExtra> getAllByRoleAndNotInHire(Pageable pageable, String role);
}
