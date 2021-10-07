package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDTO {

    private Integer id;

    private String name;

    private Integer ownerId;

    private boolean isOwner;

}