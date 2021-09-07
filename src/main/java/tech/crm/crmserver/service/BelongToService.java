package tech.crm.crmserver.service;

import tech.crm.crmserver.dao.BelongTo;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dao.Organization;

import java.util.List;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-01
 */
public interface BelongToService extends IService<BelongTo> {

    public boolean insertNewBelongTo(Integer organizationId, Integer userId);

}
