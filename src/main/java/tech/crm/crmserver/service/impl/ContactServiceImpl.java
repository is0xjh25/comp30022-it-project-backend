package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.common.constants.TimeZoneConstants;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.common.enums.Status;
import tech.crm.crmserver.common.utils.NullAwareBeanUtilsBean;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dto.ContactCreateDTO;
import tech.crm.crmserver.dto.ContactDTO;
import tech.crm.crmserver.dto.ContactUpdateDTO;
import tech.crm.crmserver.exception.*;
import tech.crm.crmserver.mapper.ContactMapper;
import tech.crm.crmserver.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    public ContactService contactService;

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
    public Page<ContactDTO> getContactByOrgIdAndDepartmentId(Page<ContactDTO> page, Integer organizationId, Integer departmentId, Integer userId) {

        QueryWrapper<Contact> queryWrapper = new QueryWrapper<>();
        Page<Contact> contacts = new Page<>();
        // User's joined department
        List<Permission> permissionList = permissionService.getPermissionByUserId(userId, PermissionLevel.DISPLAY);
        List<Integer> departmentIdJoinList = new ArrayList<>();
        for (Permission permission : permissionList) {
            departmentIdJoinList.add(permission.getDepartmentId());
        }

        if (departmentId == null) {

            // Org's department
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
            page = contactMapper.getContactDTOByDepartmentId(page, queryWrapper);
        } else {
            if (departmentIdJoinList.contains(departmentId)) {
                queryWrapper.eq("department_id", departmentId);
                page = contactMapper.getContactDTOByDepartmentId(page, queryWrapper);
            }
        }

        return page;
    }

    /**
     * Transfer ContactCreateDTO to contact
     *
     * @param contactCreateDTO to ContactCreateDTO to transfer
     * @return the contact instance transferred
     */
    @Override
    public Contact fromContactCreateDTO(ContactCreateDTO contactCreateDTO) {
        Contact contact = new Contact();
        NullAwareBeanUtilsBean.copyProperties(contactCreateDTO, contact);
        return contact;
    }

    /**
     * Transfer ContactUpdateDTO to contact
     *
     * @param contactUpdateDTO to ContactUpdateDTO to transfer
     * @return the contact instance transferred
     */
    @Override
    public Contact fromContactUpdateDTO(ContactUpdateDTO contactUpdateDTO) {
        Contact contact = new Contact();
        NullAwareBeanUtilsBean.copyProperties(contactUpdateDTO, contact);
        return contact;
    }

    /**
     * Transfer Contact to ContactDTO
     *
     * @param contact to Contact to transfer
     * @return the contact instance transferred
     */
    @Override
    public ContactDTO ContactToContactDTO(Contact contact) {
        ContactDTO contactDTO = new ContactDTO();
        NullAwareBeanUtilsBean.copyProperties(contact, contactDTO);
        LocalDate birthday = contactDTO.getBirthday();
        if(birthday != null){
            contactDTO.setAge(birthday.until(LocalDate.now(TimeZoneConstants.ZONE)).getYears());
        }
        return contactDTO;
    }

    /**
     * Transfer List of Contact to List of ContactDTO
     *
     * @param contacts a list of contacts
     * @return a list of ContactDTO instances transferred
     */
    @Override
    public List<ContactDTO> ContactToContactDTO(List<Contact> contacts) {
        List<ContactDTO> contactDTOList = new ArrayList<>();
        for (Contact contact: contacts){
            contactDTOList.add(ContactToContactDTO(contact));
        }
        return contactDTOList;
    }

    /**
     * put search key into map
     * @param searchKey search key
     * @return search map
     */
    private Map<String,String> searchMap(String searchKey){
        if(searchKey == null){
            return null;
        }
        Map<String,String> map = new HashMap<>();
        map.put("email",searchKey);
        map.put("first_name",searchKey);
        map.put("last_name",searchKey);
        map.put("gender",searchKey);
        map.put("organization",searchKey);
        map.put("description",searchKey);
        return map;
    }

    /**
     * search contacts in department by contact key
     * will not check the permission
     * @param page         the configuration of the page
     * @param departmentId the departmentId to search contact
     * @param searchKey    search key
     * @return
     */
    @Override
    public Page<ContactDTO> searchContactDTO(Page<ContactDTO> page, Integer departmentId, String searchKey) {
        Map<String, String> map = searchMap(searchKey);
        //to the true wrapper
        QueryWrapper<Contact> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id",departmentId).and(i -> {
            //add conditions into a wrapper
            for(Map.Entry<String,String> entry : map.entrySet()){
                i.or().like(entry.getKey(),entry.getValue());
            }
        });
        page = baseMapper.getContactDTOByDepartmentId(page,wrapper);
        return page;
    }

    /**
     * search contacts of user by contact key
     *
     * @param userId    the id of user
     * @param searchKey search key
     * @return all contacts of this user satisfy the searchKey
     */
    @Override
    public List<Contact> searchAllContactOfUser(Integer userId, String searchKey) {
        //get departments
        List<Permission> permissionList = permissionService.getPermissionByUserId(userId, PermissionLevel.DISPLAY);
        if (permissionList.isEmpty()) {
            throw new UserNotInDepartmentException();
        }
        List<Integer> departmentIdList = new ArrayList<>();
        for (Permission p: permissionList){
            departmentIdList.add(p.getDepartmentId());
        }
        Map<String, String> map = searchMap(searchKey);
        //to the true wrapper
        QueryWrapper<Contact> wrapper = new QueryWrapper<>();
        wrapper.in("department_id",departmentIdList).and(i -> {
            //add conditions into a wrapper
            for(Map.Entry<String,String> entry : map.entrySet()){
                i.or().like(entry.getKey(),entry.getValue());
            }
        });
        List<Contact> contactList = baseMapper.selectList(wrapper);
        return contactList;
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
    public List<Contact> getContactBasedOnSomeConditionFromDB(Integer departmentId, String email, String firstName,
                                                              String middleName, String lastName, String phone,
                                                              String gender, String customerType, String status) {
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

    /**
     * Create contact based on contactDTO
     *
     * @param contactDTO all the information need to create contact
     * @return if create success
     */
    @Override
    public boolean createContactByContactDTO(ContactCreateDTO contactDTO, Integer userId) {
        // Verify departmentID is valid
        if (!departmentService.checkIfValidDepartmentId(contactDTO.getDepartmentId())) {
            throw new DepartmentNotExistException();
        }

        if (!permissionService.ifPermissionLevelSatisfied(userId, PermissionLevel.UPDATE, contactDTO.getDepartmentId())) {
            throw new NotEnoughPermissionException();
        }

        // check if the same contact already exists
        List<Contact> contacts = contactService.getContactBasedOnSomeConditionFromDB(contactDTO.getDepartmentId(),contactDTO.getEmail(), null, null, null, null, null, null, null);
        if (contacts.size() > 0) {
            throw new ContactAlreadyExistException();
        }

        Contact newContact = contactService.fromContactCreateDTO(contactDTO);
        return save(newContact);
    }

    /**
     * Update contact based on contactUpdateDTO
     *
     * @param contactDTO all the information need to update contact
     * @return if update success
     */
    public boolean updateContactByContactDTO(ContactUpdateDTO contactDTO, Integer userId) {
        Contact newContact = contactService.fromContactUpdateDTO(contactDTO);

        Contact oldContact = contactService.getById(newContact.getId());
        if(oldContact == null || oldContact.getStatus().equals(Status.DELETED)){
            throw new ContactNotExistException();
        }

        // Verify departmentID is valid
        if (!departmentService.checkIfValidDepartmentId(oldContact.getDepartmentId())) {
            throw new DepartmentNotExistException();
        }

        // Verify permissionLevel
        if (!permissionService.ifPermissionLevelSatisfied(userId, PermissionLevel.UPDATE, oldContact.getDepartmentId())) {
            throw new NotEnoughPermissionException();
        }
       return contactService.updateContact(newContact);
    }

    /**
     * Delete contact based on contactId
     *
     * @param contactId the id of the contact to delete
     * @return if delete success
     */
    public boolean deleteContactByContactId(Integer contactId, Integer userId) {
        Contact contact = contactService.getById(contactId);

        if (contact == null || contact.getStatus().equals(Status.DELETED)) {
            throw new ContactNotExistException();
        }
        // Verify permissionLevel, the permissionLevel here is delete
        if (!permissionService.ifPermissionLevelSatisfied(userId, PermissionLevel.DELETE, contact.getDepartmentId())) {
            throw new NotEnoughPermissionException();
        }
        //delete related recent_content & attend
        ArrayList<Contact> contactArrayList = new ArrayList<>();
        contactArrayList.add(contact);
        deleteRelatedByContactList(contactArrayList);

        return removeById(contactId);

    }
}
