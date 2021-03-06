package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.enums.ToDoListStatus;

import javax.validation.constraints.NotBlank;
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

    @NotNull(message = "Start time could not be null")
    private LocalDateTime dateTime;

    @NotBlank(message = "Description could not be null or empty")
    private String description;

    @NotNull(message = "Status could not be null")
    private ToDoListStatus status;
}
