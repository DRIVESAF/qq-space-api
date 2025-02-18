package io.drivesaf.qq.space.controller;

import io.drivesaf.qq.space.common.result.Result;
import io.drivesaf.qq.space.model.vo.UserLoginVO;
import io.drivesaf.qq.space.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "认证接口")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "手机号登录")
    public Result<UserLoginVO> loginByPhone(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        return Result.ok(authService.loginByPhone(phone, code));
    }

    @PostMapping("/login/account")
    @Operation(summary = "账号密码登录")
    public Result<UserLoginVO> loginByAccount(@RequestParam("qq_open_id") String qq_open_id, @RequestParam("password") String password) {
        return Result.ok(authService.loginByAccount(qq_open_id, password));
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    public Result<Object> logout() {
        authService.logout();
        return Result.ok();
    }
}
