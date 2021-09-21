package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.dao.RecentContact;
import tech.crm.crmserver.mapper.RecentContactMapper;
import tech.crm.crmserver.service.RecentContactService;

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
        if(contactIds.isEmpty()){
            return;
        }
        QueryWrapper<RecentContact> wrapper = new QueryWrapper<>();
        wrapper.in("contact_id",contactIds);
        baseMapper.delete(wrapper);
    }
}
