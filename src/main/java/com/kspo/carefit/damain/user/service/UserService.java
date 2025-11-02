package com.kspo.carefit.damain.user.service;

import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.damain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
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
