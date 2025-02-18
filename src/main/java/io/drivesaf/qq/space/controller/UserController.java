package io.drivesaf.qq.space.controller;

import io.drivesaf.qq.space.common.cache.RequestContext;
import io.drivesaf.qq.space.common.result.Result;
import io.drivesaf.qq.space.convert.ShuoshuoConvert;
import io.drivesaf.qq.space.model.entity.Shuoshuo;
import io.drivesaf.qq.space.model.vo.ShuoshuoVO;
import io.drivesaf.qq.space.model.vo.UserInfoVO;

import io.drivesaf.qq.space.service.UserActionService;
import io.drivesaf.qq.space.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author: DRIVESAF
 * @createTime: 2025/01/21 23:15
 * @description:
 **/
@Slf4j
@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "用户接口")
public class UserController {

    private final UserService userService;
    private final UserActionService userActionService;

    // 查询用户信息
    @GetMapping("info")
    @Operation(summary = "查询用户信息")
    public Result<UserInfoVO> userInfo() {
        return Result.ok(userService.userInfo());
    }
    // 收藏说说
    @PostMapping("shuoshuo/collect")
    @Operation(summary = "收藏说说")
    public Result<Object> collectShuoshuo(@RequestParam Integer shuoshuoId) {
        userActionService.collectShuoshuo(shuoshuoId);
        return Result.ok();
    }
    // 点赞说说
    @PostMapping("shuoshuo/like")
    @Operation(summary = "点赞说说")
    public Result<Object> likeShuoshuo(@RequestParam Integer shuoshuoId) {
        userActionService.likeShuoshuo(shuoshuoId);
        return Result.ok();
    }
    // 查询好友
    @GetMapping("friends")
    @Operation(summary = "查询好友")
    public Result<List<UserInfoVO>> getUserFriends() {
        Integer userId = RequestContext.getUserId(); // 获取当前用户 ID
        List<UserInfoVO> friends = userService.getUserFriends(userId);
        return Result.ok(friends);
    }


}
