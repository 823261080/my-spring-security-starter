package com.jwzhang.starter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jwzhang.starter.entity.SysUser1;
import com.jwzhang.starter.utils.excel.ExcelUtil;
import com.jwzhang.starter.vo.Ajax;
import com.jwzhang.starter.vo.ChangePwdBody;
import com.jwzhang.starter.service.ISysUser1Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 用户1信息 前端控制器
 * </p>
 *
 * @author zjw
 * @since 2022-08-17
 */
@Api(tags = "用户1信息相关接口")
@Slf4j
@RestController
@RequestMapping("/starter/sysUser1")
public class SysUser1Controller {

    private final ISysUser1Service user1Service;

    public SysUser1Controller(ISysUser1Service user1Service) {
        this.user1Service = user1Service;
    }

    /**
     * 获取用户1账号信息列表
     * @param userCamera 查询参数
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 结果
     */
    @ApiOperation("获取账号信息列表")
    @GetMapping("/list")
    public Ajax<Page<SysUser1>> list(SysUser1 userCamera, Integer pageNum, Integer pageSize){
        return Ajax.success(user1Service.listByPage(userCamera,pageNum,pageSize));
    }

    /**
     * 导入用户信息
     *
     * @param file excel文件
     */
//    @ApiOperation("导入用户信息")
    @PostMapping("/import")
    public void importUser(@RequestPart("file") MultipartFile file) {
        List<SysUser1> list = ExcelUtil.importExcel(file, SysUser1.class);
        for (SysUser1 user1 : list) {
            log.info("user1={}", user1);
        }
    }

    /**
     * 导出用户信息
     *
     * @param response {@link HttpServletResponse}
     */
//    @ApiOperation("导出用户信息")
    @GetMapping("/export")
    public void exportUser(HttpServletResponse response) {
        List<SysUser1> users = user1Service.list();
        ExcelUtil.exportExcel(users, "用户信息", SysUser1.class, response);
    }

    @ApiOperation("添加账号")
    @PostMapping
    public Ajax<?> add(SysUser1 sysUser1){
        return Ajax.toAjax(user1Service.addUser1(sysUser1));
    }

    @ApiOperation("修改账号")
    @PutMapping
    public Ajax<?> edit(SysUser1 sysUser1){
        return Ajax.toAjax(user1Service.editUser1(sysUser1));
    }

    /**
     * 修改密码
     * @param changePwdBody 参数
     * @return 结果
     */
    @ApiOperation("修改密码")
    @PutMapping("/password")
    public Ajax<?> editPassword(@RequestBody ChangePwdBody changePwdBody){
        return Ajax.toAjax(user1Service.editPassword(changePwdBody));
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{userIds}")
    public Ajax<?> delete(@PathVariable Long[] userIds){
        return Ajax.toAjax(user1Service.deleteUsers(Arrays.asList(userIds)));
    }
}
