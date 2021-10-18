package cn.darkjrong.submeter.controller;

import cn.darkjrong.submeter.common.pojo.dto.UserDTO;
import cn.darkjrong.submeter.service.UserService;
import cn.darkjrong.submeter.common.pojo.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> addUser(@RequestBody UserDTO userDTO) {

        log.info("addUser {}", userDTO.toString());

        userService.addUser(userDTO);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", "成功");

        return result;
    }

    @GetMapping("/findUsers")
    public List<UserVO> findUsers() {
        return userService.findAll();
    }
}
