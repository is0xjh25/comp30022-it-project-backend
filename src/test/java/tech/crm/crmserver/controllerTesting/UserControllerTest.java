package tech.crm.crmserver.controllerTesting;


import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tech.crm.crmserver.common.constants.SecurityConstants;
import tech.crm.crmserver.mapper.UserMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.JVM)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    private static String token;


    /**
     * login before test
     * @throws Exception
     */
    @Before
    public void loginTest() throws Exception {
        if(token != null){
            return;
        }
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"email\": \"lingxiao1@student.unimelb.edu.au\",\n" +
                        "    \"password\": \"123456\"\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        token = mvcResult.getResponse().getHeader(SecurityConstants.TOKEN_HEADER);

    }

    @Test
    public void myOrganization() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/organization/myOrganization").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }
}

