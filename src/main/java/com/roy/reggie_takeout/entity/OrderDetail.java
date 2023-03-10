package com.roy.reggie_takeout.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细
 */
@Data
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    //名称
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    //订单id
    private Long orderId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    //菜品id
    private Long dishId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    //套餐id
    private Long setmealId;


    //口味
    private String dishFlavor;


    //数量
    private Integer number;

    //金额
    private BigDecimal amount;

    //图片
    private String image;
}
