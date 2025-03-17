package io.drivesaf.qq.space.controller;

import io.drivesaf.qq.space.common.result.Result;
import io.drivesaf.qq.space.model.dto.AccessSpaceDTO;
import io.drivesaf.qq.space.model.dto.SpaceDTO;
import io.drivesaf.qq.space.model.entity.Question;
import io.drivesaf.qq.space.service.SpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/space")
@Tag(name = "空间接口")
public class SpaceController {
    @Resource
    private SpaceService spaceService;

    @PutMapping("/update")
    @Operation(summary = "修改空间权限")
    public Result<String> updateSpacePermission(@RequestBody SpaceDTO dto) {
        spaceService.updateSpace(dto);
        return Result.ok("更新成功");
    }


    @PostMapping("/access/{spaceId}")
    @Operation(summary = "访问空间")
    public Result<Boolean> accessSpace(
            @PathVariable Integer spaceId,
            @RequestBody AccessSpaceDTO dto // 使用 @RequestBody 接收请求体
    ) {
        System.out.println("spaceId: " + spaceId);
        System.out.println("userAnswer: " + dto.getUserAnswer());

        dto.setSpaceId(spaceId); // 设置 spaceId
        boolean canAccess = spaceService.canAccessSpace(dto);
        System.out.println("canAccess: " + canAccess);

        return Result.ok(canAccess);
    }


    @GetMapping("/allowAccessScope/{userId}")
    @Operation(summary = "查询空间权限")
    public Result<Integer> getAllowAccessScopeByUserId(@PathVariable Integer userId) {
        Integer allowAccessScope = spaceService.getAllowAccessScopeByUserId(userId);
        return Result.ok(allowAccessScope);
    }

    @GetMapping("/question/{userId}")
    @Operation(summary = "查询空间访问问题")
    public Result<Question> getSpaceQuestionByUserId(@PathVariable Integer userId) {
        Question question = spaceService.getSpaceQuestionByUserId(userId);
        return Result.ok(question);
    }

    @GetMapping("/spaceName/{userId}")
    @Operation(summary = "查询空间名称")
    public Result<String> getSpaceNameByUserId(@PathVariable Integer userId) {
        String spaceName = spaceService.getSpaceNameByUserId(userId);
        return Result.ok(spaceName);
    }
}