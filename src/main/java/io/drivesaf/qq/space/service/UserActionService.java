package io.drivesaf.qq.space.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.drivesaf.qq.space.model.entity.UserAction;
import io.drivesaf.qq.space.enums.UserActionEnum;

public interface UserActionService extends IService<UserAction> {
    // 新增用户行为数据
    void insertUserAction(Integer userId, Integer resourceId, UserActionEnum userActionEnum);

    // 收藏
    void collectShuoshuo(Integer resourceId);

    // 点赞
    void likeShuoshuo(Integer resourceId);


}
