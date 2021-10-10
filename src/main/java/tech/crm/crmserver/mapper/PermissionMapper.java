package tech.crm.crmserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dto.UserPermissionDTO;

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


    @Select("SELECT p.user_id, p.department_id, p.id permission_id, p.authority_level, u.email, u.first_name, u.middle_name, u.last_name, u.recent_activity, u.photo " +
            "FROM permission  p LEFT JOIN user u ON p.user_id = u.id " +
            "WHERE department_id = #{departmentId} " +
            "ORDER BY FIELD(authority_level,0,5,4,3,2,1)")
    public Page<UserPermissionDTO> getPermissionInDepartmentOrdered(Page<?> page, @Param("departmentId") Integer departmentId);

    @Select("SELECT *\n" +
            "FROM `permission`\n" +
            "WHERE `user_id` = #{userId} and `department_id` = (SELECT `department_id` FROM `contact` where id = #{contactId})")
    public Permission getPermissionByUserIdAndContactId(@Param("userId") Integer userId,
                                                        @Param("contactId") Integer contactId);

}
