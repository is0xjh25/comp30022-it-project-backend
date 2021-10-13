package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.enums.Status;

import java.time.LocalDate;

/**
 * <p>
 *  DTO for Contact with attend id
 * </p>
 *
 * @author Yongfeng Qing
 * @since 2021-10-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactAttendDTO {

    private Integer id;

    private Integer departmentId;

    private String email;

    private String firstName;

    private String middleName;

    private String lastName;

    private String phone;

    private String description;

    private String gender;

    private LocalDate birthday;

    private String address;

    private String organization;

    private String customerType;

    private Status status;

    private Integer attendId;

    private String photo;
}
