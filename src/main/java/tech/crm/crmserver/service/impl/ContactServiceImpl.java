package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.common.utils.NullAwareBeanUtilsBean;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dao.RecentContact;
import tech.crm.crmserver.dto.ContactDTO;
import tech.crm.crmserver.mapper.ContactMapper;
import tech.crm.crmserver.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

    @Autowired
    public ContactMapper contactMapper;

    @Autowired
    public PermissionService permissionService;

    @Autowired
    public DepartmentService departmentService;

    /**
     * Delete the Contact by departmentId<br/>
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
     * Delete the Contact by departmentId list<br/>
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
     * Delete the related attend and recentContact of contacts
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

    /**
     * Search all contact based on organization id and department id<br/>
     *
     * @param page the configuration of the page
     * @param organizationId the organizationId to search contact
     * @param departmentId the departmentId to search contact
     * @param userId the userId to search contact
     * @return a list of match contact
     */
    @Override
    public Page<Contact> getContactByOrgIdAndDepartmentId(Page<Contact> page, Integer organizationId, Integer departmentId, Integer userId) {

        QueryWrapper<Contact> queryWrapper = new QueryWrapper<>();
        Page<Contact> contacts = new Page<>();
        // User加入的departmentId
        List<Permission> permissionList = permissionService.getPermissionByUserId(userId, PermissionLevel.DISPLAY);
        List<Integer> departmentIdJoinList = new ArrayList<>();
        for (Permission permission : permissionList) {
            departmentIdJoinList.add(permission.getDepartmentId());
        }

        if (departmentId == null) {

            // Org的departmentId
            List<Integer> departmentIdList = departmentService.getDepartmentIdByOrganization(organizationId);

            List<Integer> departmentInOrgAndJoin = new ArrayList<>();
            for (Integer id : departmentIdList) {
                if (departmentIdJoinList.contains(id)) {
                    departmentInOrgAndJoin.add(id);
                }
            }

            for (Integer department : departmentInOrgAndJoin) {
                queryWrapper.or().eq("department_id", department);
            }
            page = contactMapper.selectPage(page, queryWrapper);
        } else {
            if (departmentIdJoinList.contains(departmentId)) {
                queryWrapper.eq("department_id", departmentId);
                page = contactMapper.selectPage(page, queryWrapper);
            }
        }

        return page;
    }

    /**
     * Transfer contactDTO to contact
     *
     * @param contactDTO to contactDTO to transfer
     * @return the contact instance transferred
     */
    @Override
    public Contact fromContactDTO(ContactDTO contactDTO) {
        Contact contact = new Contact();
        NullAwareBeanUtilsBean.copyProperties(contactDTO, contact);
        return contact;
    }

    /**
     * Search contact based on some condition
     *
     * @param departmentId the departmentId to match
     * @param email the email to match
     * @param firstName the first name to match
     * @param middleName the middle name to match
     * @param lastName the last name to match
     * @param phone the phone number to match
     * @param gender the gender to match
     * @param customerType the customerType to match
     * @param status the status to match
     * @return the list of match contact
     */
    @Override
    public List<Contact> getContactBasedOnSomeConditionFromDB(Integer departmentId, String email, String firstName, String middleName, String lastName, String phone, String gender, String customerType, String status) {
        QueryWrapper<Contact> queryWrapper = new QueryWrapper<>();
        if (departmentId != null) {
            queryWrapper.eq("department_id", departmentId);
        }
        if (email != null) {
            queryWrapper.eq("email", email);
        }
        if (firstName != null) {
            queryWrapper.eq("first_name", firstName);
        }
        if (middleName != null) {
            queryWrapper.eq("middle_name", middleName);
        }
        if (lastName != null) {
            queryWrapper.eq("last_name", lastName);
        }
        if (phone != null) {
            queryWrapper.eq("phone", phone);
        }
        if (gender != null) {
            queryWrapper.eq("gender", gender);
        }
        if (customerType != null) {
            queryWrapper.eq("customer_type", customerType);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        List<Contact> contacts = contactMapper.selectList(queryWrapper);
        return contacts;
    }

    /**
     * Update contact
     * @param contact the contact to update
     * @return if the updating success
     */
    @Override
    public boolean updateContact(Contact contact) {
        if (contactMapper.updateById(contact)> 0) {
            return true;
        };
        return false;
    }
}
