package com.blinnproject.myworkdayback.service.user;

import com.blinnproject.myworkdayback.model.User;

public interface UserService {
    User createLocal(String username, String email, String password);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);
}
