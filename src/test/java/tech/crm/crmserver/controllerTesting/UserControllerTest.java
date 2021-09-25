package tech.crm.crmserver.controllerTesting;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import tech.crm.crmserver.common.constants.SecurityConstants;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {


    @Autowired
    private MockMvc mvc;

    private static String token;



    /**
     * login before test
     */
    @BeforeEach
    public void loginTest() throws Exception {

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
    @Order(1)
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
    @Order(2)
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
    @Order(3)
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
    @Transactional
    @Order(4)
    public void registerTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"email\": \"lingxiao2@student.unimelb.edu.au\",\n" +
                        "    \"first_name\": \"lingxiao\",\n" +
                        "    \"last_name\": \"li\",\n" +
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
    @Order(5)
    public void registerExistUserTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"email\": \"lingxiao1@student.unimelb.edu.au\",\n" +
                        "    \"first_name\": \"lingxiao\",\n" +
                        "    \"last_name\": \"li\",\n" +
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
    @Order(6)
    public void getUserTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("lingxiao1@student.unimelb.edu.au"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.first_name").value("lingxiao"));
    }

    /**
     * logout with token
     */
    @Test
    @Order(7)
    public void logoutTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/logout").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("successfully logout!"));
    }


}

