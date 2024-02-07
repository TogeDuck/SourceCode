package com.idle.togeduck.domain.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.event.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
}
