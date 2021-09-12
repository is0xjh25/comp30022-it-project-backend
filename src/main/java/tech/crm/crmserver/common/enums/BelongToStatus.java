package tech.crm.crmserver.common.enums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
public enum BelongToStatus {
    DELETE("delete"),
    ACTIVE("active");

    private String name;   // in kilograms
}