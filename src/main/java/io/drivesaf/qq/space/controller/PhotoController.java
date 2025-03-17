package io.drivesaf.qq.space.controller;

import io.drivesaf.qq.space.common.result.Result;
import io.drivesaf.qq.space.model.entity.Photo;
import io.drivesaf.qq.space.model.vo.PhotoVO;
import io.drivesaf.qq.space.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 
 * @description 照片相关接口
 */
@Tag(name = "照片接口")
@RestController
@RequestMapping("/photo")
@AllArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/upload")
    @Operation(summary = "上传照片")
    public Result<PhotoVO> uploadPhotoToAlbum(
            @RequestParam("file") MultipartFile file,
            @RequestParam("photoAlbumId") Integer photoAlbumId,
            @RequestParam(value = "photoDesc", required = false) String photoDesc
    ) {
        // 调用 service 层上传照片，并返回照片的 URL
        PhotoVO photoVO = photoService.uploadPhotoToAlbum(file, photoAlbumId, photoDesc);
        return Result.ok(photoVO); // 返回包含照片信息的结果
    }


    @GetMapping("/album/{photoAlbumId}")
    @Operation(summary = "查询相册内的所有照片")
    public Result<List<PhotoVO>> getPhotosByAlbumId(@PathVariable Integer photoAlbumId) {
        List<PhotoVO> photoVOList = photoService.getPhotosByAlbumId(photoAlbumId);
        return Result.ok(photoVOList);
    }

    @PostMapping("/delete/{photoId}")
    @Operation(summary = "删除照片")
    public Result<Void> deletePhoto(@PathVariable Integer photoId) {
        photoService.deletePhoto(photoId);
        return Result.ok();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "查询所有照片")
    public Result<List<PhotoVO>> getUserPhotos(@PathVariable Integer userId) {
        List<PhotoVO> photoVOList = photoService.getUserPhotos(userId);
        return Result.ok(photoVOList);
    }

    @GetMapping("/user/{userId}/visible")
    @Operation(summary = "查询指定用户的所有可见照片")
    public Result<List<PhotoVO>> getUserVisiblePhotos(@PathVariable Integer userId) {
        List<PhotoVO> photoVOList = photoService.getUserVisiblePhotos(userId);
        return Result.ok(photoVOList);
    }


}
