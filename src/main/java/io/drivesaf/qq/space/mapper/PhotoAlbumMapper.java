package io.drivesaf.qq.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.drivesaf.qq.space.model.entity.PhotoAlbum;
import org.apache.ibatis.annotations.Update;

public interface PhotoAlbumMapper extends BaseMapper<PhotoAlbum> {
    @Update("UPDATE t_photo_album SET delete_flag = 1 WHERE photoAlbum_id = #{PhotoAlbumId} AND delete_flag = 0")
    int updateDeleteFlag(Integer shuoshuoId);

}
