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

    @NullOrNotBlank(message = "email should be null or not blank")
    @Email(message = "invalid email for contact")
    private String email;

    @NullOrNotBlank(message = "first name should be null or not blank")
    private String firstName;

    private String middleName;

    @NullOrNotBlank(message = "last name should be null or not blank")
    private String lastName;

    private String phone;

    private String description;

    private String gender;

    private LocalDate birthday;

    private String address;

    private String organization;

    private String customerType;
}
