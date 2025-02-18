package io.drivesaf.qq.space.service;

import io.drivesaf.qq.space.model.dto.ShuoshuoDTO;
import io.drivesaf.qq.space.model.entity.Shuoshuo;

import java.util.List;

public interface ShuoshuoService {
    /**
     * 发布说说
     *
     * @param shuoshuo 说说对象
     */
    void publishShuoShuo(Shuoshuo shuoshuo);

    /**
     * 删除说说
     *
     * @param shuoshuoId 说说ID
     */
    void deleteShuoShuo(Integer shuoshuoId);

    /**
     * 查询用户和好友的说说
     *
     * @param userId 当前用户ID
     * @return 说说列表
     */
    List<Shuoshuo> getUserAndFriendShuoshuo(Integer userId);

    /**
     * 查询用户的所有说说
     *
     * @param authorId 被查询用户ID
     * @return 说说列表
     */
    List<Shuoshuo> getUserShuoshuo(Integer authorId);

    /**
     * 根据关键字模糊查询未删除的说说
     * @param keyword 关键字
     * @return 说说列表
     */
    List<Shuoshuo> selectShuoshuoByKeyword(String keyword);

    /**
     * 修改说说
     *
     * @param shuoshuoDTO 说说DTO
     */
    void updateShuoShuo(ShuoshuoDTO shuoshuoDTO);

    /**
     * 查询当前用户是否对某条说说进行过点赞
     *
     * @param shuoshuoId 说说ID
     * @return 是否点赞过
     */
    boolean hasLikedShuoshuo(Integer shuoshuoId);

    /**
     * 统计指定说说的点赞数
     *
     * @param shuoshuoId 说说ID
     * @return 点赞数
     */
    Integer getLikeNumForShuoshuo(Integer shuoshuoId);
}
