package com.idle.togeduck.domain.celebrity.repository;

import java.util.List;

import com.idle.togeduck.domain.celebrity.entity.Celebrity;

public interface CelebrityRepository { // extends JpaRepository<Celebrity, Long>
	List<Celebrity> findAllCelebrity(String name, String nickname);
}
