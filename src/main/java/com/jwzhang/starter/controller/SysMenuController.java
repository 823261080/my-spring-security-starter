package com.jwzhang.starter.controller;

import com.jwzhang.starter.entity.SysMenu;
import com.jwzhang.starter.enums.UserTypeEnum;
import com.jwzhang.starter.utils.SecurityUtils;
import com.jwzhang.starter.vo.Ajax;
import com.jwzhang.starter.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author zjw
 * @since 2022-08-17
 */
@Api(tags = "菜单相关接口")
@RestController
@RequestMapping("/starter/sysMenu")
public class SysMenuController {

    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取菜单树
     * @return 菜单树
     */
    @ApiOperation("获取菜单树")
    @GetMapping("/menuTree")
    public Ajax<List<SysMenu>> menuTree(){
        Long userId = SecurityUtils.getUserId();
        UserTypeEnum userTypeEnum = SecurityUtils.getUserType();
        return Ajax.success(menuService.getMenuTree(userId,userTypeEnum));
    }

    /**
     * 添加菜单
     * @return 结果
     */
    @ApiOperation("添加菜单")
    @PostMapping("/addMenu")
    public Ajax<Long> addMenu(@RequestBody SysMenu menuCamera){
        return Ajax.success(menuService.addMenu(menuCamera));
    }
}
