package tech.crm.crmserver.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *  entity
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("recent_contact")
public class RecentContact implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer contactId;

    private Integer userId;

    private LocalDateTime lastContact;


}
