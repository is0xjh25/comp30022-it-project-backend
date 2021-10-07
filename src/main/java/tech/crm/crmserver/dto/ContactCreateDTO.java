package tech.crm.crmserver.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.validator.NullOrNotBlank;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * <p>
 *  DTO for create Organization
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactCreateDTO {

    @Null
    private Integer id;

    @NotNull(message = "department_id cannot be null")
    @Positive(message = "department_id should be positive")
    @JsonProperty("department_id")
    private Integer departmentId;

    @NotBlank(message = "email cannot be null")
    @Email(message = "invalid email for contact")
    private String email;

    @NotBlank(message = "first_name cannot be null")
    private String firstName;

    private String middleName;

    @NotBlank(message = "last_name cannot be null")
    private String lastName;

    private String phone;

    private String description;

    private String gender;

    private LocalDate birthday;

    private String address;

    private String organization;

    private String customerType;

}
