package tech.crm.crmserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.enums.ToDoListStatus;
import tech.crm.crmserver.common.validator.NullOrNotBlank;

import javax.validation.constraints.NotBlank;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dateTime;

    @NullOrNotBlank(message = "Description should be null or not blank")
    private String description;

    private ToDoListStatus status;
}