package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
public class UserRegisterDTO {

    @NotBlank(message = "email cannot be null")
    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "password cannot be null or empty")
    private String password;

    @NotBlank(message = "first_name cannot be null")
    private String firstName;

    private String middleName;

    @NotBlank(message = "last_name cannot be null")
    private String lastName;

    @NotBlank(message = "phone cannot be null")
    private String phone;

    private String website;

    private String description;

}
