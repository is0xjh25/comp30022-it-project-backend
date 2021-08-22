package tech.crm.crmserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.mapper.UserMapper;
import tech.crm.crmserver.service.UserService;
import tech.crm.crmserver.service.impl.UserServiceImpl;

@SpringBootTest
public class serviceTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testUserMapper(){
        User user = new User();
        user.setEmail("lingxiao1@unimelb.edu.au");
        user.setUserName("Lingxiao");
        user.setFirstName("Lingxiao");
        user.setLastName("Li");
        user.setPhone("188");
        user.setPassword("123456");
        userMapper.insert(user);
    }

    @Autowired
    private UserService userService;

    @Test
    public void testUserService(){

        User user = new User();
        user.setEmail("lingxiao2@unimelb.edu.au");
        user.setUserName("Lingxiao");
        user.setFirstName("Lingxiao");
        user.setLastName("Li");
        user.setPhone("188");
        user.setPassword("123456");
        userService.save(user);
    }
}
