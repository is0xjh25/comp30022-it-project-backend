package tech.crm.crmserver.service.impl;

import tech.crm.crmserver.dao.BelongTo;
import tech.crm.crmserver.mapper.BelongToMapper;
import tech.crm.crmserver.service.BelongToService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
    @Override
    public boolean insertNewBelongTo(Integer organizationId, Integer userId) {
        // First, make sure this user and this organization do not have recorded belong to relationship
        // Then insert this belongTo relationship

        return false;
    }
}
