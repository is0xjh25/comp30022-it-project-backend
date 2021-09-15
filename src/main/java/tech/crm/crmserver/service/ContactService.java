package tech.crm.crmserver.service;

import tech.crm.crmserver.dao.Contact;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
public interface ContactService extends IService<Contact> {
    public List<Contact> getContactByOrgIdAndDepartmentId(Integer organizationId, Integer departmentId, Integer userId);
}
