package tech.crm.crmserver.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.enums.ToDoListStatus;

import javax.validation.constraints.NotNull;
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
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime finishTime;

    private String description;

    private ToDoListStatus status;
}
