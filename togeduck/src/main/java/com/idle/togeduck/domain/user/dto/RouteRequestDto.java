package com.idle.togeduck.domain.user.dto;

import java.util.List;

public record RouteRequestDto(
	List<Position> route
) {
}
