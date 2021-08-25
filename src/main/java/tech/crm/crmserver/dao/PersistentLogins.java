package tech.crm.crmserver.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 *  entity
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("persistent_logins")
public class PersistentLogins implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "series", type = IdType.AUTO)
    private String series;

    private String username;

    private String token;

    private LocalDateTime lastUsed;


}
