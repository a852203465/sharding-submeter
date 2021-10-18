package cn.darkjrong.submeter.service;

import cn.darkjrong.submeter.common.pojo.dto.UserDTO;
import cn.darkjrong.submeter.common.pojo.vo.UserVO;

import java.util.List;

public interface UserService {

    void addUser(UserDTO userDTO);

    List<UserVO> findAll();


}
