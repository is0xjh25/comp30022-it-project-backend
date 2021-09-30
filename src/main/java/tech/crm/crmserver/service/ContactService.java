package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dto.ContactCreateDTO;
import tech.crm.crmserver.dto.ContactDTO;
import tech.crm.crmserver.dto.ContactUpdateDTO;

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
     * Delete the Contact by departmentId<br/>
     * will delete the related attend and recentContact as well<br/>
     * will not check the permission<br/>
     *
     * @param departmentId the id of contact need to be deleted
     */
    public void deleteContactByDepartmentId(Integer departmentId);

    /**
     * Delete the Contact by departmentId list<br/>
     * will delete the related attend and recentContact as well<br/>
     * will not check the permission<br/>
     *
     * @param departmentIds the list of ids of contact need to be deleted
     */
    public void deleteContactByDepartmentIdList(List<Integer> departmentIds);

    /**
     * Search all contact based on organization id and department id<br/>
     *
     * @param page the configuration of the page
     * @param organizationId the organizationId to search contact
     * @param departmentId the departmentId to search contact
     * @param userId the userId to search contact
     * @return a list of match contact
     */
    public Page<ContactDTO> getContactByOrgIdAndDepartmentId(Page<ContactDTO> page, Integer organizationId, Integer departmentId, Integer userId);

    /**
     * Transfer ContactCreateDTO to contact
     *
     * @param contactCreateDTO to ContactCreateDTO to transfer
     * @return the contact instance transferred
     */
    public Contact fromContactCreateDTO(ContactCreateDTO contactCreateDTO);

    /**
     * Transfer ContactUpdateDTO to contact
     *
     * @param contactUpdateDTO to ContactUpdateDTO to transfer
     * @return the contact instance transferred
     */
    public Contact fromContactUpdateDTO(ContactUpdateDTO contactUpdateDTO);

    /**
     * Transfer Contact to ContactDTO
     *
     * @param contact to Contact to transfer
     * @return the contact instance transferred
     */
    public ContactDTO ContactToContactDTO(Contact contact);

    /**
     * Transfer List of Contact to List of ContactDTO
     *
     * @param contacts a list of contacts
     * @return a list of ContactDTO instances transferred
     */
    public List<ContactDTO> ContactToContactDTO(List<Contact> contacts);

    /**
     * search contacts in department by contact key
     * @param page the configuration of the page
     * @param departmentId the departmentId to search contact
     * @param searchKey search key
     * @return certain page of ContactDTO
     */
    public Page<ContactDTO> searchContactDTO(Page<ContactDTO> page, Integer departmentId, String searchKey);

    /**
     * search contacts of user by contact key
     * @param userId the id of user
     * @param searchKey search key
     * @return all contacts of this user satisfy the searchKey
     */
    public List<Contact> searchAllContactOfUser(Integer userId, String searchKey);

    /**
     * Create contact based on contactDTO
     *
     * @param contactDTO all the information need to create contact
     * @return if create success
     */
    public boolean createContactByContactDTO(ContactCreateDTO contactDTO, Integer userId);

    /**
     * Update contact based on contactUpdateDTO
     *
     * @param contactDTO all the information need to update contact
     * @return if update success
     */
    public boolean updateContactByContactDTO(ContactUpdateDTO contactDTO, Integer userId);

    /**
     * Delete contact based on contactId
     *
     * @param contactId the id of the contact to delete
     * @return if delete success
     */
    public boolean deleteContactByContactId(Integer contactId, Integer userId);

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
    public List<Contact> getContactBasedOnSomeConditionFromDB(Integer departmentId, String email, String firstName,
                                                              String middleName,String lastName, String phone,
                                                              String gender, String customerType, String status);

    /**
    * Update contact
    * @param contact the contact to update
    * @return if the updating success
    */
    public boolean updateContact(Contact contact);
}
