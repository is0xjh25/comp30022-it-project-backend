package tech.crm.crmserver.service;

import tech.crm.crmserver.dao.Organization;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
public interface OrganizationService extends IService<Organization> {

    // get all the organization the user own and belong to
    public List<Organization> getAllOrgUserOwnAndBelongTo(Integer userId);

    public List<Organization> getAllOrgUserOwn(Integer userId);

    public List<Organization> getOrgBasedOnName(String organizationName);

    public List<Organization> getOrgBasedOnExactName(String organizationName);

}
