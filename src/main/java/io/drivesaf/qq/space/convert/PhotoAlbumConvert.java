package io.drivesaf.qq.space.convert;

import io.drivesaf.qq.space.model.entity.PhotoAlbum;
import io.drivesaf.qq.space.model.vo.PhotoAlbumVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author: DRIVESAF
 * @createTime: 2025/02/07
 * @description: 相册实体类与 VO 的转换器
 **/
@Mapper
public interface PhotoAlbumConvert {

    PhotoAlbumConvert INSTANCE = Mappers.getMapper(PhotoAlbumConvert.class);

    PhotoAlbumVO convertToVO(PhotoAlbum album);
}
