package tech.crm.crmserver.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ToDoListStatus {


    TO_DO("to do"),
    IN_PROGRESS("in progress"),
    DONE("done");

    @EnumValue
    @JsonValue
    private String status;

    ToDoListStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
