package com.roy.reggie_takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.roy.reggie_takeout.common.R;
import com.roy.reggie_takeout.entity.User;
import com.roy.reggie_takeout.service.UserService;
import com.roy.reggie_takeout.utils.SMSUtils;
import com.roy.reggie_takeout.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * @description: 发送手机短信验证码
     * @param user
     * @return com.roy.reggie_takeout.common.R<java.lang.String>
     * @author: Ruofan Li
     * @date: 2023/2/13
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //1. 获取手机号
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            //2. 生成随机的四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            log.info("code={}",code);

            //3. 调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("瑞吉外卖", "", phone, code);

            //4. 需要将生成的验证码保存到session
            session.setAttribute(phone, code);

            return R.success("手机验证码短信发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * @description: 移动端用户登录
     * @param map
     * @param session
     * @return com.roy.reggie_takeout.common.R<java.lang.String>
     * @author: Ruofan Li
     * @date: 2023/2/13
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        //1. 获取手机号
        String phone = map.get("phone").toString();

        //2. 获取验证码
        String code = map.get("code").toString();

        //3. 从Session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);

        //4. 进行验证码的比对（页面提交的验证码和Session中保存的验证码比对
        if(codeInSession != null && codeInSession.equals(code)){
            //如果能够成功比对，说明登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(queryWrapper);
            if(user == null){
                //判断当前手机号对应的用户是否为新用户，新用户则自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
