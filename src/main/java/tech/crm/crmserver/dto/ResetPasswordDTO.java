package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *  DTO for reset password
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO {

    @NotNull(message = "email cannot be null")
    @Email(message = "invalid email")
    private String email;
}
