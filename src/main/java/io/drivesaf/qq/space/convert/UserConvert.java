package io.drivesaf.qq.space.convert;

import io.drivesaf.qq.space.model.entity.User;
import io.drivesaf.qq.space.model.vo.UserInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


/**
 * @author: DRIVESAF
 * @createTime: 2025/01/21 23:10
 * @description:
 **/
@Mapper
public interface UserConvert {
    //获取UserConvert实例，由MapStruct ⾃动⽣成实现类并提供实例
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);
    //将User对象转换为UserInfoVO对象
    UserInfoVO convert(User user);
    List<UserInfoVO> convertList(List<User> users);
}