package tech.crm.crmserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, COMP30022";
    }

    @GetMapping("/introduction")
    public String introduction() {
        return "Hi, it's Team35 here. This project is about a person CRM.";
    }

}
