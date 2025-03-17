package io.drivesaf.qq.space.service;

import io.drivesaf.qq.space.model.dto.AccessSpaceDTO;
import io.drivesaf.qq.space.model.dto.SpaceDTO;
import io.drivesaf.qq.space.model.entity.Question;
import io.drivesaf.qq.space.model.entity.Space;

public interface SpaceService {
    /**
     * 更新空间权限
     *
     * @param
     */
    void updateSpace(SpaceDTO dto);

    /**
     * 检查用户是否有权限访问空间
     *
     * @param dto 访问空间请求参数
     * @return 是否可以访问
     */
    boolean canAccessSpace(AccessSpaceDTO dto);

    /**
     * 查询用户的空间权限
     *
     * @param userId 用户ID
     * @return 空间权限
     */
    Integer getAllowAccessScopeByUserId(Integer userId);

    /**
     * 查询用户设置的空间访问问题
     *
     * @param userId 用户ID
     * @return 空间访问问题
     */
    Question getSpaceQuestionByUserId(Integer userId);

    /**
     * 查询用户的空间名称
     *
     * @param userId 用户ID
     * @return 空间名称
     */
    String getSpaceNameByUserId(Integer userId);

    Space getSpaceByCreateUserId(Integer createUserId);
}