package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 *  DTO for Login form
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-18
 */
@Data
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "email cannot be null")
    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "password cannot be null")
    private String password;
}
