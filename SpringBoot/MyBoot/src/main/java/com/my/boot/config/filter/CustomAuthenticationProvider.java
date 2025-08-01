package com.my.boot.config.filter;

import com.project.my.config.service.CustomUserDetailsService;
import com.project.my.user.dto.UsersDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final CustomUserDetailsService userDetailsService;
  private final CustomPasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String userId = authentication.getName();
    String rawPassword = authentication.getCredentials().toString();

    //log.info("📌 복호화된 비밀번호: {}", rawPassword);

    UsersDTO userDetails = (UsersDTO) userDetailsService.loadUserByUsername(userId);
    String encoded = userDetails.getPassword();
    String salt = userDetails.getSalt();

    log.info("로그인 검증 - userId: {}", userId);
    //log.info("저장된 해시: {}", encoded);
    //log.info("저장된 salt: {}", salt);

    if (!passwordEncoder.matches(rawPassword, salt, encoded)) {
      throw new BadCredentialsException("비밀번호 불일치");
    }

    return new UsernamePasswordAuthenticationToken(userDetails, rawPassword, userDetails.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}

