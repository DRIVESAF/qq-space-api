package io.drivesaf.qq.space.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.drivesaf.qq.space.model.entity.Shuoshuo;
import io.drivesaf.qq.space.model.entity.User;
import io.drivesaf.qq.space.model.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author: DRIVESAF
 * @createTime: 2025/01/21 23:07
 * @description:
 **/
public interface UserService extends IService<User> {
    /**
     * 用户信息
     *
     * @return 用户信息对象
     */
    @Schema(description = "用户信息")
    UserInfoVO userInfo();

    /**
     * 根据用户 ID 获取用户信息
     *
     * @param userId 用户 ID
     * @return 用户信息对象
     */
    @Schema(description = "根据用户 ID 获取用户信息")
    UserInfoVO getUserInfoById(Integer userId);

    /**
     * 查询用户的全部好友
     *
     * @param userId 用户ID
     * @return 好友列表
     */
    List<UserInfoVO> getUserFriends(Integer userId);

}