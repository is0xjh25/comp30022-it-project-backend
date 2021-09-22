package tech.crm.crmserver.controllerTesting;



import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tech.crm.crmserver.common.constants.SecurityConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {


    @Autowired
    private MockMvc mvc;

    private static String token;



    /**
     * login before test
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

    /**
     * login with invalid email
     */
    @Test
    public void loginInvalidEmailTest() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"email\": \"lingxiaostudent.unimelb.edu.au\",\n" +
                        "    \"password\": \"123456\"\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("invalid email"));

    }

    /**
     * login with incorrect email
     */
    @Test
    public void loginIncorrectEmailTest() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"email\": \"lingxiao2@student.unimelb.edu.au\",\n" +
                        "    \"password\": \"123456\"\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("The user email or password is not correct."));

    }

    /**
     * login with incorrect password
     */
    @Test
    public void loginIncorrectPasswordTest() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"email\": \"lingxiao1@student.unimelb.edu.au\",\n" +
                        "    \"password\": \"123443256\"\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("The user email or password is not correct."));

    }

    /**
     * register with correct input
     */
    @Test
    public void registerTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"email\": \"lingxiao2@student.unimelb.edu.au\",\n" +
                        "    \"firstName\": \"lingxiao\",\n" +
                        "    \"lastName\": \"li\",\n" +
                        "    \"password\": \"123456\",\n" +
                        "    \"phone\": \"188188\"\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(SecurityConstants.TOKEN_HEADER))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("successfully sign up!"));

        //get user to check whether insert data into database
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"email\": \"lingxiao2@student.unimelb.edu.au\",\n" +
                        "    \"password\": \"123456\"\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String token = mvcResult.getResponse().getHeader(SecurityConstants.TOKEN_HEADER);

        mvc.perform(MockMvcRequestBuilders.get("/user").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("lingxiao2@student.unimelb.edu.au"));
    }

    /**
     * register using exist email
     */
    @Test
    public void registerExistUserTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"email\": \"lingxiao1@student.unimelb.edu.au\",\n" +
                        "    \"firstName\": \"lingxiao\",\n" +
                        "    \"lastName\": \"li\",\n" +
                        "    \"password\": \"123456\",\n" +
                        "    \"phone\": \"188188\"\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Same email already exist!"));
    }


    /**
     * get current user with token
     */
    @Test
    public void getUserTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("lingxiao1@student.unimelb.edu.au"));
    }

    /**
     * logout with token
     */
    @Test
    public void logoutTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/logout").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("successfully logout!"));
    }


}

