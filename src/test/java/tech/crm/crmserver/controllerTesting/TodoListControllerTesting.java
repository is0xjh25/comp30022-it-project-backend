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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import tech.crm.crmserver.common.constants.SecurityConstants;
import tech.crm.crmserver.common.enums.ToDoListStatus;
import tech.crm.crmserver.dao.ToDoList;
import tech.crm.crmserver.service.ToDoListService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoListControllerTesting {

    @Autowired
    private MockMvc mvc;

    private static String token;

    @Autowired
    private ToDoListService toDoListService;

    /**
     * login before test
     * @throws Exception
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
     * Testing creating todoList data for a user
     * @throws Exception
     */
    @Test
    @Order(1)
    public void testAAddNewTodoListData() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/toDoList").header(SecurityConstants.TOKEN_HEADER,token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"date_time\": \"2021-01-01 19:00\", \n" +
                        "    \"description\": \"test\", \n" +
                        "    \"status\": \"to do\" \n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String str = "2021-01-01 19:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        List<ToDoList> test = toDoListService.queryTodoList(null, null, "test", dateTime, ToDoListStatus.TO_DO);
        assert (test.size() == 1);
        // contactId = contactBasedOnSomeConditionFromDB.get(0).getId();
        // assert (contactBasedOnSomeConditionFromDB.size() == 1);
    }

    /**
     * Testing querying todoList data for a user
     * @throws Exception
     */
    @Test
    @Order(2)
    public void testBQueryNewTodoListData() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/toDoList").param("topNTodoListData", String.valueOf(2)).header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andReturn();
        // contactId = contactBasedOnSomeConditionFromDB.get(0).getId();
        // assert (contactBasedOnSomeConditionFromDB.size() == 1);
    }

    /**
     * Testing update toDoList data for a user
     * @throws Exception
     */
    @Test
    @Order(3)
    @Transactional
    public void testCUpdateToDoListData() throws Exception {
        // Create mock data and check its format
        String startTime = "2021-02-02 20:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(startTime, formatter);

        String description = "Watch Moc lecture";

        // Try to insert a new to-do list
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/toDoList").header(SecurityConstants.TOKEN_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "   \"date_time\" : \"" + startTime + "\", \n" +
                        "   \"description\": \"" + description + "\", \n" +
                        "   \"status\": \"to do\" \n" +
                        "}"
                ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Check if the insert is successful
        List<ToDoList> testInsert = toDoListService.queryTodoList(null, null, description, null, null);
        ToDoList test = testInsert.get(0);
        Integer testId = test.getId();
        System.out.println(test);
        assert (test.getDateTime().equals(dateTime));
        assert (test.getDescription().equals(description));

        // Create mock update data and check its format
        String startUpdate = "2021-10-02 20:00";
        LocalDateTime dateTimeUpdate = LocalDateTime.parse(startUpdate, formatter);

        String descriptionUpdate = "Watch GI lecture";

        // Try to update the to-do list
        MvcResult mvcResultUpdate = mvc.perform(MockMvcRequestBuilders.put("/toDoList").header(SecurityConstants.TOKEN_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\n" +
                        "   \"id\" : \"" + String.valueOf(testId) + "\", \n" +
                        "   \"date_time\" : \"" + startUpdate + "\", \n" +
                        "   \"description\": \"" + descriptionUpdate + "\", \n" +
                        "   \"status\": \"to do\" \n" +
                        "}"

                ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Check if the update is successful
        List<ToDoList> testUpdate = toDoListService.queryTodoList(testId, null, null, null, null);
        assert (testUpdate.get(0).getDateTime().equals(dateTimeUpdate));
        assert (testUpdate.get(0).getDescription().equals(descriptionUpdate));
    }

    /**
     * Testing delete a to-do list
     * @throws Exception
     */
    @Test
    @Order(4)
    @Transactional
    public void testDDeleteToDoListData() throws Exception {
        // Create mock data and check its format
        String startTime = "2021-02-02 20:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(startTime, formatter);

        String description = "Watch Moc lecture";

        // Try to insert a new to-do list
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/toDoList").header(SecurityConstants.TOKEN_HEADER, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\n" +
                                    " \"date_time\" : \"" + startTime + "\", \n" +
                                    " \"description\": \"" + description + "\", \n" +
                                    " \"status\": \"to do\" \n" +
                                "}"
                        ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Check if the insert is successful
        List<ToDoList> testInsert = toDoListService.queryTodoList(null, null, description, null, null);
        ToDoList test = testInsert.get(0);
        Integer testId = test.getId();
        assert (test.getDateTime().equals(dateTime));
        assert (test.getDescription().equals(description));

        // Try to delete this data
        MvcResult mvcResultDelete = mvc.perform(MockMvcRequestBuilders.delete("/toDoList").param("todoList_id", String.valueOf(testId)).header(SecurityConstants.TOKEN_HEADER, token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Check if the deleting is successful
        List<ToDoList> testDelete = toDoListService.queryTodoList(testId, null, null, null, null);
        assert (testDelete.size() == 0);
    }
}
