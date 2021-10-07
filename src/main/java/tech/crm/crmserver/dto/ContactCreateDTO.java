package tech.crm.crmserver.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.validator.NullOrNotBlank;

import javax.validation.constraints.*;
import java.time.LocalDate;

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

    @NullOrNotBlank
    private String middleName;

    @NotBlank(message = "last_name cannot be null")
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
