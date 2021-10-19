package tech.crm.crmserver.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EventStatus {

    UPCOMING("upcoming"),
    IN_PROGRESS("in progress"),
    DONE("done");

    @EnumValue
    @JsonValue
    private String status;

    EventStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
