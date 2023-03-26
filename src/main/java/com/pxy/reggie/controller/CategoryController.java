package com.pxy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pxy.reggie.common.R;
import com.pxy.reggie.entity.Category;
import com.pxy.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-25 15:25
 **/
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    /**
     *添加分类
     * @param category 类的对象
     * @return msg&code
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     *  分类分页查询
     * @param page 当前页
     * @param pageSize 页大小
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //分页构造器
        Page<Category> pageParam = new Page<>(page, pageSize);
        //条件构造器 根据sort排序
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper();
        wrapper.orderByAsc(Category::getSort);
        Page<Category> pages = categoryService.page(pageParam, wrapper);
        return R.success(pages);
    }

    /**
     * 根据id删除分类
     * @param id 分类id
     * @return code
     */
    @DeleteMapping
    public R<String> delete(Long id){
//        categoryService.removeById(id);
        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }

    /**
     * 修改分类信息
     * @param category 分类表对象
     * @return json数据
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类名称={}",category);
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getType()!= null,Category::getType,category.getType());
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(wrapper);
        return R.success(list);
    }


}
