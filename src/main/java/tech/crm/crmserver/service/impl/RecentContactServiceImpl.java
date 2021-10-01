package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.common.constants.TimeZoneConstants;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dao.RecentContact;
import tech.crm.crmserver.dto.RecentContactDTO;
import tech.crm.crmserver.mapper.RecentContactMapper;
import tech.crm.crmserver.service.RecentContactService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-20
 */
@Service
public class RecentContactServiceImpl extends ServiceImpl<RecentContactMapper, RecentContact> implements RecentContactService {

    /**
     * Delete the RecentContact by contactId<br/>
     * will not check the permission
     *
     * @param contactIds the id of contact need to be deleted
     */
    @Override
    public void deleteRecentContactByContactIds(List<Integer> contactIds) {
        //sanity check
        if(contactIds.isEmpty()){
            return;
        }
        QueryWrapper<RecentContact> wrapper = new QueryWrapper<>();
        wrapper.in("contact_id",contactIds);
        baseMapper.delete(wrapper);
    }

    /**
     * save or update recent contact to database<br/>
     * will not check permission
     *
     * @param userId    the id of user
     * @param contactId the id of contact
     */
    @Override
    public void saveOrUpdateRecentContactByUserId(Integer userId, Integer contactId) {
        //sanity check
        if(userId == null || contactId == null) {
            log.warn("Fail to update recent contact! Have null input!");
            return;
        }
        //search recent contact
        UpdateWrapper<RecentContact> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id",userId)
                .eq("contact_id",contactId);
        RecentContact recentContact = baseMapper.selectOne(wrapper);

        //try to insert
        if(recentContact == null){
            recentContact = new RecentContact(contactId, userId, LocalDateTime.now(TimeZoneConstants.ZONE));
            save(recentContact);
        }
        //try to update
        else {
            wrapper.set("last_contact",LocalDateTime.now(TimeZoneConstants.ZONE));
            update(wrapper);
        }
    }

    /**
     * get user's recent contact
     *
     * @param userId the id of user
     * @param limit  the number of recent contact needed
     * @return List<RecentContactDTO> return a list of recent contacts with last contact time
     */
    @Override
    public List<RecentContactDTO> getContactByUserId(Integer userId, Integer limit) {
        //sanity check
        if(userId == null){
            log.warn("Fail to get recent contact! Have null input!");
            return null;
        }

        if(limit != null){
            return baseMapper.getRecentContactDTOByUserIdLimit(userId, limit);
        }
        else {
            return baseMapper.getRecentContactDTOByUserId(userId);
        }


    }
}
