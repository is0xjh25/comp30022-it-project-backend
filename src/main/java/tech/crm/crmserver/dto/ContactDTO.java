package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * <p>
 *  DTO for contact
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {

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

    private Integer age;

    private String address;

    private String organization;

    private String customerType;

}
