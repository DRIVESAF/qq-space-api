package io.drivesaf.qq.space.controller;

import io.drivesaf.qq.space.common.result.Result;
import io.drivesaf.qq.space.model.dto.PhotoAlbumDTO;
import io.drivesaf.qq.space.model.entity.PhotoAlbum;
import io.drivesaf.qq.space.model.vo.PhotoAlbumVO;
import io.drivesaf.qq.space.service.PhotoAlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/photoAlbum")
@AllArgsConstructor
@Tag(name = "相册接口")
public class PhotoAlbumController {

    private final PhotoAlbumService photoAlbumService;

    @PostMapping("/create")
    @Operation(summary = "新建相册")
    public Result<Void> createPhotoAlbum(@RequestBody PhotoAlbum photoAlbum) {
        photoAlbumService.createPhotoAlbum(photoAlbum);
        return Result.ok();
    }

    @GetMapping("/{userId}/albumList")
    @Operation(summary = "查询相册")
    public Result<List<PhotoAlbumVO>> listUserPhotoAlbums(@PathVariable Integer userId) {
        // 调用服务层方法获取相册列表
        List<PhotoAlbumVO> photoAlbums = photoAlbumService.getUserPhotoAlbums(userId);
        return Result.ok(photoAlbums);
    }

    @PutMapping("/edit")
    @Operation(summary = "编辑相册")
    public Result<Void> editPhotoAlbum(@RequestBody PhotoAlbumDTO dto) {
        photoAlbumService.updatePhotoAlbum(
                dto.getPhotoAlbumId(),
                dto.getAlbumName(),
                dto.getAlbumDesc(),
                dto.getVisibleScope(),
                dto.getCoverUrl()
        );
        return Result.ok();
    }


    @PostMapping("/delete/{photoAlbumId}")
    @Operation(summary = "删除相册")
    public Result<Void> deletePhotoAlbum(@PathVariable Integer photoAlbumId) {
        photoAlbumService.deletePhotoAlbum(photoAlbumId);
        return Result.ok();
    }
}
