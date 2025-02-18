package io.drivesaf.qq.space.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.drivesaf.qq.space.model.entity.Shuoshuo;
import io.drivesaf.qq.space.model.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    // 根据手机号查询用户
    default User getByPhone(String phone) {
        return this.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
    }

    // 根据QQ openId 查询用户
    default User getByQqOpenId(String openId) {
        return this.selectOne(new LambdaQueryWrapper<User>().eq(User::getQqOpenId, openId));
    }
    // 查询好友
    @Select("""
        SELECT u.*
        FROM t_friend f
        JOIN t_user u ON f.friend_id = u.pk_id
        WHERE f.user_id = #{userId} AND f.status = 1 AND f.delete_flag = 0
    """)
    List<User> getFriendsByUserId(@Param("userId") Integer userId);

    /**
     * 获取用户的所有好友ID
     *
     * @param userId 当前用户ID
     * @return 好友ID列表
     */
    @Select("SELECT friend_id FROM t_friend WHERE user_id = #{userId} AND status = 1 AND delete_flag = 0")
    List<Integer> getFriendId(@Param("userId") Integer userId);
}
