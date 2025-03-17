package io.drivesaf.qq.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.drivesaf.qq.space.model.entity.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
}