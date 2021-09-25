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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.constants.SecurityConstants;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrganizationControllerTest {

    @Autowired
    private MockMvc mvc;

    private static String token;

    /**
     * login before test
     * @throws Exception
     */
    @BeforeEach
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
     * Test if myOrganization can find all my organization
     * @throws Exception
     */
    @Test
    @Order(1)
    public void testAmyOrganization() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/organization/myOrganization").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andReturn();

    }

    /**
     * Test the create organization API, and if there is a same name organization exits
     * the create result would be fail with bad request
     * @throws Exception
     */
    @Test
    @Order(2)
    public void testBcreateOrganization() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/organization").param("organization_name", "TestingOrganization").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"));

        mvc.perform(MockMvcRequestBuilders.post("/organization").param("organization_name", "TestingOrganization").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(ExceptionMessageConstants.ORGANIZATION_ALREADY_EXIST_EXCEPTION));

        mvc.perform(MockMvcRequestBuilders.get("/organization/name").param("organization_name", "TestingOrganization").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"));
    }

    /**
     * Test the create organization API, and if there is a same name organization exits
     * the create result would be fail with bad request
     * @throws Exception
     */
    @Test
    @Order(3)
    public void testCgetOrganizationById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/organization").param("organization_id", "1").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"));

        mvc.perform(MockMvcRequestBuilders.get("/organization").param("organization_id", "100").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(ExceptionMessageConstants.ORGANIZATION_NOT_EXIST_EXCEPTION));
    }

    /**
     * Test searching organization based on name
     * @throws Exception
     */
    @Test
    @Order(4)
    public void testDgetOrganizationByName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/organization/name").param("organization_name", "TestingOrganization").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"));

        mvc.perform(MockMvcRequestBuilders.get("/organization/name").param("organization_name", "NotOrganizationWithThis").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(ExceptionMessageConstants.ORGANIZATION_NOT_FOUND_EXCEPTION));
    }

    /**
     * Test delete the organization
     *  @throws Exception
     */
    @Test
    @Order(5)
    @Transactional
    public void testEdeleteOrganizationById() throws Exception {
        // Delete the organization which is owned by user
        mvc.perform(MockMvcRequestBuilders.delete("/organization").param("organization_id", "1").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"));

        mvc.perform(MockMvcRequestBuilders.delete("/organization").param("organization_id", "100").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Organization do not exist!"));

        // Delete the organization which is not owned by user
        mvc.perform(MockMvcRequestBuilders.delete("/organization").param("organization_id", "2").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Sorry you do not have enough permissions to access it!"));
    }

    /**
     * Test join an organization
     *  @throws Exception
     */
    @Test
    @Order(6)
    public void testFJoinOrganization() throws Exception {
        // Join an organization created by other
        mvc.perform(MockMvcRequestBuilders.post("/organization/join").param("organization_id", "2").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"));

        // Join an organization created by myself
        mvc.perform(MockMvcRequestBuilders.post("/organization/join").param("organization_id", "3").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(ExceptionMessageConstants.USER_ALREADY_IN_ORGANIZATION_EXCEPTION));

        // Join organization with invalid organization id
        mvc.perform(MockMvcRequestBuilders.post("/organization/join").param("organization_id", "100").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(ExceptionMessageConstants.ORGANIZATION_NOT_EXIST_EXCEPTION));
    }

    /**
     * Test find all department in the organization
     *  @throws Exception
     */
    @Test
    @Order(7)
    public void testGFindDepartmentByOrganizationId() throws Exception {
        // Join an organization created by other
        mvc.perform(MockMvcRequestBuilders.get("/organization/departments").param("organization_id", "2").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"));

        mvc.perform(MockMvcRequestBuilders.get("/organization/departments").param("organization_id", "100").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Test create department
     *  @throws Exception
     */
    @Test
    @Order(8)
    public void testHCreateDepartment() throws Exception {
        // Create department of the organization create by other
        mvc.perform(MockMvcRequestBuilders.post("/organization/department").param("organization_id", "2").param("department_name", "department of test").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Sorry you do not have enough permissions to access it!"));

        // Create department of the organization which own by user
        mvc.perform(MockMvcRequestBuilders.post("/organization/department").param("organization_id", "3").param("department_name", "department of test").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Successfully create department!"));


    }
}