package com.web.service;

import com.web.dto.CustomUserDetails;
import com.web.dto.LoginDto;
import com.web.dto.TokenDto;
import com.web.entity.User;
import com.web.enums.ActiveStatus;
import com.web.exception.MessageException;
import com.web.jwt.JwtTokenProvider;
import com.web.repository.UserRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public TokenDto login(LoginDto loginDto){
        Optional<User> users = userRepository.findByUsername(loginDto.getUsername());
        if(users.isEmpty()){
            throw new MessageException("Tài khoản không tồn tại");
        }
        if(passwordEncoder.matches(loginDto.getPassword(), users.get().getPassword()) == false){
            throw new MessageException("Mật khẩu không chính xác");
        }
        // check infor user
        if(users.get().getActived() == false){
            throw new MessageException("Tài khoản đã bị khóa");
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(users.get());
        String token = jwtTokenProvider.generateToken(customUserDetails);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(token);
        tokenDto.setUser(users.get());
        return tokenDto;
    }


    public User regis(User user){
        userRepository.findByUsername(user.getUsername())
                .ifPresent(exist->{
                    throw new MessageException("Tên đăng nhập đã tồn tại", 400);
                });
        user.setCreatedDate(new Date(System.currentTimeMillis()));
        user.setActived(true);
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User result = userRepository.save(user);
        return result;
    }

    public Page<User> findByRoleAndParam(Pageable pageable, String role, String search){
        Page<User> page = null;
        if(role == null){
            page = userRepository.findByParam(search, pageable);
        }
        else{
            page = userRepository.findByParamAndRole(search, role, pageable);
        }
        return page;
    }

    public ActiveStatus lockOrUnlock(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new MessageException("user not found");
        }
        if(user.get().getActived() == true){
            user.get().setActived(false);
            userRepository.save(user.get());
            return ActiveStatus.DA_KHOA;
        }
        else{
            user.get().setActived(true);
            userRepository.save(user.get());
            return ActiveStatus.DA_MO_KHOA;
        }
    }
}
