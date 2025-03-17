package io.drivesaf.qq.space.controller;

import io.drivesaf.qq.space.common.cache.RequestContext;
import io.drivesaf.qq.space.common.result.Result;
import io.drivesaf.qq.space.model.dto.ShuoshuoDTO;
import io.drivesaf.qq.space.model.entity.Shuoshuo;
import io.drivesaf.qq.space.model.vo.ShuoshuoVO;
import io.drivesaf.qq.space.service.ShuoshuoService;
import io.drivesaf.qq.space.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;


import java.util.List;

@RestController
@RequestMapping("/shuoshuo")
@AllArgsConstructor
@Slf4j
@Tag(name = "说说接口")
public class ShuoshuoController {

    private final ShuoshuoService shuoshuoService;
    private final UserService userService;

    @PostMapping("/publish")
    @Operation(summary = "发布说说")
    public Result<Void> publishShuoShuo(@RequestBody Shuoshuo shuoshuo) {
        shuoshuoService.publishShuoShuo(shuoshuo);
        return Result.ok();
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "删除说说")
    public Result<?> deleteShuoShuo(@PathVariable Integer id) {
        shuoshuoService.deleteShuoShuo(id);
        return Result.ok();
    }

    @GetMapping("/select")
    @Operation(summary = "展示所有说说")
    public Result<List<ShuoshuoVO>> getUserAndFriendShuoshuo() {
        Integer userId = RequestContext.getUserId();
        List<Shuoshuo> shuoshuos = shuoshuoService.getUserAndFriendShuoshuo(userId);

        // 转换为 VO
        List<ShuoshuoVO> shuoshuoVOs = shuoshuos.stream().map(shuoshuo -> {
            ShuoshuoVO vo = new ShuoshuoVO();
            vo.setPkId(shuoshuo.getPkId());
            vo.setContent(shuoshuo.getContent());
            vo.setNickname(shuoshuo.getNickname());
            vo.setAuthor(shuoshuo.getAuthor());
            vo.setAvatar(shuoshuo.getAvatar());
            vo.setLikeNum(shuoshuo.getLikeNum());
            vo.setIsTop(shuoshuo.getIsTop());
            vo.setImages(shuoshuo.getImages());
            vo.setTranspondNum(shuoshuo.getTranspondNum());
            vo.setCreateTime(shuoshuo.getCreateTime());
            vo.setUpdateTime(shuoshuo.getUpdateTime());
            return vo;
        }).toList();

        return Result.ok(shuoshuoVOs);
    }

    @GetMapping("/user/{authorId}/shuoshuos")
    @Operation(summary = "查询用户说说")
    public Result<List<ShuoshuoVO>> getUserShuoshuo(@PathVariable Integer authorId) {
        List<Shuoshuo> shuoshuos = shuoshuoService.getUserShuoshuo(authorId);

        // 转换为 VO
        List<ShuoshuoVO> shuoshuoVOs = shuoshuos.stream().map(shuoshuo -> {
            ShuoshuoVO vo = new ShuoshuoVO();
            vo.setPkId(shuoshuo.getPkId());
            vo.setContent(shuoshuo.getContent());
            vo.setNickname(shuoshuo.getNickname());
            vo.setAuthor(shuoshuo.getAuthor());
            vo.setAvatar(shuoshuo.getAvatar());
            vo.setLikeNum(shuoshuo.getLikeNum());
            vo.setIsTop(shuoshuo.getIsTop());
            vo.setImages(shuoshuo.getImages());
            vo.setTranspondNum(shuoshuo.getTranspondNum());
            vo.setCreateTime(shuoshuo.getCreateTime());
            vo.setUpdateTime(shuoshuo.getUpdateTime());
            return vo;
        }).toList();

        return Result.ok(shuoshuoVOs);
    }

    @GetMapping("/search")
    @Operation(summary = "查询说说")
    public Result<List<ShuoshuoVO>> searchShuoshuo(@RequestParam String keyword) {
        List<Shuoshuo> shuoshuos = shuoshuoService.selectShuoshuoByKeyword(keyword);

        // 转换为 VO
        List<ShuoshuoVO> shuoshuoVOs = shuoshuos.stream().map(shuoshuo -> {
            ShuoshuoVO vo = new ShuoshuoVO();
            vo.setPkId(shuoshuo.getPkId());
            vo.setContent(shuoshuo.getContent());
            vo.setAuthor(shuoshuo.getAuthor());
            vo.setNickname(shuoshuo.getNickname());
            vo.setAvatar(shuoshuo.getAvatar());
            vo.setLikeNum(shuoshuo.getLikeNum());
            vo.setIsTop(shuoshuo.getIsTop());
            vo.setTranspondNum(shuoshuo.getTranspondNum());
            vo.setCreateTime(shuoshuo.getCreateTime());
            vo.setUpdateTime(shuoshuo.getUpdateTime());
            return vo;
        }).toList();

        return Result.ok(shuoshuoVOs);
    }

    @PutMapping("/update")
    @Operation(summary = "修改说说")
    public Result<Void> updateShuoShuo(@RequestBody ShuoshuoDTO shuoshuoDTO) {
        shuoshuoService.updateShuoShuo(shuoshuoDTO);
        return Result.ok();
    }

    @GetMapping("/hasLiked")
    @Operation(summary = "点赞查询")
    public Result<Boolean> hasLikedShuoshuo(@RequestParam Integer shuoshuoId) {
        boolean hasLiked = shuoshuoService.hasLikedShuoshuo(shuoshuoId);
        return Result.ok(hasLiked);
    }

    @GetMapping("/likeNum/{shuoshuoId}")
    @Operation(summary = "点赞数查询")
    public Result<Integer> getLikeNum(@PathVariable Integer shuoshuoId) {
        Integer likeNum = shuoshuoService.getLikeNumForShuoshuo(shuoshuoId);
        return Result.ok(likeNum);
    }

    @PostMapping("/ai-write")
    @Operation(summary = "AI帮写")
    public Result<String> aiWrite(@RequestParam String userInput) {
        try {
            GenerationResult result = callWithMessage(userInput);
            // 提取 content 并去除换行符
            String content = extractContentFromResult(result);
            return Result.ok(content);
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            log.error("An error occurred while calling the generation service: ", e);
            return Result.error("AI帮写服务调用失败: " + e.getMessage());
        }
    }

    private GenerationResult callWithMessage(String userInput) throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(userInput)
                .build();
        GenerationParam param = GenerationParam.builder()
                .apiKey(System.getenv("DASHSCOPE_API_KEY")) // 确保环境变量中有 DASHSCOPE_API_KEY
                .model("farui-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        return gen.call(param);
    }

    private String extractContentFromResult(GenerationResult result) {
        if (result == null || result.getOutput() == null || result.getOutput().getChoices() == null) {
            return "";
        }

        // 获取第一条 choice 的 content
        String content = result.getOutput()
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();

        // 去除换行符和多余的空格
        return content.replace("\n", "").trim();
    }
}
