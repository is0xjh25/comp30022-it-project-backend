package tech.crm.crmserver.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Status Enum for all the entity who have status
 * @author Yongfeng
 * @since 2021-09-18
 */
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
