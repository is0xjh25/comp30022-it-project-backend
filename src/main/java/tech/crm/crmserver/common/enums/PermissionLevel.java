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


    public static PermissionLevel getPermissionLevel(Integer value){
        for (PermissionLevel e:PermissionLevel.values()) {
            if(e.getLevel().equals(value))
                return e;
        }
        //not found
        return null;
    }


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

    public Boolean equal(PermissionLevel o){
        if(o == null){
            return false;
        }
        if(this.getLevel().equals(o.getLevel())){
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return "PermissionLevel{" +
                "permission='" + permission + '\'' +
                ", level=" + level +
                '}';
    }


}
