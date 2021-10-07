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
    @NullOrNotBlank
    private String password;

    @NullOrNotBlank
    private String firstName;

    @NullOrNotBlank
    private String middleName;

    @NullOrNotBlank
    private String lastName;

    @NullOrNotBlank
    private String phone;

    @NullOrNotBlank
    private String website;

    @NullOrNotBlank
    private String description;

}
