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
import tech.crm.crmserver.common.constants.SecurityConstants;
import tech.crm.crmserver.common.enums.ToDoListStatus;
import tech.crm.crmserver.dao.Event;
import tech.crm.crmserver.dao.ToDoList;
import tech.crm.crmserver.service.EventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventControllerTesting {

    @Autowired
    private MockMvc mvc;

    private static String token;

    @Autowired
    private EventService eventService;

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
     * Testing creating event data for a user
     * @throws Exception
     */
    @Test
    @Order(1)
    public void testAAddNewEvent() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/events").header(SecurityConstants.TOKEN_HEADER,token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"start_time\": \"2021-09-30 19:20\", \n" +
                        "    \"finish_time\": \"2021-10-01 19:20\", \n" +
                        "    \"description\": \"zoom meeting\" \n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Event> test = eventService.queryEvent(null, null, null, null, ToDoListStatus.ACTIVE, "zoom meeting");
        assert (test.size() == 1);
        // contactId = contactBasedOnSomeConditionFromDB.get(0).getId();
        // assert (contactBasedOnSomeConditionFromDB.size() == 1);
    }
}