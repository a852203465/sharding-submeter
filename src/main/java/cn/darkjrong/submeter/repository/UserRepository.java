package cn.darkjrong.submeter.repository;

import cn.darkjrong.submeter.common.pojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
