package io.drivesaf.qq.space.service;

import com.baomidou.mybatisplus.extension.service.IService;

import io.drivesaf.qq.space.model.entity.User;
import io.drivesaf.qq.space.model.vo.UserLoginVO;


public interface AuthService extends IService<User> {
    /**
     * 登录
     *
     * @param phone 电话
     * @param code  验证码
     * @return {@link UserLoginVO}
     */
    UserLoginVO loginByPhone(String phone, String code);

    /**
     * 账号密码登录
     *
     * @param qq_open_id qq 账号
     * @param password 密码
     * @return {@link UserLoginVO}
     */
    UserLoginVO loginByAccount(String qq_open_id, String password);
    /**
     * 检查用户是否启用
     *
     * @param userId 用户 ID
     * @return boolean
     */
    boolean checkUserEnabled(Integer userId);

    /**
     * 登出
     */
    void logout();
}
