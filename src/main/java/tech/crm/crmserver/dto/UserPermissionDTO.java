package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.enums.PermissionLevel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionDTO {

    private Integer userId;
    private Integer departmentId;
    private Integer permissionId;
    private PermissionLevel authorityLevel;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;

}
