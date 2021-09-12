package tech.crm.crmserver.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tech.crm.crmserver.dao.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {


    @Select("SELECT * FROM permission WHERE department_id = #{departmentId} ORDER BY FIELD(authority_level,0,5,4,3,2,1)")
    public Page<Permission> getPermissionInDepartmentOrdered(Page<?> page,@Param("departmentId") Integer departmentId);

}
