package com.web.api;

import com.web.dto.*;
import com.web.entity.User;
import com.web.enums.ActiveStatus;
import com.web.jwt.JwtTokenProvider;
import com.web.repository.UserRepository;
import com.web.exception.MessageException;
import com.web.service.UserService;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.sql.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserApi {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDto loginDto) {
        TokenDto tokenDto = userService.login(loginDto);
        return new ResponseEntity<>(tokenDto, HttpStatus.OK);
    }

    @PostMapping("/regis")
    public ResponseEntity<?> regisUser(@RequestBody User user) {
        User result = userService.regis(user);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/admin/all-user")
    public Page<User> findByRole(Pageable pageable, @RequestParam(value = "role", required = false) String role,
                                 @RequestParam(value = "search", required = false) String search){
        if(search == null){
            search = "";
        }
        search = "%"+search+"%";
        Page<User> result = userService.findByRoleAndParam(pageable,role, search);
        return result;
    }

    @GetMapping("/admin/lock-user")
    public ResponseEntity<?> lock(@RequestParam(value = "id") Long id){
        ActiveStatus activeStatus = userService.lockOrUnlock(id);
        return new ResponseEntity<>(activeStatus, HttpStatus.OK);
    }

    @PostMapping("/update-infor")
    public ResponseEntity<?> updateInfor(@RequestBody User user){
        User us = userUtils.getUserWithAuthority();
        us.setFullName(user.getFullName());
        us.setAvatar(user.getAvatar());
        User result = userRepository.save(us);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
