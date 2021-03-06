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
 *  DTO for update todoList form
 * </p>
 *
 * @author Kaiyuan Zheng
 * @since 2021-09-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoListUpdateDTO {

    @NotNull(message="The target to-do list id is empty.")
    @Positive(message = "id should be positive")
    private Integer id;

    private LocalDateTime dateTime;

    @NullOrNotBlank(message = "Description can not be null or blank")
    private String description;

    private ToDoListStatus status;
}