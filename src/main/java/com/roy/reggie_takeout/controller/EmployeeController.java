package com.roy.reggie_takeout.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roy.reggie_takeout.common.R;
import com.roy.reggie_takeout.entity.Employee;
import com.roy.reggie_takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * @description: 员工登录
     * @param request
     * @param employee
     * @return com.roy.reggie_takeout.common.R<com.roy.reggie_takeout.entity.Employee>
     * @author: Ruofan Li
     * @date: 2023/2/7
     */
    @PostMapping("/login") //前端发送请求是以post请求方式发送
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1. 页面提交的密码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2. 根据页面提交的用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername()); //相当于调用Employee的getUsername()
        Employee emp = employeeService.getOne(queryWrapper); //通过用户名查询数据库

        //3. 如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登录失败");
        }

        //4. 密码比对，如果不一致则返回登录失败
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        //比对成功
        //5. 查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        //6. 登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * @description: 员工退出
     * @param request
     * @return com.roy.reggie_takeout.common.R<java.lang.String>
     * @author: Ruofan Li
     * @date: 2023/2/7
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //1. 清理Session中的用户id
        request.getSession().removeAttribute("employee");

        //2. 返回结果
        return R.success("退出成功");
    }

    /**
     * @description: 新增员工
     * @param employee
     * @return com.roy.reggie_takeout.common.R<java.lang.String>
     * @author: Ruofan Li
     * @date: 2023/2/7
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工的信息为{}",employee.toString());

        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        //获取当前登录用户的id
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * @description: 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return com.roy.reggie_takeout.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     * @author: Ruofan Li
     * @date: 2023/2/7
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName,name);
        //添加排序条件,通过更新时间进行倒序排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * @description: 根据id修改员工信息
     * @param employee
     * @return com.roy.reggie_takeout.common.R<java.lang.String>
     * @author: Ruofan Li
     * @date: 2023/2/8
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
//        //设置updateUser和updateTime
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(empId);
//        employee.setUpdateTime(LocalDateTime.now());

        //根据id更新
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * @description: 根据员工id获取员工信息
     * @param id
     * @return com.roy.reggie_takeout.common.R<com.roy.reggie_takeout.entity.Employee>
     * @author: Ruofan Li
     * @date: 2023/2/8
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
