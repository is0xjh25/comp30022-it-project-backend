package tech.crm.crmserver.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ToDoListStatus {


    ACTIVE("active"),
    DELETED("deleted"),
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
