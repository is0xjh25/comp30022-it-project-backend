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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tech.crm.crmserver.common.constants.SecurityConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrganizationControllerTesting {

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

    /**
     * Test if myOrganization can find all my organization
     */
    @Test
    public void myOrganization() throws Exception {
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
    public void createOrganization() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/organization").param("organization_name", "TestingOrganization").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"));

        mvc.perform(MockMvcRequestBuilders.post("/organization").param("organization_name", "TestingOrganization").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Organization with same name exists"));
    }

    /**
     * Test the create organization API, and if there is a same name organization exits
     * the create result would be fail with bad request
     * @throws Exception
     */
    @Test
    public void getOrganizationById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/organization").param("organization_id", "1").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"));

        mvc.perform(MockMvcRequestBuilders.get("/organization").param("organization_id", "100").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("No content"));
    }

    /**
     * Test searching organization based on name
     * @throws Exception
     */
    @Test
    public void getOrganizationByName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/organization").param("organization_name", "TestingOrganization").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"));

        mvc.perform(MockMvcRequestBuilders.get("/organization").param("organization_name", "NotOrganizationWithThis").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("No content"));
    }
}
