package io.drivesaf.qq.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.drivesaf.qq.space.common.cache.RequestContext;
import io.drivesaf.qq.space.common.exception.ServerException;
import io.drivesaf.qq.space.mapper.PhotoMapper;
import io.drivesaf.qq.space.mapper.PhotoAlbumMapper;
import io.drivesaf.qq.space.mapper.AlbumPhotoRelationMapper;
import io.drivesaf.qq.space.model.entity.Photo;
import io.drivesaf.qq.space.model.entity.PhotoAlbum;
import io.drivesaf.qq.space.model.entity.AlbumPhotoRelation;
import io.drivesaf.qq.space.model.vo.PhotoVO;
import io.drivesaf.qq.space.service.PhotoService;
import io.drivesaf.qq.space.service.CommonService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements PhotoService {

    private final CommonService commonService;
    private final PhotoAlbumMapper photoAlbumMapper;
    private final AlbumPhotoRelationMapper albumPhotoRelationMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Override
    public PhotoVO uploadPhotoToAlbum(MultipartFile file, Integer photoAlbumId, String photoDesc) {
        // 验证相册是否存在
        PhotoAlbum photoAlbum = photoAlbumMapper.selectById(photoAlbumId);
        if (photoAlbum == null) {
            throw new IllegalArgumentException("相册不存在");
        }

        // 上传图片并获取图片URL
        String photoUrl = commonService.upload(file);

        // 获取当前用户ID
        Integer userId = RequestContext.getUserId();

        // 保存图片信息到 t_photo 表
        Photo photo = new Photo();
        photo.setUserId(userId);
        photo.setPhotoAlbumId(photoAlbumId);
        photo.setPhotoUrl(photoUrl);
        photo.setPhotoDesc(photoDesc);
        photo.setDeleteFlag(0);
        photo.setUploadTime(LocalDateTime.now());
        this.save(photo);

        // 保存相册与照片的关系到 t_album_photo_relation 表
        AlbumPhotoRelation relation = new AlbumPhotoRelation();
        relation.setPhotoAlbumId(photoAlbumId);
        relation.setPhotoId(photo.getPhotoId());
        relation.setCreateTime(LocalDateTime.now());
        albumPhotoRelationMapper.insert(relation);

        // 创建 PhotoVO 返回结果
        PhotoVO photoVO = new PhotoVO();
        photoVO.setPhotoId(photo.getPhotoId());
        photoVO.setPhotoAlbumId(photo.getPhotoAlbumId());
        photoVO.setUserId(photo.getUserId());
        photoVO.setPhotoUrl(photo.getPhotoUrl());
        photoVO.setPhotoDesc(photo.getPhotoDesc());
        photoVO.setUploadTime(photo.getUploadTime());

        return photoVO; // 返回包含照片 URL 的 VO 对象
    }


    @Override
    public List<PhotoVO> getPhotosByAlbumId(Integer photoAlbumId) {
        // 查询相册内所有未删除的照片
        List<Photo> photos = photoMapper.selectByAlbumId(photoAlbumId);

        // 转换为 VO 对象
        return photos.stream().map(photo -> {
            PhotoVO photoVO = new PhotoVO();
            photoVO.setPhotoId(photo.getPhotoId());
            photoVO.setPhotoAlbumId(photo.getPhotoAlbumId());
            photoVO.setUserId(photo.getUserId());
            photoVO.setPhotoUrl(photo.getPhotoUrl());
            photoVO.setPhotoDesc(photo.getPhotoDesc());
            photoVO.setUploadTime(photo.getUploadTime());
            // 如果需要，你可以在这里查询并填充相册名称
            return photoVO;
        }).collect(Collectors.toList());
    }

    @Override
    public void deletePhoto(Integer photoId) {
        int rowsUpdated = baseMapper.updateDeleteFlag(photoId);
        if (rowsUpdated == 0) {
            throw new ServerException("未找到该说说或已删除");
        }
    }

    @Override
    public List<PhotoVO> getUserPhotos(Integer userId) {
        // 查询用户所有未删除的照片，并按上传时间倒序排列
        List<Photo> photos = photoMapper.selectByUserIdOrderByUploadTimeDesc(userId);

        // 转换为 VO 对象
        return photos.stream().map(photo -> {
            PhotoVO photoVO = new PhotoVO();
            photoVO.setPhotoId(photo.getPhotoId());
            photoVO.setPhotoAlbumId(photo.getPhotoAlbumId());
            photoVO.setUserId(photo.getUserId());
            photoVO.setPhotoUrl(photo.getPhotoUrl());
            photoVO.setPhotoDesc(photo.getPhotoDesc());
            photoVO.setUploadTime(photo.getUploadTime());
            // 如果需要填充其他信息，例如相册名称，可以在这里处理
            return photoVO;
        }).collect(Collectors.toList());
    }

}
