package com.geoxus.modules.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.modules.user.entity.UserLogEntity;
import com.geoxus.modules.user.mapper.UserLogMapper;
import com.geoxus.modules.user.service.UserLogService;
import org.springframework.stereotype.Service;

@Service
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLogEntity> implements UserLogService {
}
