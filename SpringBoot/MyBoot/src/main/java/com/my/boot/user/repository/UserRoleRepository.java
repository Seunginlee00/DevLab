package com.my.boot.user.repository;

import com.my.boot.user.entity.UserRole;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query("select r.roleList from UserRole r where r.loginId = :loginId")
    Optional<String> findRoleCsvByUserId(@Param("userId") String loginId);
}
