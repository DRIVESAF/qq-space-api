package io.drivesaf.qq.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.drivesaf.qq.space.model.entity.Photo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface PhotoMapper extends BaseMapper<Photo> {
    // 查询全部照片
    @Select("SELECT * FROM t_photo WHERE photo_album_id = #{photoAlbumId} AND delete_flag = 0")
    List<Photo> selectByAlbumId(Integer photoAlbumId);

    // 删除照片
    @Update("UPDATE t_photo SET delete_flag = 1 WHERE photo_id = #{PhotoId} AND delete_flag = 0")
    int updateDeleteFlag(Integer photoId);

    @Select("SELECT * FROM t_photo WHERE user_id = #{userId} AND delete_flag = 0 ORDER BY upload_time DESC")
    List<Photo> selectByUserIdOrderByUploadTimeDesc(@Param("userId") Integer userId);

}
