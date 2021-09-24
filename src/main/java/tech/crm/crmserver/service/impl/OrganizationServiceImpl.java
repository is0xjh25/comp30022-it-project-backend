package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.exception.NotEnoughPermissionException;
import tech.crm.crmserver.exception.OrganizationNotExistException;
import tech.crm.crmserver.mapper.OrganizationMapper;
import tech.crm.crmserver.service.BelongToService;
import tech.crm.crmserver.service.DepartmentService;
import tech.crm.crmserver.service.OrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {

    @Autowired
    public OrganizationMapper organizationMapper;

    @Autowired
    public DepartmentService departmentService;

    @Autowired
    public UserService userService;

    @Autowired
    public BelongToService belongToService;

    /**
     * Get all the organization based on userId
     *
     * @param userId the user id to search all organization
     * @return a list of organization, the user own or belong to
     */
    @Override
    public List<Organization> getAllOrgUserOwnAndBelongTo(Integer userId) {
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put("owner", userId);
        // Own
        // List<Organization> orgOwnByUser = organizationMapper.selectByMap(selectMap);
        // Belong to
        List<Organization> orgBelongsToUser = organizationMapper.getOrganizationUserBelongsTo(userId);
        return orgBelongsToUser;
    }

    /**
     * Get all the organization own by user
     *
     * @param userId the user id to search those owning organization
     * @return a list of organization, the user own
     */
    @Override
    public List<Organization> getAllOrgUserOwn(Integer userId) {
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put("owner", userId);
        List<Organization> orgOwnByUser = organizationMapper.selectByMap(selectMap);
        return orgOwnByUser;
    }

    /**
     * Get all the organization by organization name
     *
     * @param orgNameName the name of the organization
     * @return a list of organization match
     */
    @Override
    public List<Organization> getOrgBasedOnName(String orgNameName) {
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", orgNameName);
        List<Organization> organizations = organizationMapper.selectList(queryWrapper);
        return organizations;
    }

    /**
     * Get all the organization by exact organization name
     *
     * @param organizationName the name of the organization
     * @return a list of organization match
     */
    @Override
    public List<Organization> getOrgBasedOnExactName(String organizationName) {
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", organizationName);
        List<Organization> organizations = organizationMapper.selectList(queryWrapper);
        return organizations;
    }

    /**
     * Delete the organization, belongTo, departments, permission in it<br/>
     * will check the permission
     *
     * @param organizationId the organization id of organization need to be deleted
     */
    @Override
    public void deleteOrganization(Integer organizationId) {
        Organization organization = baseMapper.selectById(organizationId);
        if(organization == null){
            throw new OrganizationNotExistException();
        }
        //check permission
        if(!organization.getOwner().equals(userService.getId())){
            throw new NotEnoughPermissionException();
        }
        //delete
        belongToService.deleteBelongToByOrganizationId(organizationId);
        departmentService.deleteDepartmentByOrganizationId(organizationId);
        baseMapper.deleteById(organizationId);
    }
}
