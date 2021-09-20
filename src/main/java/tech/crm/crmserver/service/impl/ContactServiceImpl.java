package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dao.RecentContact;
import tech.crm.crmserver.mapper.ContactMapper;
import tech.crm.crmserver.service.AttendService;
import tech.crm.crmserver.service.ContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.service.RecentContactService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact> implements ContactService {

    @Autowired
    private AttendService attendService;

    @Autowired
    private RecentContactService recentContactService;

    /**
     * delete the Contact by departmentId<br/>
     * will delete the related attend and recentContact as well<br/>
     * will not check the permission<br/>
     *
     * @param departmentId the id of contact need to be deleted
     */
    @Override
    public void deleteContactByDepartmentId(Integer departmentId) {
        QueryWrapper<Contact> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id",departmentId);
        List<Contact> contacts = baseMapper.selectList(wrapper);
        deleteRelatedByContactList(contacts);
        baseMapper.delete(wrapper);
    }


    /**
     * delete the Contact by departmentId list<br/>
     * will delete the related attend and recentContact as well<br/>
     * will not check the permission<br/>
     *
     * @param departmentIds the list of ids of contact need to be deleted
     */
    @Override
    public void deleteContactByDepartmentIdList(List<Integer> departmentIds) {
        if(departmentIds.isEmpty()){
            return;
        }
        QueryWrapper<Contact> wrapper = new QueryWrapper<>();
        wrapper.in("department_id",departmentIds);
        List<Contact> contacts = baseMapper.selectList(wrapper);
        deleteRelatedByContactList(contacts);
        baseMapper.delete(wrapper);
    }

    /**
     * delete the related attend and recentContact of contacts
     * @param contacts contacts
     */
    private void deleteRelatedByContactList(List<Contact> contacts){
        if(contacts.isEmpty()){
            return;
        }
        List<Integer> contactIdList = new ArrayList<>();
        for(Contact c : contacts){
            contactIdList.add(c.getId());
        }
        attendService.deleteAttendByContactIds(contactIdList);
        recentContactService.deleteRecentContactByContactIds(contactIdList);
    }
}
