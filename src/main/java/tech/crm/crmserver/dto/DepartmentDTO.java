package tech.crm.crmserver.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *  department DTO
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DepartmentDTO implements Serializable {


    public enum Status {
        OWNER("owner"),
        MEMBER("member"),
        NOT_JOIN("notJoin");

        Status(String name) {
            this.name = name;
        }

        private String name;

        public String getName() {
            return this.name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer organizationId;

    private String status;

}
