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

    /**
     * delete the organization, belongTo, departments, permission in it<br/>
     * will check the permission
     * @param organizationId the organization id of organization need to be deleted
     */
    public void deleteOrganization(Integer organizationId);

}
