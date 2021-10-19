package tech.crm.crmserver.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.crm.crmserver.common.enums.EventStatus;
import tech.crm.crmserver.common.enums.ToDoListStatus;

import java.time.LocalDateTime;
import java.util.List;

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
public class EventAttendDTO {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private LocalDateTime startTime;

    private LocalDateTime finishTime;

    private String description;

    private EventStatus status;

    private List<ContactAttendDTO> contactList;
}
