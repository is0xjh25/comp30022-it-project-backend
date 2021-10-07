package tech.crm.crmserver.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.validator.NullOrNotBlank;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * <p>
 *  DTO for update contact
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactUpdateDTO {

    @NotNull(message = "id cannot be null")
    @Positive(message = "id should be positive")
    private Integer id;

    @NullOrNotBlank
    @Email(message = "invalid email for contact")
    private String email;

    @NullOrNotBlank
    private String firstName;

    private String middleName;

    @NullOrNotBlank
    private String lastName;

    private String phone;

    private String description;

    private String gender;

    private LocalDate birthday;

    private String address;

    private String organization;

    private String customerType;
}
