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
 *  DTO for TodoList
 * </p>
 *
 * @author Yongfeng Qin
 * @since 2021-09-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoListCreateDTO {

    @NotNull(message = "Date time could not be null")
    private LocalDateTime dateTime;

    @NotNull(message = "Description could not be null")
    private String description;

    @NotNull(message = "Status could not be null")
    private ToDoListStatus status;
}
