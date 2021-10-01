package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *  DTO for statistic information of event or to do
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatDTO {

    private Integer toDo;
    private Integer inProgress;
    private Integer done;
}
