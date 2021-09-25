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
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dao.Department;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dto.LoginRequest;
import tech.crm.crmserver.service.ContactService;
import tech.crm.crmserver.service.DepartmentService;
import tech.crm.crmserver.service.PermissionService;
import tech.crm.crmserver.service.UserService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContactControllerTest {

    @Autowired
    private MockMvc mvc;

    private static String token;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ContactService contactService;

    private int contactId = 10;

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
     * Test add a contact into the department
     * @throws Exception
     */
    @Test
    public void testAAddNewContact() throws Exception {
        int departmentId = 2;

        String email = "test@gamil.com";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/contact").header(SecurityConstants.TOKEN_HEADER,token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"department_id\": \"2\", \n" +
                        "    \"email\": \"test@gamil.com\", \n" +
                        "    \"first_name\": \"testFirst\", \n" +
                        "    \"last_name\": \"testLast\", \n" +
                        "    \"gender\": \"male\", \n" +
                        "    \"birthday\": \"2020-01-01\", \n" +
                        "    \"status\": \"active\" \n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<Contact> contactBasedOnSomeConditionFromDB = contactService.getContactBasedOnSomeConditionFromDB(departmentId, email, null, null, null, null, null, null, null);
        contactId = contactBasedOnSomeConditionFromDB.get(0).getId();
        assert (contactBasedOnSomeConditionFromDB.size() == 1);
    }

    /**
     * Test update a contact
     * @throws Exception
     */
    @Test
    public void testBUpdatingContact() throws Exception {
        int departmentId = 2;


        String email = "te@gamil.com";

        String updateEmail = "updateEmail@gamil.com";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/contact").header(SecurityConstants.TOKEN_HEADER,token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"department_id\": \"2\", \n" +
                        "    \"email\": \"te@gamil.com\", \n" +
                        "    \"first_name\": \"testFirst\", \n" +
                        "    \"last_name\": \"testLast\", \n" +
                        "    \"gender\": \"male\", \n" +
                        "    \"birthday\": \"2020-01-01\", \n" +
                        "    \"status\": \"active\" \n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Contact> contactBasedOnSomeConditionFromDB = contactService.getContactBasedOnSomeConditionFromDB(departmentId, email, null, null, null, null, null, null, null);
        contactId = contactBasedOnSomeConditionFromDB.get(0).getId();

        MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.put("/contact").header(SecurityConstants.TOKEN_HEADER,token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"id\": \"" +String.valueOf(contactId) + "\", \n" +
                        "    \"department_id\": \"2\", \n" +
                        "    \"email\": \"updateEmail@gamil.com\", \n" +
                        "    \"first_name\": \"updateFirstName\", \n" +
                        "    \"last_name\": \"testLast\", \n" +
                        "    \"gender\": \"male\", \n" +
                        "    \"birthday\": \"2020-01-01\", \n" +
                        "    \"status\": \"active\" \n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<Contact> contactBasedOnSomeConditionFromDB2 = contactService.getContactBasedOnSomeConditionFromDB(departmentId, updateEmail, null, null, null, null, null, null, null);
        assert (contactBasedOnSomeConditionFromDB2.get(0).getFirstName().equals("updateFirstName"));
        assert (contactBasedOnSomeConditionFromDB2.size() == 1);
    }


    /**
     * Test delete a contact
     * @throws Exception
     */
    @Test
    public void testCDeleteContact() throws Exception {
        int departmentId = 2;


        String email = "testDelete@gamil.com";

        String updateEmail = "updateEmail@gamil.com";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/contact").header(SecurityConstants.TOKEN_HEADER,token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"department_id\": \"2\", \n" +
                        "    \"email\": \"testDelete@gamil.com\", \n" +
                        "    \"first_name\": \"testFirst\", \n" +
                        "    \"last_name\": \"testLast\", \n" +
                        "    \"gender\": \"male\", \n" +
                        "    \"birthday\": \"2020-01-01\", \n" +
                        "    \"status\": \"active\" \n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Contact> contactBasedOnSomeConditionFromDB = contactService.getContactBasedOnSomeConditionFromDB(departmentId, email, null, null, null, null, null, null, null);
        contactId = contactBasedOnSomeConditionFromDB.get(0).getId();

        MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.delete("/contact").param("contact_id", String.valueOf(contactId)).header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Contact contact = contactService.getById(contactId);
        assert (contact == null);
    }

    /**
     * Test get a contact by id
     * @throws Exception
     */
    @Test
    public void testDGetContactDetails() throws Exception {
        int departmentId = 2;



        String email = "testGetContact@gamil.com";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/contact").header(SecurityConstants.TOKEN_HEADER,token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"department_id\": \"2\", \n" +
                        "    \"email\": \"testGetContact@gamil.com\", \n" +
                        "    \"first_name\": \"testFirst\", \n" +
                        "    \"last_name\": \"testLast\", \n" +
                        "    \"gender\": \"male\", \n" +
                        "    \"birthday\": \"2020-01-01\", \n" +
                        "    \"status\": \"active\" \n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Contact> contactBasedOnSomeConditionFromDB = contactService.getContactBasedOnSomeConditionFromDB(departmentId, email, null, null, null, null, null, null, null);
        contactId = contactBasedOnSomeConditionFromDB.get(0).getId();

        MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.get("/contact/detail").param("contact_id", String.valueOf(contactId)).header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Contact contact = contactService.getById(contactId);
        assert (contact != null);
    }

}