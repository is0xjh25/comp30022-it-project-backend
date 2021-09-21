package tech.crm.crmserver.controllerTesting;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tech.crm.crmserver.CrmServerApplication;
import tech.crm.crmserver.mapper.UserMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

//    @Autowired
//    private MockMvc mvc;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void loginTest() throws Exception {

//        mvc.perform(MockMvcRequestBuilders.post("/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\n" +
//                        "    \"email\": \"lingxiao1@student.unimelb.edu.au\",\n" +
//                        "    \"password\": \"123456\"\n" +
//                        "}"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());

        System.out.println(userMapper.selectById(2));




    }
}

