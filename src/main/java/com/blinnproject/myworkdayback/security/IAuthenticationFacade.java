package com.blinnproject.myworkdayback.security;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
  Authentication getAuthentication();
}
