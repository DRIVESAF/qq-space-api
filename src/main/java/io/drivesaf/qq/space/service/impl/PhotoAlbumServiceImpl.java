package io.drivesaf.qq.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.drivesaf.qq.space.common.cache.RequestContext;
import io.drivesaf.qq.space.common.exception.ServerException;
import io.drivesaf.qq.space.convert.PhotoAlbumConvert;
import io.drivesaf.qq.space.mapper.PhotoAlbumMapper;
import io.drivesaf.qq.space.model.entity.PhotoAlbum;
import io.drivesaf.qq.space.model.vo.PhotoAlbumVO;
import io.drivesaf.qq.space.service.PhotoAlbumService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PhotoAlbumServiceImpl extends ServiceImpl<PhotoAlbumMapper, PhotoAlbum> implements PhotoAlbumService {

    @Override
    public void createPhotoAlbum(PhotoAlbum photoAlbum) {
        // 获取当前用户 ID
        Integer userId = RequestContext.getUserId();
        photoAlbum.setUserId(userId);

        // 校验可见权限设置
        if (photoAlbum.getVisibleScope() == 2 && CollectionUtils.isEmpty(photoAlbum.getVisibleUserIds())) {
            throw new IllegalArgumentException("部分好友可见时，必须提供好友ID列表");
        }

        // 确保 visibleUserIds 里包含当前用户
        if (photoAlbum.getVisibleUserIds() == null) {
            photoAlbum.setVisibleUserIds(new ArrayList<>());
        }
        if (!photoAlbum.getVisibleUserIds().contains(userId)) {
            photoAlbum.getVisibleUserIds().add(userId);
        }

        // 手动设置默认值
        if (photoAlbum.getDeleteFlag() == null) {
            photoAlbum.setDeleteFlag(0);
        }
        if (photoAlbum.getCreateTime() == null) {
            photoAlbum.setCreateTime(LocalDateTime.now());
        }
        if (photoAlbum.getUpdateTime() == null) {
            photoAlbum.setUpdateTime(LocalDateTime.now());
        }

        // 保存相册到数据库
        this.save(photoAlbum);
    }

    @Override
    public List<PhotoAlbumVO> getUserPhotoAlbums(Integer userId) {
        // 查询指定用户（目标用户）的所有未删除相册
        List<PhotoAlbum> allPhotoAlbums = baseMapper.selectList(new LambdaQueryWrapper<PhotoAlbum>()
                .eq(PhotoAlbum::getUserId, userId) // 查询目标用户的相册
                .eq(PhotoAlbum::getDeleteFlag, 0)  // 只查未删除的相册
                .orderByDesc(PhotoAlbum::getCreateTime) // 按创建时间倒序排序
        );

        // 获取当前登录用户 ID
        Integer currentUserId = RequestContext.getUserId();

        // 初始化返回的相册列表
        List<PhotoAlbumVO> visiblePhotoAlbums = new ArrayList<>();

        for (PhotoAlbum album : allPhotoAlbums) {
            Integer visibleScope = album.getVisibleScope();
            List<Integer> visibleUserIds = album.getVisibleUserIds();

            if (visibleScope == 1) {
                // 公开
                visiblePhotoAlbums.add(PhotoAlbumConvert.INSTANCE.convertToVO(album));
            } else if (visibleScope == 2) {
                // 部分好友可见，废弃
                if (visibleUserIds != null && visibleUserIds.contains(currentUserId)) {
                    visiblePhotoAlbums.add(PhotoAlbumConvert.INSTANCE.convertToVO(album));
                }
            } else if (visibleScope == 3) {
                // 私密
                if (album.getUserId().equals(currentUserId)) {
                    visiblePhotoAlbums.add(PhotoAlbumConvert.INSTANCE.convertToVO(album));
                }
            }
        }

        return visiblePhotoAlbums;
    }


    @Override
    public void updatePhotoAlbum(Integer albumId, String albumName, String albumDesc, Integer visibleScope, String coverUrl) {
        // 获取相册对象
        PhotoAlbum album = this.getById(albumId);
        if (album == null || album.getDeleteFlag() == 1) {
            throw new IllegalArgumentException("相册不存在或已被删除");
        }

        // 更新相册的名称、描述、权限和封面图
        album.setAlbumName(albumName);
        album.setAlbumDesc(albumDesc);
        album.setVisibleScope(visibleScope);
        album.setCoverUrl(coverUrl);
        album.setUpdateTime(LocalDateTime.now());

        // 校验权限设置
        if (visibleScope == 2 && CollectionUtils.isEmpty(album.getVisibleUserIds())) {
            throw new IllegalArgumentException("部分好友可见时，必须提供好友ID列表");
        }

        // 更新相册
        this.updateById(album);
    }

    @Override
    public void deletePhotoAlbum(Integer photoAlbumId) {
        int rowsUpdated = baseMapper.updateDeleteFlag(photoAlbumId);
        if (rowsUpdated == 0) {
            throw new ServerException("未找到该说说或已删除");
        }
    }
}
