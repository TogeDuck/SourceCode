package com.idle.togeduck.global.base;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> extends Response{
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;
}
