package cn.darkjrong.submeter.service.impl;

import cn.darkjrong.submeter.common.pojo.dto.UserDTO;
import cn.darkjrong.submeter.common.pojo.entity.User;
import cn.darkjrong.submeter.common.pojo.vo.UserVO;
import cn.darkjrong.submeter.mapper.UserMapper;
import cn.darkjrong.submeter.repository.UserRepository;
import cn.darkjrong.submeter.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void addUser(UserDTO userDTO) {

        CopyOptions copyOptions = new CopyOptions();
        copyOptions.setIgnoreNullValue(Boolean.TRUE);
        copyOptions.setIgnoreError(Boolean.TRUE);

        User user = BeanUtil.copyProperties(userDTO, User.class);
        DateTime parse = DateUtil.parse(userDTO.getBirthday(), DatePattern.NORM_DATE_PATTERN);
        user.setBirthday(parse.getTime());

        userRepository.save(user);
    }


    @Override
    public List<UserVO> findAll() {

        List<User> userList = userRepository.findAll();

        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {
            CopyOptions copyOptions = new CopyOptions();
            copyOptions.setIgnoreNullValue(Boolean.TRUE);
            copyOptions.setIgnoreError(Boolean.TRUE);
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            userVO.setBirthday(DateUtil.format(new Date(user.getBirthday()), DatePattern.NORM_DATE_PATTERN));
            userVOList.add(userVO);

        }

        return userVOList;
    }
}
