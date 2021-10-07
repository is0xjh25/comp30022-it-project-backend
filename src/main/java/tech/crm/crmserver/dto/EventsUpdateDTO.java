package tech.crm.crmserver.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.enums.ToDoListStatus;
import tech.crm.crmserver.common.validator.NullOrNotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * <p>
 *  DTO for update event
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsUpdateDTO {

    @NotNull(message = "Id cannot be null")
    @Positive(message = "id should be positive")
    private Integer id;

    private LocalDateTime startTime;

    private LocalDateTime finishTime;

    @NullOrNotBlank(message = "Description can not be null or blank")
    private String description;

    private ToDoListStatus status;
}
