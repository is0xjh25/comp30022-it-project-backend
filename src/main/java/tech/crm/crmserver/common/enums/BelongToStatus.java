package tech.crm.crmserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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