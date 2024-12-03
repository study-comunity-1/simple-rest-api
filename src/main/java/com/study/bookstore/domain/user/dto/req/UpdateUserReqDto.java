package com.study.bookstore.domain.user.dto.req;

public record UpdateUserReqDto(
    String password,
    String nick,
    String address
) {
}
