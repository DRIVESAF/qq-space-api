package io.drivesaf.qq.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.drivesaf.qq.space.common.cache.RequestContext;
import io.drivesaf.qq.space.common.exception.ServerException;
import io.drivesaf.qq.space.enums.UserActionEnum;
import io.drivesaf.qq.space.mapper.UserActionMapper;
import io.drivesaf.qq.space.model.entity.User;
import io.drivesaf.qq.space.model.entity.UserAction;
import io.drivesaf.qq.space.service.UserActionService;
import io.drivesaf.qq.space.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserActionServiceImpl extends ServiceImpl<UserActionMapper, UserAction> implements UserActionService {

    private final UserService userService;

    @Override
    public void insertUserAction(Integer userId, Integer shuoshuoId, UserActionEnum userActionEnum) {
        UserAction userAction = new UserAction();
        userAction.setUserId(userId);
        userAction.setShuoshuoId(shuoshuoId);
        userAction.setType(userActionEnum.getCode());
        userAction.setDeleteFlag(0);
        userAction.setUpdateTime(LocalDateTime.now());
        userAction.setCreateTime(LocalDateTime.now());
        save(userAction);
    }

    @Override
    public void collectShuoshuo(Integer shuoshuoId) {
        actionShuoshuo(RequestContext.getUserId(), shuoshuoId, UserActionEnum.COLLECT);
    }

    @Override
    public void likeShuoshuo(Integer shuoshuoId) {
        actionShuoshuo(RequestContext.getUserId(), shuoshuoId, UserActionEnum.LIKE);
    }

    private void actionShuoshuo(Integer userId, Integer shuoshuoId, UserActionEnum userActionEnum) {
        LambdaQueryWrapper<UserAction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAction::getShuoshuoId, shuoshuoId)
                .eq(UserAction::getUserId, userId)
                .eq(UserAction::getType, userActionEnum.getCode());

        if (baseMapper.selectCount(queryWrapper) > 0) {
            // TODO 兑换资源处理
            baseMapper.delete(queryWrapper);
        } else {
            insertUserAction(userId, shuoshuoId, userActionEnum);
        }
    }



}
