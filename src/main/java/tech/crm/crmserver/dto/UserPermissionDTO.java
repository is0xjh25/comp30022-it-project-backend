package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.enums.PermissionLevel;

/**
 * <p>
 *  DTO for User Permission
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-18
 */
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
