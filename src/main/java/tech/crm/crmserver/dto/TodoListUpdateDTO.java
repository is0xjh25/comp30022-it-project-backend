package tech.crm.crmserver.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import tech.crm.crmserver.common.enums.Status;
import tech.crm.crmserver.common.enums.ToDoListStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime finishTime;

    private String description;

    private ToDoListStatus status;
}