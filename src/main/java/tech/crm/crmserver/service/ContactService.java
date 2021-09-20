package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import tech.crm.crmserver.dao.Contact;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dto.ContactDTO;

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

    /**
     * delete the Contact by departmentId<br/>
     * will delete the related attend and recentContact as well<br/>
     * will not check the permission
     *
     * @param departmentId the id of contact need to be deleted
     */
    public void deleteContactByDepartmentId(Integer departmentId);

    /**
     * delete the Contact by departmentId list<br/>
     * will delete the related attend and recentContact as well<br/>
     * will not check the permission
     *
     * @param departmentIds the list of ids of contact need to be deleted
     */
    public void deleteContactByDepartmentIdList(List<Integer> departmentIds);

    public Page<Contact> getContactByOrgIdAndDepartmentId(Page<Contact> page, Integer organizationId, Integer departmentId, Integer userId);

    public Contact fromContactDTO(ContactDTO contactDTO);

    public List<Contact> getContactBasedOnSomeConditionFromDB(Integer departmentId, String email, String firstName, String middleName,
                                                              String lastName, String phone, String gender, String customerType, String status);
    public boolean updateContact(Contact contact);

}
