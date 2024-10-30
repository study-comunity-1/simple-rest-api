package com.study.bookstore.domain.user.dto.req;

import com.study.bookstore.domain.user.entity.User;
import com.study.bookstore.domain.user.service.LoginUserService;

public record UpdateUserReqDto(
    String password,
    String nick,
    String address
) {
}
