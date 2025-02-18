package io.drivesaf.qq.space.service;

import io.drivesaf.qq.space.model.vo.PhotoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {
    /**
     * 上传照片到指定相册
     *
     * @param file         照片文件
     * @param photoAlbumId 所属相册ID
     * @param photoDesc    照片描述
     * @return 返回上传后的照片信息（包含照片 URL）
     */
    PhotoVO uploadPhotoToAlbum(MultipartFile file, Integer photoAlbumId, String photoDesc);

    /**
     * 根据相册ID查询相册内的所有照片
     *
     * @param photoAlbumId 相册ID
     * @return 照片信息列表
     */
    List<PhotoVO> getPhotosByAlbumId(Integer photoAlbumId);

    /**
     * 删除照片
     *
     * @param
     */
    void deletePhoto(Integer photoId);

    /**
     * 查询用户的所有照片并按上传时间倒序排列
     *
     * @param userId 用户ID
     * @return 照片信息列表
     */
    List<PhotoVO> getUserPhotos(Integer userId);

}
