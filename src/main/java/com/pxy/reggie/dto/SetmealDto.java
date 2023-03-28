package com.pxy.reggie.dto;

import com.pxy.reggie.entity.Setmeal;
import com.pxy.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
