package com.example.SqsxUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * 持久层
 * <p/>
 */
@Repository
public interface SqsxUserRepository extends JpaRepository<SqsxUser, Integer>
{
    //使用JpaRepository简化开发流程，非常舒服地定义简单的service 接口即可，会自动实现
    SqsxUser findByUsername(String username);

//    Integer login(String name, String password);

    SqsxUser findById(Integer id);

    @Query(value = "select * from sqsx_user where sqsx_user.isdel = 0 and username =?1 ",nativeQuery = true)
    public List<SqsxUser> findByUserName(String username);
}
