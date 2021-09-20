package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.dao.Attend;
import tech.crm.crmserver.mapper.AttendMapper;
import tech.crm.crmserver.service.AttendService;

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
public class AttendServiceImpl extends ServiceImpl<AttendMapper, Attend> implements AttendService {

    /**
     * delete the attend by contactId<br/>
     * will not check the permission
     *
     * @param contactIds the list of ids of contact need to be deleted
     */
    @Override
    public void deleteAttendByContactIds(List<Integer> contactIds) {
        if(contactIds.isEmpty()){
            return;
        }
        QueryWrapper<Attend> wrapper = new QueryWrapper<>();
        wrapper.in("contact_id",contactIds);
        baseMapper.delete(wrapper);
    }
}
