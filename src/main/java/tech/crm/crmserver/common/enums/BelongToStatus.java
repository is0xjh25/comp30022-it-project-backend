package tech.crm.crmserver.common.enums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Status Enum for Belong To
 * @author Yongfeng
 * @since 2021-09-18
 */
@AllArgsConstructor
@NoArgsConstructor
public enum BelongToStatus {
    DELETE("delete"),
    ACTIVE("active");

    private String name;   // in kilograms
}