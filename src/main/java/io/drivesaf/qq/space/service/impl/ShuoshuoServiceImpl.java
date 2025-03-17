package io.drivesaf.qq.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.drivesaf.qq.space.common.cache.RequestContext;
import io.drivesaf.qq.space.common.exception.ServerException;
import io.drivesaf.qq.space.convert.ShuoshuoConvert;
import io.drivesaf.qq.space.mapper.ShuoshuoMapper;
import io.drivesaf.qq.space.mapper.UserActionMapper;
import io.drivesaf.qq.space.mapper.UserMapper;
import io.drivesaf.qq.space.model.dto.AccessSpaceDTO;
import io.drivesaf.qq.space.model.dto.ShuoshuoDTO;
import io.drivesaf.qq.space.model.entity.Shuoshuo;
import io.drivesaf.qq.space.model.entity.Space;
import io.drivesaf.qq.space.model.entity.User;
import io.drivesaf.qq.space.model.entity.UserAction;
import io.drivesaf.qq.space.service.ShuoshuoService;
import io.drivesaf.qq.space.service.SpaceService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ShuoshuoServiceImpl extends ServiceImpl<ShuoshuoMapper, Shuoshuo> implements ShuoshuoService {


    private final UserMapper userMapper;
    @Resource
    private UserActionMapper actionMapper;
    @Resource
    private SpaceService spaceService;

    @Override
    public void publishShuoShuo(Shuoshuo shuoshuo) {
        // 获取当前用户 ID
        Integer userId = RequestContext.getUserId();
        shuoshuo.setAuthor(userId);

        // 校验可见权限设置
        if (shuoshuo.getVisibleScope() == 2 && CollectionUtils.isEmpty(shuoshuo.getVisibleUser())) {
            throw new IllegalArgumentException("部分好友可见时，必须提供好友ID列表");
        }

        // 确保 visibleUser 里包含当前用户
        if (shuoshuo.getVisibleUser() == null) {
            shuoshuo.setVisibleUser(new ArrayList<>());
        }
        if (!shuoshuo.getVisibleUser().contains(userId)) {
            shuoshuo.getVisibleUser().add(userId);
        }

        // 手动设置默认值
        if (shuoshuo.getDeleteFlag() == null) {
            shuoshuo.setDeleteFlag(0);
        }
        if (shuoshuo.getCreateTime() == null) {
            shuoshuo.setCreateTime(LocalDateTime.now());
        }
        if (shuoshuo.getUpdateTime() == null) {
            shuoshuo.setUpdateTime(LocalDateTime.now());
        }

        // 保存到数据库
        this.save(shuoshuo);
    }

    @Override
    public void deleteShuoShuo(Integer shuoshuoId) {
        int rowsUpdated = baseMapper.updateDeleteFlag(shuoshuoId);
        if (rowsUpdated == 0) {
            throw new ServerException("未找到该说说或已删除");
        }
    }

    // 查询用户所有未删除说说
    @Override
    public List<Shuoshuo> getUserShuoshuo(Integer authorId) {
        // 查询指定用户（目标用户）的所有说说
        List<Shuoshuo> allShuoshuo = baseMapper.selectList(new LambdaQueryWrapper<Shuoshuo>()
                .eq(Shuoshuo::getAuthor, authorId) // 查询目标用户的说说
                .eq(Shuoshuo::getDeleteFlag, 0)  // 只查未删除的说说
                .orderByDesc(Shuoshuo::getCreateTime) // 按时间倒序排序
        );

        // 联表查询用户的昵称和头像
        for (Shuoshuo shuoshuo : allShuoshuo) {
            User user = userMapper.selectById(shuoshuo.getAuthor());
            if (user != null) {
                shuoshuo.setNickname(user.getNickname());
                shuoshuo.setAvatar(user.getAvatar());
            }
        }



        // 过滤说说，依据权限判断是否可见
        List<Shuoshuo> visibleShuoshuo = new ArrayList<>();
        for (Shuoshuo shuoshuo : allShuoshuo) {
            Integer visibleScope = shuoshuo.getVisibleScope();
            List<Integer> visibleUser = shuoshuo.getVisibleUser();
            Integer userId = RequestContext.getUserId();

            System.out.println("Visible User IDs: " + visibleUser);
            if (visibleScope == 1) {
                // 公开说说，所有人都可见，直接添加
                visibleShuoshuo.add(shuoshuo);
            } else if (visibleScope == 2) {
                // 部分好友可见，检查查询者（userId）是否在 visibleUser 列表中
                if (visibleUser != null && visibleUser.contains(authorId)) {
                    visibleShuoshuo.add(shuoshuo);
                }
            } else if (visibleScope == 3) {

                shuoshuo.setAuthor(authorId);
                if (shuoshuo.getAuthor().equals(userId)) {
                    visibleShuoshuo.add(shuoshuo);
                }
            }

        }

        return visibleShuoshuo;
    }

    @Override
    public List<Shuoshuo> selectShuoshuoByKeyword(String keyword) {
        // 获取当前用户 ID
        Integer userId = RequestContext.getUserId();

        // 使用 MyBatis Plus 的模糊查询
        List<Shuoshuo> allShuoshuo = baseMapper.selectList(new LambdaQueryWrapper<Shuoshuo>()
                .like(Shuoshuo::getContent, keyword) // 模糊匹配内容
                .eq(Shuoshuo::getDeleteFlag, 0)      // 只查询未删除的说说
                .orderByDesc(Shuoshuo::getCreateTime) // 按时间倒序排序
        );

        // 联表查询用户的昵称和头像
        for (Shuoshuo shuoshuo : allShuoshuo) {
            User user = userMapper.selectById(shuoshuo.getAuthor());
            if (user != null) {
                shuoshuo.setNickname(user.getNickname());
                shuoshuo.setAvatar(user.getAvatar());
            }
        }

        // 过滤说说，依据权限判断是否可见
        List<Shuoshuo> visibleShuoshuo = new ArrayList<>();
        for (Shuoshuo shuoshuo : allShuoshuo) {
            Integer visibleScope = shuoshuo.getVisibleScope();
            List<Integer> visibleUser = shuoshuo.getVisibleUser();
            if (visibleScope == 1) {
                // 公开说说，所有人都可见，直接添加
                visibleShuoshuo.add(shuoshuo);
            } else if (visibleScope == 2) {
                // 部分好友可见，检查当前用户是否在 visibleUser 列表中
                if (visibleUser != null && visibleUser.contains(userId)) {
                    visibleShuoshuo.add(shuoshuo);
                }
            } else if (visibleScope == 3) {
                // 仅自己可见，检查是否是发布者
                if (shuoshuo.getAuthor().equals(userId)) {
                    visibleShuoshuo.add(shuoshuo);
                }
            }
        }

        return visibleShuoshuo;
    }


    @Override
    public void updateShuoShuo(ShuoshuoDTO shuoshuoDTO) {
        // 获取当前用户 ID
        Integer userId = RequestContext.getUserId();

        // 查询要修改的说说
        Shuoshuo existingShuoshuo = baseMapper.selectById(shuoshuoDTO.getPkId());
        if (existingShuoshuo == null) {
            throw new ServerException("未找到该说说");
        }

        // 确保当前用户是说说的发布者
        if (!existingShuoshuo.getAuthor().equals(userId)) {
            throw new ServerException("无权限修改该说说");
        }

        // 将 DTO 转换为实体类
        Shuoshuo shuoshuo = ShuoshuoConvert.INSTANCE.convertToEntity(shuoshuoDTO);

        // 更新说说内容和权限
        existingShuoshuo.setContent(shuoshuo.getContent());
        existingShuoshuo.setVisibleScope(shuoshuo.getVisibleScope());
        existingShuoshuo.setVisibleUser(shuoshuo.getVisibleUser());

        // 更新时间
        existingShuoshuo.setUpdateTime(LocalDateTime.now());

        // 保存更新后的说说
        this.updateById(existingShuoshuo);
    }


    @Override
    public boolean hasLikedShuoshuo(Integer shuoshuoId) {
        Integer userId = RequestContext.getUserId();

        // 查询是否存在 type 为 3（点赞）且 delete_flag 为 0（未删除）的记录
        LambdaQueryWrapper<UserAction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAction::getShuoshuoId, shuoshuoId)
                .eq(UserAction::getType, 3)  // 3 表示点赞
                .eq(UserAction::getDeleteFlag, 0)  // 0 表示未删除
                .eq(UserAction::getUserId, userId);  // 当前用户

        Long count = actionMapper.selectCount(queryWrapper);
        return count > 0;
    }

    @Override
    public Integer getLikeNumForShuoshuo(Integer shuoshuoId) {
        QueryWrapper<UserAction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shuoshuo_id", shuoshuoId)
                .eq("type", 3)
                .eq("delete_flag", 0);
        Long likeCount = actionMapper.selectCount(queryWrapper);
        return likeCount.intValue();
    }

    @Override
    public List<Shuoshuo> getUserAndFriendShuoshuo(Integer userId) {
        // 查询当前用户及其好友
        List<User> friends = userMapper.getFriendsByUserId(userId);
        List<Integer> friendIds = new ArrayList<>();
        friendIds.add(userId); // 当前用户的说说也要显示
        for (User friend : friends) {
            friendIds.add(friend.getPkId());
        }

        // 查询所有说说，包括昵称和头像（联表查询）
        List<Shuoshuo> allShuoshuo = baseMapper.selectList(new LambdaQueryWrapper<Shuoshuo>()
                .in(Shuoshuo::getAuthor, friendIds)
                .eq(Shuoshuo::getDeleteFlag, 0)
                .orderByDesc(Shuoshuo::getCreateTime)
        );

        // 联表查询用户的昵称和头像
        for (Shuoshuo shuoshuo : allShuoshuo) {
            User user = userMapper.selectById(shuoshuo.getAuthor());
            if (user != null) {
                shuoshuo.setNickname(user.getNickname());
                shuoshuo.setAvatar(user.getAvatar());
            }
        }

        // 过滤说说，依据权限判断是否可见
        List<Shuoshuo> visibleShuoshuo = new ArrayList<>();
        Integer currentUserId = RequestContext.getUserId(); // 当前用户ID

        for (Shuoshuo shuoshuo : allShuoshuo) {
            Integer authorId = shuoshuo.getAuthor();
            Integer visibleScope = shuoshuo.getVisibleScope();
            List<Integer> visibleUser = shuoshuo.getVisibleUser();

            boolean isVisible = false;

            // 原可见性判断逻辑
            if (visibleScope == 1) { // 公开
                isVisible = true;
            } else if (visibleScope == 2) { // 部分好友可见
                if (visibleUser != null && visibleUser.contains(currentUserId)) {
                    isVisible = true;
                }
            } else if (visibleScope == 3) { // 仅自己可见
                if (authorId.equals(currentUserId)) {
                    isVisible = true;
                }
            }

            // 如果是好友的说说，额外检查空间访问权限
            if (isVisible && !authorId.equals(currentUserId)) {
                Space friendSpace = spaceService.getSpaceByCreateUserId(authorId);
                if (friendSpace == null) {
                    isVisible = false; // 好友无空间，不可见
                } else {
                    AccessSpaceDTO accessDto = new AccessSpaceDTO();
                    accessDto.setSpaceId(friendSpace.getSpaceId());
                    isVisible = spaceService.canAccessSpace(accessDto); // 调用权限检查
                }
            }

            if (isVisible) {
                visibleShuoshuo.add(shuoshuo);
            }
        }

        return visibleShuoshuo;
    }
}
