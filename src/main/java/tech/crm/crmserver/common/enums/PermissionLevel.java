package tech.crm.crmserver.common.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PermissionLevel {

    PENDING("pending",0),
    DISPLAY("display",1),
    UPDATE("insert and update",2),
    DELETE("delete",3),
    MANAGE("manage",4),
    OWNER("owner",5)
    ;


    private final String permission;

    @EnumValue
    @JsonValue
    private final Integer level;

    PermissionLevel(String permission, Integer level) {
        this.permission = permission;
        this.level = level;
    }

    public String getPermission() {
        return permission;
    }

    public Integer getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "PermissionLevel{" +
                "permission='" + permission + '\'' +
                ", level=" + level +
                '}';
    }


}
