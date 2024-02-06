package com.idle.togeduck.domain.celebrity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.celebrity.entity.Celebrity;

public interface CelebrityRepository extends JpaRepository<Celebrity, Long>, CelebrityRepositoryCustom {
}