package tech.crm.crmserver.controllerTesting;

import org.junit.jupiter.api.*;
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
import tech.crm.crmserver.common.constants.SecurityConstants;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.dao.Department;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.service.DepartmentService;
import tech.crm.crmserver.service.PermissionService;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepartmentControllerTest {
    @Autowired
    private MockMvc mvc;

    private static String token;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private DepartmentService departmentService;

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
     * Test join a department and create a permission with permissionLevel 0
     * @throws Exception
     */
    @Test
    @Order(1)
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
    @Order(2)
    public void testAGetMemberofDepartment() throws Exception{
        int departmentId = 2;
        mvc.perform(MockMvcRequestBuilders.get("/department/member").param("department_id", String.valueOf(departmentId)).param("size", "4").param("current", "1").header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Success"))
                .andReturn();
    }

    /**
     * Test delete department by departmentId
     * @throws Exception
     */
    @Test
    @Order(3)
    public void testCDeleteDepartment() throws Exception{
        int departmentId = 2;
        mvc.perform(MockMvcRequestBuilders.delete("/department").param("department_id", String.valueOf(departmentId)).header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Successfully delete the department!"))
                .andReturn();

        Department department = departmentService.getById(departmentId);
        assert (department == null);
    }

}
