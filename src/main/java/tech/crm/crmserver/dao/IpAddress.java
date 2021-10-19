package tech.crm.crmserver.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *  entity
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("ip_address")
public class IpAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String country;

    @TableField("region_name")
    private String regionName;

    private String city;


}
