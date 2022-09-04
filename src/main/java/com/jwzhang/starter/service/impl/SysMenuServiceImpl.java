package com.jwzhang.starter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jwzhang.starter.entity.SysMenu;
import com.jwzhang.starter.enums.StatusEnum;
import com.jwzhang.starter.enums.UserTypeEnum;
import com.jwzhang.starter.mapper.SysMenuMapper;
import com.jwzhang.starter.utils.SecurityUtils;
import com.jwzhang.starter.service.ISysMenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author zjw
 * @since 2022-08-17
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return {@link List <SchMenu>}
     */
    @Override
    public List<SysMenu> findMenuByUserIdAndType(Long userId, UserTypeEnum userType) {
        return this.baseMapper.selectMenuByUserIdAndType(userId,userType.getCode());
    }

    /**
     * 获取菜单树
     *
     * @param userId       用户id
     * @param userTypeEnum 用户类型枚举
     * @return 结果
     */
    @Override
    public List<SysMenu> getMenuTree(Long userId, UserTypeEnum userTypeEnum) {
        List<SysMenu> menuList = this.baseMapper.selectMenuByUserIdAndType(userId,userTypeEnum.getCode());
        menuList = buildMenu(menuList);
        return menuList;
    }

    private List<SysMenu> buildMenu(List<SysMenu> menuList) {
        if(CollectionUtils.isEmpty(menuList)){
            return null;
        }
        //第一级菜单
        List<SysMenu> firstMenu = menuList.stream()
                .filter(menuCamera -> menuCamera.getParentId() == 0)
                .collect(Collectors.toList());

        callBackMenu(menuList,firstMenu);
        return firstMenu;
    }

    private void callBackMenu(List<SysMenu> organManuList, List<SysMenu> parents) {
        for (SysMenu parent : parents) {
            Long parentId = parent.getMenuId();
            List<SysMenu> children = organManuList.stream()
                    .filter(menu -> Objects.equals(menu.getParentId(), parentId))
                    .collect(Collectors.toList());
            parent.setChildren(children);
            if(!CollectionUtils.isEmpty(children)){
                callBackMenu(organManuList,children);
            }
        }
    }

    /**
     * 添加菜单
     *
     * @param menuCamera 菜单信息
     * @return 菜单id
     */
    @Override
    public Long addMenu(SysMenu menuCamera) {
        menuCamera.setMenuId(null);
        menuCamera.setStatus(StatusEnum.NORMAL.getCode());
        menuCamera.setCreateBy(SecurityUtils.getUsername());
        menuCamera.setCreateTime(LocalDateTime.now());
        this.baseMapper.insert(menuCamera);
        return menuCamera.getMenuId();
    }
}
