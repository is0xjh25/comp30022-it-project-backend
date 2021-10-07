package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.validator.NullOrNotBlank;

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
    @NullOrNotBlank(message = "password should be null or not blank!")
    private String password;

    @NullOrNotBlank(message = "first name should be null or not blank!")
    private String firstName;

    private String middleName;

    @NullOrNotBlank(message = "last name should be null or not blank!")
    private String lastName;

    @NullOrNotBlank(message = "phone should be null or not blank!")
    private String phone;

    private String website;

    private String description;

}
