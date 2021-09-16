package tech.crm.crmserver.service;

import tech.crm.crmserver.common.enums.Status;
import tech.crm.crmserver.common.utils.NullAwareBeanUtilsBean;
import tech.crm.crmserver.dao.Contact;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.dto.ContactDTO;
import tech.crm.crmserver.dto.UserDTO;
import tech.crm.crmserver.dto.UserPermissionDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.time.LocalDate;
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
    public Page<Contact> getContactByOrgIdAndDepartmentId(Page<Contact> page, Integer organizationId, Integer departmentId, Integer userId);

    public Contact fromContactDTO(ContactDTO contactDTO);
    public List<Contact> getContactBasedOnSomeConditionFromDB(Integer departmentId, String email, String firstName, String middleName,
                                                        String lastName, String phone, String gender, String customerType, String status);
    public boolean updateContact(Contact contact);
}
