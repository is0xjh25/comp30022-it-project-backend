package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *  DTO for User for register
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotNull(message = "email cannot be null")
    @Email(message = "invalid email")
    private String email;

    @NotNull(message = "password cannot be null")
    private String password;

    @NotNull(message = "firstName cannot be null")
    private String firstName;

    private String middleName;

    @NotNull(message = "lastName cannot be null")
    private String lastName;

    @NotNull(message = "phone cannot be null")
    private String phone;

    private String website;

    private String description;

}
