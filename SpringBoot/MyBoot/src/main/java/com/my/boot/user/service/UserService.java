package com.my.boot.user.service;


import com.my.boot.common.dto.ResultDTO;
import com.my.boot.common.exception.ApiException;
import com.my.boot.common.exception.ExceptionData;
import com.my.boot.common.util.PasswordUtil;
import com.my.boot.user.dto.UsersRequest;
import com.my.boot.user.entity.User;
import com.my.boot.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

/*
* 회원가입
* */
    @Transactional(readOnly = true)
    public ResultDTO<Void> userSignUp(UsersRequest request)  {

      try{
        if(userRepository.existsByLoginId(request.loginId())){
          throw new ApiException(ExceptionData.EXISTS_USER);
        }

        String salt = PasswordUtil.getSalt();
        String encrypted = PasswordUtil.hashSSHA(request.passwd(), salt);

        User user = request.toEntity(encrypted);


        User saveUser = userRepository.save(user);


        return ResultDTO.<Void>builder()
            .success(true)
            .message("회원 가입 되었습니다.")
            .build();
      }catch (Exception ex) {
        log.error("회원 가입 중 오류", ex);
        return ResultDTO.<Void>builder()
            .success(false)
            .message("회원 가입 중 오류가 발생했습니다: " + ex.getMessage())
            .build();
      }


    }


}
