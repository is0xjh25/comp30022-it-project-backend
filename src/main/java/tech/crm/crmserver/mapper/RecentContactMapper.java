package tech.crm.crmserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tech.crm.crmserver.dao.RecentContact;
import tech.crm.crmserver.dto.RecentContactDTO;

import java.util.List;

/**
 * <p>
 *  Mapper
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-20
 */
@Repository
public interface RecentContactMapper extends BaseMapper<RecentContact> {


    @Select("SELECT contact.*, recent_contact.last_contact\n" +
            "FROM (SELECT contact_id, last_contact FROM recent_contact WHERE user_id = #{userId} ORDER BY last_contact DESC LIMIT #{limit}) recent_contact\n" +
            "LEFT JOIN contact on recent_contact.contact_id = contact.id")
    public List<RecentContactDTO> getRecentContactDTOByUserIdLimit(@Param("userId") Integer userId,
                                                              @Param("limit") Integer limit);

    @Select("SELECT contact.*, recent_contact.last_contact\n" +
            "FROM (SELECT contact_id, last_contact FROM recent_contact WHERE user_id = #{userId} ORDER BY last_contact DESC) recent_contact\n" +
            "LEFT JOIN contact on recent_contact.contact_id = contact.id")
    public List<RecentContactDTO> getRecentContactDTOByUserId(@Param("userId") Integer userId);
}
