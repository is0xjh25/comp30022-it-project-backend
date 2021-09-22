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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.constants.SecurityConstants;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dto.LoginRequest;
import tech.crm.crmserver.service.PermissionService;
import tech.crm.crmserver.service.UserService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DepartmentControllerTest {
    @Autowired
    private MockMvc mvc;

    private static String token;

    @Autowired
    private PermissionService permissionService;

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
     * Test join a department and create a permission with permissionLevel 0
     * @throws Exception
     */
    @Test
    public void testBJoinDepartment() throws Exception {
        int departmentId = 3;
        mvc.perform(MockMvcRequestBuilders.post("/department/join").param("department_id", String.valueOf(departmentId)).header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Successfully create permission!"))
                .andReturn();

        List<Permission> permissions = permissionService.getPermissionByUserId(1, PermissionLevel.PENDING);
        boolean permissionCreate = false;
        for (Permission permission : permissions) {
            if (permission.getDepartmentId() == departmentId) {
                permissionCreate = true;
            }
        }
        assert (permissionCreate = true);
    }

    /**
     * Test get members of a department
     * @throws Exception
     */
    @Test
    public void testAGetMemberofDepartment() throws Exception{
        int departmentId = 3;
        mvc.perform(MockMvcRequestBuilders.post("/department/join").param("department_id", String.valueOf(departmentId)).header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Successfully create permission!"))
                .andReturn();
    }

}
