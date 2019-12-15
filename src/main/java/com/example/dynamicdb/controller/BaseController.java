package com.example.dynamicdb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: He Zhigang
 * @Date: 2019/12/15 16:17
 * @Description:
 */
public abstract class BaseController<T> {

    @Autowired
    public IService<T> service;

    @PostMapping()
    public Object add(@RequestBody T t) {
        return service.save(t);
    }

    @PutMapping()
    public Object update(@RequestBody T t) {
        return service.updateById(t);
    }

    @GetMapping("/{id}")
    public T get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping()
    public IPage<T> find(@RequestParam(required = false, defaultValue = "0") Integer page,
                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<T> pageable = new Page<>(page, size);
        return service.page(pageable, new QueryWrapper<T>().orderByDesc("id"));
    }
}