package io.drivesaf.qq.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.drivesaf.qq.space.common.cache.RedisCache;
import io.drivesaf.qq.space.common.cache.RequestContext;
import io.drivesaf.qq.space.common.cache.TokenStoreCache;
import io.drivesaf.qq.space.mapper.UserMapper;
import io.drivesaf.qq.space.model.entity.User;
import io.drivesaf.qq.space.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import io.drivesaf.qq.space.model.vo.UserLoginVO;
import io.drivesaf.qq.space.utils.JwtUtil;
import org.apache.commons.lang3.ObjectUtils;

import io.drivesaf.qq.space.common.cache.RedisKeys;
import io.drivesaf.qq.space.common.exception.ErrorCode;
import io.drivesaf.qq.space.common.exception.ServerException;
import io.drivesaf.qq.space.enums.AccountStatusEnum;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements AuthService {
    private final RedisCache redisCache;
    private final TokenStoreCache tokenStoreCache;

    @Override
    public UserLoginVO loginByPhone(String phone, String code) {
        String smsCacheKey = RedisKeys.getSmsKey(phone);
        Integer redisCode = (Integer) redisCache.get(smsCacheKey);
        if (ObjectUtils.isEmpty(redisCode) || !redisCode.toString().equals(code)) {
            throw new ServerException(ErrorCode.SMS_CODE_ERROR);
        }
        redisCache.delete(smsCacheKey);
        User user = baseMapper.getByPhone(phone);
        if (ObjectUtils.isEmpty(user)) {
            throw new ServerException("用户不存在，请先注册QQ");
            // log.info("用户不存在，创建用户，phone:{}", code);
            // user = new User();
            // user.setNickname(phone);
            // user.setPhone(phone);
            // user.setAvatar("默认头像的url");
            // user.setEnabled(AccountStatusEnum.ENABLED.getValue());
            // user.setBonus(0);
            // user.setRemark("这个人很懒，什么都没有写");
            // baseMapper.insert(user);
        }
        if (!user.getEnabled().equals(AccountStatusEnum.ENABLED.getValue())) {
            throw new ServerException(ErrorCode.ACCOUNT_DISABLED);
        }
        String accessToken = JwtUtil.createToken(user.getPkId());
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setPkId(user.getPkId());
        userLoginVO.setPhone(user.getPhone());
        userLoginVO.setQqOpenId(user.getQqOpenId());
        userLoginVO.setAccessToken(accessToken);
        tokenStoreCache.saveUser(accessToken, userLoginVO);
        return userLoginVO;
    }

    @Override
    public UserLoginVO loginByAccount(String qq_open_id, String password) {
        // 查询用户
        User user = baseMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getQqOpenId, qq_open_id) // 使用 qq_open_id 查询
                        .eq(User::getDeleteFlag, 0) // 只查询未删除的账号
        );

        if (ObjectUtils.isEmpty(user)) {
            throw new ServerException("用户不存在，请检查账号");
        }

        // 验证密码
        String hashedPassword = DigestUtils.sha256Hex(password);
        System.out.println("Hashed Password: " + hashedPassword);
        if (!hashedPassword.equals(user.getPassword())) {
            throw new ServerException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 验证账号状态
        if (!user.getEnabled().equals(1)) {
            throw new ServerException(ErrorCode.ACCOUNT_DISABLED);
        }

        // 生成访问令牌
        String accessToken = JwtUtil.createToken(user.getPkId());

        // 返回登录信息
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setPkId(user.getPkId());
        userLoginVO.setPhone(user.getPhone());
        userLoginVO.setQqOpenId(user.getQqOpenId());
        userLoginVO.setAccessToken(accessToken);

        // 缓存登录信息
        tokenStoreCache.saveUser(accessToken, userLoginVO);

        return userLoginVO;
    }
    @Override
    public boolean checkUserEnabled(Integer userId) {
        User user = baseMapper.selectById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return false;
        }
        return user.getEnabled().equals(AccountStatusEnum.ENABLED.getValue());
    }

    @Override
    public void logout() {
        String cacheKey = RedisKeys.getUserIdKey(RequestContext.getUserId());
        String accessToken = (String) redisCache.get(cacheKey);
        redisCache.delete(cacheKey);
        tokenStoreCache.deleteUser(accessToken);
    }
}
