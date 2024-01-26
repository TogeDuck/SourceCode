package com.idle.togeduck.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
}
