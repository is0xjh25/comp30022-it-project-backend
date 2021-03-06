package tech.crm.crmserver.controllerTesting;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.constants.SecurityConstants;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dto.LoginRequest;
import tech.crm.crmserver.service.PermissionService;
import tech.crm.crmserver.service.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PermissionControllerTesting {

    @Autowired
    private MockMvc mvc;

    private static String departmentOwnerToken;
    private static String departmentManagerToken;
    private static String departmentMemberToken;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    private static final String deleteSuccess = "Successfully delete the member!";
    private static final String updateSuccess = "Successfully update permission!";
    private static final String permissionDenied = ExceptionMessageConstants.NOT_ENOUGH_PERMISSION_EXCEPTION;


    /**
     * login the owner, manager and member of the department(id=1) before test
     */
    @BeforeEach
    public void loginBeforeTest(){
        //login the owner
        departmentOwnerToken = userService.login(new LoginRequest("lingxiao1@student.unimelb.edu.au","123456"));

        //login the manager
        departmentManagerToken = userService.login(new LoginRequest("test3@student.unimelb.edu.au","123456"));

        //login the member
        departmentMemberToken = userService.login(new LoginRequest("test4@student.unimelb.edu.au","123456"));

    }

    /**
     * delete the member by department owner
     */
    @Test
    @Transactional
    @Order(1)
    public void deleteMemberByOwnerTest() throws Exception{
        //member has the permission before
        Integer userId = 4;
        Integer departmentId = 1;
        Permission permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        System.out.println(permission);

        //check response
        ResultActions resultActions = deleteMember(userId, departmentId, departmentOwnerToken);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(deleteSuccess));

        //check whether delete the permission
        permission = permissionService.findPermission(departmentId, userId);
        assert (permission == null);
        System.out.println("permission is null");
    }

    /**
     * delete the member by department manager
     */
    @Test
    @Transactional
    @Order(2)
    public void deleteMemberByManagerTest() throws Exception{
        //member has the permission before
        Integer userId = 4;
        Integer departmentId = 1;
        Permission permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        System.out.println(permission);

        //check response
        ResultActions resultActions = deleteMember(userId, departmentId, departmentManagerToken);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(deleteSuccess));

        //check whether delete the permission
        permission = permissionService.findPermission(departmentId, userId);
        assert (permission == null);
        System.out.println("permission is null");
    }

    /**
     * delete the member by department member<br/>
     * should not delete the member because he do not have enough permission to access it
     */
    @Test
    @Transactional
    @Order(3)
    public void deleteMemberByMemberTest() throws Exception{
        //member has the permission before
        Integer userId = 5;
        Integer departmentId = 1;
        Permission permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        System.out.println(permission);

        //check response
        ResultActions resultActions = deleteMember(userId, departmentId, departmentMemberToken);
        resultActions.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(permissionDenied));

        //member still has the permission
        permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        System.out.println(permission);
    }

    /**
     * delete the member by department member<br/>
     * should not delete the member because he do not have enough permission to access it
     */
    @Test
    @Transactional
    @Order(4)
    public void deleteManagerByManagerTest() throws Exception{
        //member has the permission before
        Integer userId = 6;
        Integer departmentId = 1;
        Permission permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        System.out.println(permission);

        //check response
        ResultActions resultActions = deleteMember(userId, departmentId, departmentManagerToken);
        resultActions.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(permissionDenied));

        //member still has the permission
        permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        System.out.println(permission);
    }

    /**
     * Update permission by Owner to level 4<br/>
     * member has the permission before
     */
    @Test
    @Transactional
    @Order(5)
    public void updatePermissionByOwnerTest() throws Exception{
        //member has the permission before
        Integer userId = 5;
        Integer departmentId = 1;
        Permission permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        assert (permission.getAuthorityLevel().getLevel().equals(PermissionLevel.PENDING.getLevel()));
        System.out.println(permission);

        //check response
        ResultActions resultActions = updatePermission(userId, departmentId, departmentOwnerToken, PermissionLevel.MANAGE.getLevel());
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(updateSuccess));

        //member still has the permission
        permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        assert (permission.getAuthorityLevel().getLevel().equals(PermissionLevel.MANAGE.getLevel()));
        System.out.println(permission);
    }

    /**
     * Update permission by Manager to level 3<br/>
     * member has the permission before
     */
    @Test
    @Transactional
    @Order(6)
    public void updatePermissionByManagerTest() throws Exception{
        //member has the permission before
        Integer userId = 5;
        Integer departmentId = 1;
        Permission permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        assert (permission.getAuthorityLevel().getLevel().equals(PermissionLevel.PENDING.getLevel()));
        System.out.println(permission);

        //check response
        ResultActions resultActions = updatePermission(userId, departmentId, departmentManagerToken, PermissionLevel.DELETE.getLevel());
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(updateSuccess));

        //member still has the permission
        permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        assert (permission.getAuthorityLevel().getLevel().equals(PermissionLevel.DELETE.getLevel()));
        System.out.println(permission);
    }

    /**
     * Update permission by Member to level 1 <br/>
     * should not change permission <br/>
     * member has the permission before
     */
    @Test
    @Transactional
    @Order(7)
    public void updatePermissionByMemberTest() throws Exception{
        //member has the permission before
        Integer userId = 5;
        Integer departmentId = 1;
        Permission permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        assert (permission.getAuthorityLevel().getLevel().equals(PermissionLevel.PENDING.getLevel()));
        System.out.println(permission);

        //check response
        ResultActions resultActions = updatePermission(userId, departmentId, departmentMemberToken, PermissionLevel.DISPLAY.getLevel());
        resultActions.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(permissionDenied));

        //member still has the permission
        permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        assert (permission.getAuthorityLevel().getLevel().equals(PermissionLevel.PENDING.getLevel()));
        System.out.println(permission);
    }

    /**
     * Update permission by Stranger(Not a member of department) to level 1 <br/>
     * should not change permission <br/>
     * member has the permission before
     */
    @Test
    @Transactional
    @Order(8)
    public void updatePermissionByStrangerTest() throws Exception{
        //member has the permission before
        Integer userId = 5;
        Integer departmentId = 2;
        Permission permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        assert (permission.getAuthorityLevel().getLevel().equals(PermissionLevel.PENDING.getLevel()));
        System.out.println(permission);

        //check response
        ResultActions resultActions = updatePermission(userId, departmentId, departmentMemberToken, PermissionLevel.DISPLAY.getLevel());
        resultActions.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(permissionDenied));

        //member still has the permission
        permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        assert (permission.getAuthorityLevel().getLevel().equals(PermissionLevel.PENDING.getLevel()));
        System.out.println(permission);
    }

    /**
     * Update permission by Owner to level 5(Owner level permission) <br/>
     * should not change permission <br/>
     * member has the permission before
     */
    @Test
    @Transactional
    @Order(9)
    public void updatePermissionByOwnerToOwnerTest() throws Exception{
        //member has the permission before
        Integer userId = 5;
        Integer departmentId = 1;
        Permission permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        assert (permission.getAuthorityLevel().getLevel().equals(PermissionLevel.PENDING.getLevel()));
        System.out.println(permission);

        //check response
        ResultActions resultActions = updatePermission(userId, departmentId, departmentOwnerToken, PermissionLevel.OWNER.getLevel());
        resultActions.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(permissionDenied));

        //member still has the permission
        permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        assert (permission.getAuthorityLevel().getLevel().equals(PermissionLevel.PENDING.getLevel()));
        System.out.println(permission);
    }

    /**
     * Update permission by Owner to level 4<br/>
     * the member is a Stranger(Not a member of department) before
     */
    @Test
    @Transactional
    @Order(10)
    public void updatePermissionByOwnerOfStrangerTest() throws Exception{
        //member has the permission before
        Integer userId = 6;
        Integer departmentId = 2;
        Permission permission = permissionService.findPermission(departmentId, userId);
        assert (permission == null);
        System.out.println("permission is null");

        //check response
        ResultActions resultActions = updatePermission(userId, departmentId, departmentOwnerToken, PermissionLevel.MANAGE.getLevel());
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(updateSuccess));

        //member still has the permission
        permission = permissionService.findPermission(departmentId, userId);
        assert (permission != null);
        assert (permission.getAuthorityLevel().getLevel().equals(PermissionLevel.MANAGE.getLevel()));
        System.out.println(permission);
    }



    /**
     * test delete a member of department function by token owner
     * @param userId the member need to be delete from the department
     * @param departmentId the department
     * @param token the token of user who execute the deletion
     */
    @Transactional
    @Order(11)
    public ResultActions deleteMember(Integer userId, Integer departmentId, String token) throws Exception{
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.delete("/permission")
                .header(SecurityConstants.TOKEN_HEADER, token)
                .param("user_id", Integer.toString(userId))
                .param("department_id", Integer.toString(departmentId)));

        return resultActions;

    }

    /**
     * update permission of user by token owner
     * @param userId the member need to be delete from the department
     * @param departmentId the department
     * @param token the token of user who execute the deletion
     * @param permissionLevel update to permissionLevel
     */
    @Transactional
    @Order(12)
    public ResultActions updatePermission(Integer userId, Integer departmentId, String token, Integer permissionLevel) throws Exception{
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.put("/permission")
                .header(SecurityConstants.TOKEN_HEADER, token)
                .param("user_id", Integer.toString(userId))
                .param("department_id", Integer.toString(departmentId))
                .param("permission_level", Integer.toString(permissionLevel)));

        return resultActions;

    }




}
