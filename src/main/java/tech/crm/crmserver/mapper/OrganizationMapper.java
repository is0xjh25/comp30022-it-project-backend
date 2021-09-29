package tech.crm.crmserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tech.crm.crmserver.dao.Organization;

import java.util.List;

/**
 * <p>
 *  Mapper
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@Repository
public interface OrganizationMapper extends BaseMapper<Organization> {

    @Select("SELECT organization.* FROM belong_to INNER JOIN organization ON belong_to.organization_id = organization.id WHERE belong_to.user_id = #{userId}")
    // @Select("SELECT organization.* FROM organization")
    List<Organization> getOrganizationUserBelongsTo(@Param("userId") Integer userId);
}
