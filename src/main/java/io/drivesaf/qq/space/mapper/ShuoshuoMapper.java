package io.drivesaf.qq.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.drivesaf.qq.space.model.entity.Shuoshuo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ShuoshuoMapper extends BaseMapper<Shuoshuo> {

    @Update("UPDATE t_shuoshuo SET delete_flag = 1 WHERE pk_id = #{shuoshuoId} AND delete_flag = 0")
    int updateDeleteFlag(Integer shuoshuoId);



}
