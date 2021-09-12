package tech.crm.crmserver.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {

    ACTIVE("active"),
    DELETED("deleted"),
    PENDING("pending"),
    DONE("done")
    ;

    @EnumValue
    @JsonValue
    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
