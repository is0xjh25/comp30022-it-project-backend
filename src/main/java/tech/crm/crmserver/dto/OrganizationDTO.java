package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDTO {

    @NonNull
    private Integer id;

    @NonNull
    private String name;

    @NonNull
    private Integer owner;

    private String ownerName;

}