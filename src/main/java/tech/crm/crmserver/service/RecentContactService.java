package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dao.RecentContact;
import tech.crm.crmserver.dto.RecentContactDTO;

import java.util.List;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-20
 */
public interface RecentContactService extends IService<RecentContact> {

    /**
     * Delete the RecentContact by contactId<br/>
     * will not check the permission<br/>
     *
     * @param contactIds the list of ids of contact need to be deleted
     */
    public void deleteRecentContactByContactIds(List<Integer> contactIds);

    /**
     * save or update recent contact to database<br/>
     * will not check permission
     * @param userId the id of user
     * @param contactId the id of contact
     */
    public void saveOrUpdateRecentContactByUserId(Integer userId, Integer contactId);

    /**
     * get user's recent contact
     * @param userId the id of user
     * @param limit the number of recent contact needed
     * @return List<RecentContactDTO> return a list of recent contacts with last contact time
     */
    public List<RecentContactDTO> getContactByUserId(Integer userId, Integer limit);

}
