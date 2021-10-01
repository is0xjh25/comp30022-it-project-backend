package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 *  the get DTO for recent contact<br/>
 *  include all details about contact and last contact time
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentContactDTO {

    //id of contact
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

    private LocalDateTime lastContact;

}
