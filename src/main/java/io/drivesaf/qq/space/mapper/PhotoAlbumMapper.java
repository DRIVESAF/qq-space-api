package io.drivesaf.qq.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.drivesaf.qq.space.model.entity.PhotoAlbum;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface PhotoAlbumMapper extends BaseMapper<PhotoAlbum> {
    @Update("UPDATE t_photo_album SET delete_flag = 1 WHERE photoAlbum_id = #{PhotoAlbumId} AND delete_flag = 0")
    int updateDeleteFlag(Integer shuoshuoId);

    @Select("SELECT * FROM t_photo_album WHERE user_id = #{userId} AND delete_flag = 0 AND visible_scope != 3")
    List<PhotoAlbum> selectNonPrivateAlbumsByUserId(@Param("userId") Integer userId);

}
