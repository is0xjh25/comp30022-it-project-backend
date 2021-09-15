package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.crm.crmserver.common.enums.BelongToStatus;
import tech.crm.crmserver.dao.BelongTo;
import tech.crm.crmserver.mapper.BelongToMapper;
import tech.crm.crmserver.service.BelongToService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-01
 */
@Service
public class BelongToServiceImpl extends ServiceImpl<BelongToMapper, BelongTo> implements BelongToService {

    /**
     * Insert new BelongTo relationship,
     * UserId, OrganizationId
     */

    @Autowired
    private BelongToMapper belongToMapper;

    @Autowired
    private BelongToService belongToService;

    @Override
    public boolean insertNewBelongTo(Integer organizationId, Integer userId) {
        // First, make sure this user and this organization do not have recorded belong to relationship
        // Then insert this belongTo relationship
        List<BelongTo> belongToList = queryBelongToRelation(null, organizationId, userId, BelongToStatus.ACTIVE);
        if (belongToList.size() == 0) {
            BelongTo belongTo = new BelongTo();
            belongTo.setOrganizationId(organizationId);
            belongTo.setUserId(userId);
            belongToService.save(belongTo);
        }
        return true;
    }

    @Override
    public List<BelongTo> queryBelongToRelation(Integer id, Integer userId, Integer organizationId, BelongToStatus belongToStatus) {
        QueryWrapper<BelongTo> queryWrapper = new QueryWrapper<>();
        if (id != null) {
            queryWrapper.eq("id", id);
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (organizationId != null) {
            queryWrapper.eq("organization_id", organizationId);
        }
        if (belongToStatus != null) {
            queryWrapper.eq("status", belongToStatus);
        }
        return belongToMapper.selectList(queryWrapper);
    }
}
