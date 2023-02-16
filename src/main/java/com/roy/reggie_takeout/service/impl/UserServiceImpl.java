package com.roy.reggie_takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roy.reggie_takeout.entity.User;
import com.roy.reggie_takeout.mapper.UserMapper;
import com.roy.reggie_takeout.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{
}
