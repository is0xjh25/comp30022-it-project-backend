package tech.crm.crmserver.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.enums.Status;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {
    
    private Integer id;

    @NotNull(message = "department_id cannot be null")
    @JsonProperty("department_id")
    private Integer departmentId;

    @NotNull(message = "email cannot be null")
    @Email(message = "invalid email for contact")
    private String email;

    @NotNull(message = "first_name cannot be null")
    private String firstName;

    private String middleName;

    @NotNull(message = "last_name cannot be null")
    private String lastName;

    private String phone;

    private String description;

    private String gender;

    private LocalDate birthday;

    private String address;

    private String organization;

    private String customerType;

    private Status status;
}
