package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dao.RecentContact;

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
     * delete the RecentContact by contactId<br/>
     * will not check the permission<br/>
     * @param contactIds the list of ids of contact need to be deleted
     */
    public void deleteRecentContactByContactIds(List<Integer> contactIds);

}
