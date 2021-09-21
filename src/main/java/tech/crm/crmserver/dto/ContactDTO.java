package tech.crm.crmserver.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tech.crm.crmserver.common.enums.Status;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {

    private Integer id;

    @NonNull
    private Integer departmentId;

    @NonNull
    private String email;

    @NonNull
    private String firstName;

    private String middleName;

    @NonNull
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
