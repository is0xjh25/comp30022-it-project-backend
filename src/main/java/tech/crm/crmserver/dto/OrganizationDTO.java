package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *  DTO for get Organization
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDTO {

    private Integer id;

    private String name;

    private Integer ownerId;

    private boolean isOwner;

}