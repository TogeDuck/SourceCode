package com.idle.togeduck.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.chat.entity.Party;

public interface PartyRepository extends JpaRepository<Party, Long>, PartyRepositoryCustom {
}
