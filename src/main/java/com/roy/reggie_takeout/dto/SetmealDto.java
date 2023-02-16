package com.roy.reggie_takeout.dto;

import com.roy.reggie_takeout.entity.Setmeal;
import com.roy.reggie_takeout.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
