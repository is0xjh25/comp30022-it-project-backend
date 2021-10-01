package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dao.Organization;

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

    /**
     * Get all the organization based on userId
     *
     * @param userId the user id to search all organization
     * @return a list of organization, the user own or belong to
     */
    public List<Organization> getAllOrgUserOwnAndBelongTo(Integer userId);

    /**
     * Get all the organization own by user
     *
     * @param userId the user id to search those owning organization
     * @return a list of organization, the user own
     */
    public List<Organization> getAllOrgUserOwn(Integer userId);

    /**
     * Get all the organization by organization name
     *
     * @param organizationName the name of the organization
     * @return a list of organization match
     */
    public List<Organization> getOrgBasedOnName(String organizationName);

    /**
     * Get all the organization by exact organization name
     *
     * @param organizationName the name of the organization
     * @return a list of organization match
     */
    public List<Organization> getOrgBasedOnExactName(String organizationName);

    /**
     * delete the organization, belongTo, departments, permission in it<br/>
     * will check the permission
     * @param organizationId the organization id of organization need to be deleted
     */
    public void deleteOrganization(Integer organizationId);

    /**
     * transfer the owner of the organization to another user in organization<br/>
     * only the owner of the organization has the permission to do that<br/>
     * will update the permission of departments as well
     * @param organizationId id of the organization
     * @param from previous owner of organization
     * @param to new owner of organization
     */
    public void transferOwnershipOfOrganization(Integer organizationId, Integer from, Integer to);

}
