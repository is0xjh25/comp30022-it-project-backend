package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 *  DTO for User in Organization
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBelongToDTO {

    private Integer userId;
    private Integer organizationId;
    private Integer belongToId;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDateTime recentActivity;

}
