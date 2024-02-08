package com.idle.togeduck.domain.celebrity.repository;

import java.util.List;

import com.idle.togeduck.domain.celebrity.entity.Celebrity;

public interface CelebrityRepositoryCustom {

	List<Celebrity> findCelebrityByKeyword(String keyword);
}
