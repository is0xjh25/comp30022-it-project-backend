package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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
    private Integer contactId;

    @NotNull(message = "Event id cannot be null!")
    private Integer eventId;
}
