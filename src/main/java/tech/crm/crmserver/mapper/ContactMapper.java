package tech.crm.crmserver.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dto.ContactDTO;

/**
 * <p>
 *  Mapper
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@Repository
public interface ContactMapper extends BaseMapper<Contact> {

    @Select("SELECT contact.*,TIMESTAMPDIFF(YEAR,contact.birthday,CURDATE()) AS age\n" +
            "FROM contact ${ew.customSqlSegment}")
    public Page<ContactDTO> getContactDTOByDepartmentId(Page<?> page, @Param(Constants.WRAPPER) Wrapper<Contact> queryWrapper);

}
