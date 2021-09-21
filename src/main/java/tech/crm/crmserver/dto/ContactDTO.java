package tech.crm.crmserver.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tech.crm.crmserver.common.enums.Status;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {

    private Integer id;

    @NotNull
    @JsonProperty("department_id")
    private Integer departmentId;

    @NotNull
    private String email;

    @NotNull
    private String firstName;

    private String middleName;

    @NotNull
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
