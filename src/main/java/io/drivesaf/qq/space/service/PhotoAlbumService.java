package io.drivesaf.qq.space.service;

import io.drivesaf.qq.space.model.entity.PhotoAlbum;
import io.drivesaf.qq.space.model.vo.PhotoAlbumVO;

import java.util.List;

public interface PhotoAlbumService {
    /**
     * 新建相册
     *
     * @param photoAlbum 相册对象
     */
    void createPhotoAlbum(PhotoAlbum photoAlbum);

    /**
     * 查询所有相册
     *
     * @param
     */
    List<PhotoAlbumVO> getUserPhotoAlbums(Integer userId);

    // 编辑相册
    void updatePhotoAlbum(Integer albumId, String albumName, String albumDesc, Integer visibleScope, String coverUrl);


    // 删除相册
    void deletePhotoAlbum(Integer albumId);

}
