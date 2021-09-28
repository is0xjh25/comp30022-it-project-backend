package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *  DTO for User for update
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {

    //raw password
    private String password;

    private String firstName;

    private String middleName;

    private String lastName;

    private String phone;

    private String website;

    private String description;

}
