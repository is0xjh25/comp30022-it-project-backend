package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dao.Attend;

import java.util.List;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-20
 */
public interface AttendService extends IService<Attend> {

    /**
     * delete the attend by contactId<br/>
     * will not check the permission
     * @param contactIds the list of ids of contact need to be deleted
     */
    public void deleteAttendByContactIds(List<Integer> contactIds);

    /**
     * delete the attend by eventId
     *
     * @param eventId the evenId to match those attend which to delete
     */
    public void deleteAttendByEventId(Integer eventId);

    /**
     * Get attend data by eventId
     *
     * @param eventId the evenId to match those attend which to get
     */
    public List<Attend> getAttendByEventId(Integer eventId);

}
