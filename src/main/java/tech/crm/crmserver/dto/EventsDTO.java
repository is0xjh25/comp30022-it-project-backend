package tech.crm.crmserver.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime startTime;

    @NotNull(message = "Finish time of the event can not be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime finishTime;

    @NotNull(message = "The description of the event can not be null")
    private String description;
}
