package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

}
