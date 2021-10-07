package tech.crm.crmserver.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 *  DTO for Event
 * </p>
 *
 * @author Yongfeng Qin
 * @since 2021-09-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsDTO {

    @NotNull(message = "Start time of the event can not be null")
    private LocalDateTime startTime;

    @NotNull(message = "Finish time of the event can not be null")
    private LocalDateTime finishTime;

    @NotBlank(message = "The description of the event can not be null")
    private String description;
}
