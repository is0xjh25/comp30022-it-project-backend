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
    @Transactional
    public void testAAddNewEvent() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/event").header(SecurityConstants.TOKEN_HEADER,token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"start_time\": \"2021-09-30 19:20\", \n" +
                        "    \"finish_time\": \"2021-10-01 19:20\", \n" +
                        "    \"description\": \"zoom meeting\" \n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Event> test = eventService.queryEvent(null, null, null, null, null, "zoom meeting");
        assert (test.size() == 1);
        // contactId = contactBasedOnSomeConditionFromDB.get(0).getId();
        // assert (contactBasedOnSomeConditionFromDB.size() == 1);
    }

    /**
     * Testing query event
     * @throws Exception
     */
    @Test
    @Order(2)
    @Transactional
    public void testBQueryEventsData() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/event").header(SecurityConstants.TOKEN_HEADER,token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"start_time\": \"2021-09-30 19:20\", \n" +
                        "    \"finish_time\": \"2021-10-01 19:20\", \n" +
                        "    \"description\": \"zoom meeting\" \n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Event> test = eventService.queryEvent(null, null, null, null, null, "zoom meeting");
        Integer eventId = test.get(0).getId();

        MvcResult mvcResult2 = mvc.perform(MockMvcRequestBuilders.get("/event").param("event_id", String.valueOf(eventId)).header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // contactId = contactBasedOnSomeConditionFromDB.get(0).getId();
        // assert (contactBasedOnSomeConditionFromDB.size() == 1);
    }

    /**
     * Testing update event
     * @throws Exception
     */
    @Test
    @Order(3)
    @Transactional
    public void testCUpdateEventsData() throws Exception {
        // Insert a new event
        String startTime = "2021-09-30 19:20";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTimeStart = LocalDateTime.parse(startTime, formatter);

        String finishTime = "2021-10-01 19:20";
        LocalDateTime dateTimeFinish = LocalDateTime.parse(finishTime, formatter);

        String description = "zoom meeting";
        // Add a new event first
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/event").header(SecurityConstants.TOKEN_HEADER,token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"start_time\": \"" + startTime + "\", \n" +
                        "    \"finish_time\": \"" + finishTime + "\", \n" +
                        "    \"description\": \"zoom meeting\" \n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Event> test = eventService.queryEvent(null, null, null, null, null, "zoom meeting");
        Integer eventId = test.get(0).getId();
        System.out.println(dateTimeFinish);
        assert (test.get(0).getFinishTime().equals(dateTimeFinish));
        assert (test.get(0).getStartTime().equals(dateTimeStart));
        assert (test.get(0).getDescription().equals(description));

        // Update event, update startTime, finishTime, description and status
        String startTimeUpdate = "2021-09-29 19:20";
        DateTimeFormatter formatterUpdate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTimeStartUpdate = LocalDateTime.parse(startTimeUpdate, formatter);

        String finishTimeUpdate = "2021-10-04 19:20";
        LocalDateTime dateTimeFinishUpdate = LocalDateTime.parse(finishTimeUpdate, formatter);

        String descriptionUpdate = "zoom meeting, processing";
        ToDoListStatus status = ToDoListStatus.IN_PROGRESS;
        MvcResult mvcResultUpdate = mvc.perform(MockMvcRequestBuilders.put("/event").header(SecurityConstants.TOKEN_HEADER,token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"start_time\": \"" + startTimeUpdate + "\", \n" +
                        "    \"finish_time\": \"" + finishTimeUpdate + "\", \n" +
                        "    \"description\": \"" + descriptionUpdate + "\", \n" +
                        "    \"id\": \"" + String.valueOf(eventId) + "\", \n" +
                        "    \"status\": \"" + status.getStatus() + "\" \n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Event> testEvent = eventService.queryEvent(eventId, null, null, null, null, null);
        System.out.println(testEvent.get(0));
        System.out.println(dateTimeFinishUpdate);
        System.out.println(testEvent.get(0).getFinishTime());
        assert (testEvent.get(0).getFinishTime().equals(dateTimeFinishUpdate));
        assert (testEvent.get(0).getStartTime().equals(dateTimeStartUpdate));
        assert (testEvent.get(0).getDescription().equals(descriptionUpdate));
    }

    /**
     * Testing delete events
     * @throws Exception
     */
    @Test
    @Order(4)
    @Transactional
    public void testCDeleteEventsData() throws Exception {
        // Insert a new event
        String startTime = "2021-09-30 19:20";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTimeStart = LocalDateTime.parse(startTime, formatter);

        String finishTime = "2021-10-01 19:20";
        LocalDateTime dateTimeFinish = LocalDateTime.parse(finishTime, formatter);

        String description = "zoom meeting";
        // Add a new event first
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/event").header(SecurityConstants.TOKEN_HEADER,token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"start_time\": \"" + startTime + "\", \n" +
                        "    \"finish_time\": \"" + finishTime + "\", \n" +
                        "    \"description\": \"zoom meeting\" \n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Event> test = eventService.queryEvent(null, null, null, null, null, "zoom meeting");
        Integer eventId = test.get(0).getId();
        System.out.println(dateTimeFinish);
        assert (test.get(0).getFinishTime().equals(dateTimeFinish));
        assert (test.get(0).getStartTime().equals(dateTimeStart));
        assert (test.get(0).getDescription().equals(description));


        MvcResult mvcResult2 = mvc.perform(MockMvcRequestBuilders.delete("/event").param("event_id", String.valueOf(eventId)).header(SecurityConstants.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Event> testEvent = eventService.queryEvent(eventId, null, null, null, null, null);
        assert (testEvent.size() == 0);
    }
}