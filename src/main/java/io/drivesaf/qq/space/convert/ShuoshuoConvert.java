package io.drivesaf.qq.space.convert;

import io.drivesaf.qq.space.model.dto.ShuoshuoDTO;
import io.drivesaf.qq.space.model.entity.Shuoshuo;
import io.drivesaf.qq.space.model.vo.ShuoshuoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author: DRIVESAF
 * @createTime: 2025/01/26
 * @description: 说说实体类与 VO 的转换器
 **/
@Mapper
public interface ShuoshuoConvert {

    ShuoshuoConvert INSTANCE = Mappers.getMapper(ShuoshuoConvert.class);

    ShuoshuoVO convert(Shuoshuo shuoshuo);

    Shuoshuo convertToEntity(ShuoshuoDTO shuoshuoDTO);
}
