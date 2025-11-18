package com.kspo.carefit.damain.user.service;

import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.base.security.oauth2.entity.UserOauth2Token;
import com.kspo.carefit.base.security.util.CookieUtil;
import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.damain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User checkExistingUser(String username,String email,String name){

        User user = userRepository
                .findByUsername(username)
                .orElseGet(()-> { // 새로운 회원일 경우 생성
                    return initUser(username,email,name);
                });

        updateUser(user,username,email,name); // 기존 회원의 경우 업데이트

        return user;
    }

    // 유저의 기본필드 세팅 후 리턴하는 메소드
    private User initUser(String username,String email,String name){

        return User.builder()
                .username(username)
                .email(email)
                .nickname(name)
                .role("ROLE_USER")
                .build();
    }


    /*
    기본 CRUD 메소드 모음
     */

    // 유저 정보 ( username , email ) 을 업데이트하는 메소드
    private void updateUser(User user,
                            String username,
                            String email,
                            String name){

        user.updateUser(username,email,name);
    }

    public User updateCodes(String username,
                            Integer disabilityCode,
                            Integer sidoCode,
                            Integer sigunguCode,
                            List<Integer> sportsCode){

        User user = findByUsername(username);
        user.updateCodes(sidoCode,sigunguCode,disabilityCode,sportsCode);

        return user;

    }

    // 유저를 DB에 저장하는 메소드
    public void saveUser(User user){
        userRepository.save(user);
    }

    // 유저를 username 으로 찾는 메소드
    public User findByUsername(String username){
        return userRepository
                .findByUsername(username)
                .orElseThrow(()-> new BaseException(BaseExceptionEnum
                        .ENTITY_NOT_FOUND));
    }

    // 유저를 username 으로 삭제하는 메소드
    public void deleteUser(String username){
        User user = findByUsername(username);
        userRepository.delete(user);
    }


}
