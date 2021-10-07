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

    @NullOrNotBlank
    private String middleName;

    @NullOrNotBlank
    private String lastName;

    @NullOrNotBlank
    private String phone;

    @NullOrNotBlank
    private String description;

    @NullOrNotBlank
    private String gender;

    private LocalDate birthday;

    @NullOrNotBlank
    private String address;

    @NullOrNotBlank
    private String organization;

    @NullOrNotBlank
    private String customerType;
}
