package com.roy.reggie_takeout.controller;

import com.roy.reggie_takeout.common.R;
import com.roy.reggie_takeout.entity.Orders;
import com.roy.reggie_takeout.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * @description: 用户下单
     * @param orders
     * @return com.roy.reggie_takeout.common.R<java.lang.String>
     * @author: Ruofan Li
     * @date: 2023/2/15
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){

        orderService.submit(orders);
        return R.success("下单成功！");
    }
}
