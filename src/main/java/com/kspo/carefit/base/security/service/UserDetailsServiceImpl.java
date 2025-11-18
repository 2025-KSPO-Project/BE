package com.kspo.carefit.base.security.service;

import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.base.security.UserDetailsImpl;
import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.damain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(()-> new BaseException(BaseExceptionEnum
                        .ENTITY_NOT_FOUND)); // 해당 유저를 찾을 수 없는 경우

        return new UserDetailsImpl(user);

    }
}
