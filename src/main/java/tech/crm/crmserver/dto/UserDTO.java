package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.enums.Status;

import java.time.LocalDateTime;

/**
 * <p>
 *  DTO for get User
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Integer id;

    private String email;

    private String password;

    private String firstName;

    private String middleName;

    private String lastName;

    private String phone;

    private LocalDateTime recentActivity;

    private String website;

    private String description;

    private Status status;

    private String photo;

}
