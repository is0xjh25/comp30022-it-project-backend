package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tech.crm.crmserver.common.validator.NullOrNotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDTO {

    private Integer id;

    private String name;

    private Integer ownerId;

    private boolean isOwner;

}