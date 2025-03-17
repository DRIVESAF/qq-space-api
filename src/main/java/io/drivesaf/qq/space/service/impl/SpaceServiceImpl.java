package io.drivesaf.qq.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.drivesaf.qq.space.common.cache.RequestContext;
import io.drivesaf.qq.space.model.dto.AccessSpaceDTO;
import io.drivesaf.qq.space.model.dto.SpaceDTO;
import io.drivesaf.qq.space.model.entity.Space;
import io.drivesaf.qq.space.mapper.SpaceMapper;
import io.drivesaf.qq.space.model.entity.Question;
import io.drivesaf.qq.space.mapper.QuestionMapper;
import io.drivesaf.qq.space.model.vo.UserInfoVO;
import io.drivesaf.qq.space.service.SpaceService;
import io.drivesaf.qq.space.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SpaceServiceImpl implements SpaceService {

    @Autowired
    private SpaceMapper spaceMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserService userService;



    @Override
    @Transactional
    public void updateSpace(SpaceDTO dto) {
        // 获取当前用户 ID
        Integer userId = RequestContext.getUserId();

        // 查询要修改的空间
        Space existingSpace = spaceMapper.selectById(dto.getSpaceId());
        if (existingSpace == null) {
            throw new RuntimeException("未找到该空间");
        }

        // 确保当前用户是空间的创建者
        if (!existingSpace.getCreateUserId().equals(userId)) {
            throw new RuntimeException("无权限修改该空间");
        }

        // 如果 allowAccessScope 为 4，保存问题并关联 questionId
        if (dto.getAllowAccessScope() == 4) {
            Question question = new Question();
            question.setQuestionContent(dto.getQuestionContent());
            question.setCorrectAnswer(dto.getCorrectAnswer());
            question.setDeleteFlag(0); // 显式设置 delete_flag 为 0
            question.setCreateTime(LocalDateTime.now());
            questionMapper.insert(question);

            // 将生成的 questionId 设置到 space 中
            existingSpace.setQuestionId(question.getQuestionId());
        }

        // 更新空间权限
        existingSpace.setAllowAccessScope(dto.getAllowAccessScope());
        existingSpace.setAllowedUserIds(dto.getAllowedUserIds());

        // 更新时间
        existingSpace.setUpdateTime(LocalDateTime.now());

        // 保存更新后的空间
        spaceMapper.updateById(existingSpace);
    }

    @Override
    public boolean canAccessSpace(AccessSpaceDTO dto) {
        // 获取当前用户 ID
        Integer userId = RequestContext.getUserId();

        // 查询空间信息
        Space space = spaceMapper.selectById(dto.getSpaceId());
        if (space == null) {
            throw new RuntimeException("空间不存在");
        }

        // 如果用户是空间的创建者，允许访问
        if (space.getCreateUserId().equals(userId)) {
            return true;
        }

        // 根据 allowAccessScope 判断访问权限
        switch (space.getAllowAccessScope()) {
            case 1: // 公开空间，允许所有用户访问
                return true;

            case 2: // 仅好友可访问
                // 获取空间创建者的好友列表
                List<UserInfoVO> friends = userService.getUserFriends(space.getCreateUserId());
                // 检查当前用户是否在好友列表中
                boolean isFriend = friends.stream()
                        .anyMatch(friend -> friend.getPkId().equals(userId));
                if (!isFriend) {
                    return false; // 不是好友，拒绝访问
                }
                break;

            case 4: // 仅回答问题可访问，不需要检查 allowedUserIds
                // 如果空间设置了问题，检查用户是否回答了问题
                if (space.getQuestionId() != null) {
                    Question question = questionMapper.selectById(space.getQuestionId());
                    if (question == null) {
                        throw new RuntimeException("问题不存在");
                    }

                    // 检查用户答案是否正确
                    if (dto.getUserAnswer() == null || !dto.getUserAnswer().equals(question.getCorrectAnswer())) {
                        return false; // 用户未回答问题或答案错误
                    }
                }
                break;

            default: // 其他情况，检查 allowedUserIds
                // 检查用户是否在 allowedUserIds 列表中
                List<Integer> allowedUserIds = space.getAllowedUserIds();
                if (allowedUserIds != null && !allowedUserIds.contains(userId)) {
                    return false; // 用户不在允许访问的列表中
                }
                break;
        }

        // 所有检查通过，允许访问
        return true;
    }

    @Override
    public Integer getAllowAccessScopeByUserId(Integer userId) {
        // 查询用户创建的空间
        QueryWrapper<Space> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_user_id", userId);
        Space space = spaceMapper.selectOne(queryWrapper);

        if (space != null) {
            return space.getAllowAccessScope();
        }

        return null; // 如果用户没有创建空间，返回null
    }

    @Override
    public Question getSpaceQuestionByUserId(Integer userId) {
        // 查询用户创建的空间
        QueryWrapper<Space> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_user_id", userId);
        Space space = spaceMapper.selectOne(queryWrapper);

        if (space != null && space.getQuestionId() != null) {
            // 查询问题
            return questionMapper.selectById(space.getQuestionId());
        }

        return null; // 如果用户没有设置问题，返回null
    }

    @Override
    public String getSpaceNameByUserId(Integer userId) {
        // 查询用户创建的空间
        QueryWrapper<Space> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_user_id", userId);
        Space space = spaceMapper.selectOne(queryWrapper);

        if (space != null) {
            return space.getSpaceName();
        }

        return null; // 如果用户没有创建空间，返回null
    }

    @Override
    public Space getSpaceByCreateUserId(Integer createUserId) {
        QueryWrapper<Space> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_user_id", createUserId);
        return spaceMapper.selectOne(queryWrapper);
    }
}