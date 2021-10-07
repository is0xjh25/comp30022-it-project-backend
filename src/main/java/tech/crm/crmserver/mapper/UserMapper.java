package tech.crm.crmserver.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tech.crm.crmserver.dao.User;
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
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT p.user_id, p.department_id, p.id permission_id, p.authority_level, u.email, u.first_name, u.middle_name, u.last_name, u.recent_activity " +
            "FROM (permission p LEFT JOIN user u ON p.user_id = u.id) " +
            "${ew.customSqlSegment}")
    public Page<UserPermissionDTO> getUserPermissionDTO(Page<?> page, @Param(Constants.WRAPPER) Wrapper<User> queryWrapper);

    @Select("SELECT b.user_id, u.email, u.first_name, u.middle_name, u.last_name, u.recent_activity " +
            "FROM (belong_to b LEFT JOIN user u ON b.user_id = u.id) ${ew.customSqlSegment}")
    public Page<UserPermissionDTO> getUserPermissionDTOInOrganization(Page<?> page, @Param(Constants.WRAPPER) Wrapper<User> queryWrapper);

}
