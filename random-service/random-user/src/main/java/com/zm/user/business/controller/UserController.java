package com.zm.user.business.controller;

import com.zm.system.service.IUserService;
import com.zm.user.business.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@DubboService
@RestController
@RequestMapping("user")
public class UserController implements IUserService {
    @Autowired
    UserService userService;

    @GetMapping("send")
    public String send() {
        return userService.send();
    }
}
