package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.common.enums.BelongToStatus;
import tech.crm.crmserver.dao.BelongTo;

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

    /**
     * Insert new belongTo relation, when join an organization <br/>
     * will not check the permission
     *
     * @param organizationId the organizationId to join
     * @param userId the userId join organization
     * @return if the insert success
     */
    public boolean insertNewBelongTo(Integer organizationId, Integer userId);

    /**
     * Search belongTo relation based on some condition
     *
     * @param id the id of the belongTo relationship to match
     * @param userId the userId of the belongTo relationship to match
     * @param organizationId the organizationId of the belongTo relationship to match
     * @param belongToStatus the status of the belongTo relation
     * @return the list of belongTo relation match
     */
    public List<BelongTo> queryBelongToRelation(Integer id, Integer userId, Integer organizationId, BelongToStatus belongToStatus);

    /**
     * Delete the belongTo by organizationId<br/>
     * will not check the permission
     *
     * @param organizationId the organization id of organization need to be deleted
     */
    public void deleteBelongToByOrganizationId(Integer organizationId);

}
