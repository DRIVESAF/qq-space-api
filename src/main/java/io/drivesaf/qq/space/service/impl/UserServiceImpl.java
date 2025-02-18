package io.drivesaf.qq.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.drivesaf.qq.space.common.cache.RequestContext;
import io.drivesaf.qq.space.common.exception.ErrorCode;
import io.drivesaf.qq.space.common.exception.ServerException;

import io.drivesaf.qq.space.convert.UserConvert;
import io.drivesaf.qq.space.mapper.UserMapper;


import io.drivesaf.qq.space.model.entity.User;
import io.drivesaf.qq.space.model.vo.UserInfoVO;

import io.drivesaf.qq.space.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {



    @Override
    public UserInfoVO userInfo() {
        Integer userId = RequestContext.getUserId();


        // 查询数据库
        User user = baseMapper.selectById(userId);

        if (user == null) {
            log.error("用户不存在, userId: {}", userId);
            throw new ServerException(ErrorCode.USER_NOT_EXIST);
        }
        UserInfoVO userInfoVO = UserConvert.INSTANCE.convert(user);

        return userInfoVO;

    }

    @Override
    public List<UserInfoVO> getUserFriends(Integer userId) {
        // 查询好友
        List<User> friends = baseMapper.getFriendsByUserId(userId);

        // 转换为 VO 对象
        return UserConvert.INSTANCE.convertList(friends);
    }




}
