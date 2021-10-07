package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.validator.NullOrNotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * <p>
 *  DTO for add contact or delete contact in event
 * </p>
 *
 * @author Lingxiao Li
 * @since 2021-09-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventContactDTO {

    @NotNull(message = "Contact id cannot be null!")
    @Positive(message = "Contact id should be positive")
    private Integer contactId;

    @NotNull(message = "Event id cannot be null!")
    @Positive(message = "Event id should be positive")
    private Integer eventId;
}
