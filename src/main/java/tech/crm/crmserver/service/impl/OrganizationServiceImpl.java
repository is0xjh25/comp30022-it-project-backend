package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.common.exception.NotEnoughPermissionException;
import tech.crm.crmserver.common.exception.OrganizationNotExistException;
import tech.crm.crmserver.common.exception.UserAlreadyOwnOrganizationException;
import tech.crm.crmserver.common.exception.UserNotExistException;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.dto.UserPermissionDTO;
import tech.crm.crmserver.mapper.OrganizationMapper;
import tech.crm.crmserver.service.*;

import java.util.ArrayList;
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

    @Autowired
    public PermissionService permissionService;

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

    /**
     * transfer the owner of the organization to another user in organization<br/>
     * only the owner of the organization has the permission to do that<br/>
     * will update the permission of departments as well
     *
     * @param organizationId id of the organization
     * @param from           previous owner of organization
     * @param to             new owner of organization
     */
    @Override
    public void transferOwnershipOfOrganization(Integer organizationId, Integer from, Integer to) {
        Organization organization = baseMapper.selectById(organizationId);
        if(organization == null){
            throw new OrganizationNotExistException();
        }
        //not the owner of organization
        if(!organization.getOwner().equals(from)){
            throw new NotEnoughPermissionException();
        }
        //check transfer to same owner
        if(from.equals(to)){
            throw new UserAlreadyOwnOrganizationException();
        }
        //check whether the new owner exist
        if(userService.getById(to) == null){
            throw new UserNotExistException();
        }
        //add belongTo relationship for new owner
        belongToService.insertNewBelongTo(organizationId,to);

        //transfer the ownership
        organization.setOwner(to);
        baseMapper.updateById(organization);

        //update permissions of previous owner
        List<Integer> departmentIdList = departmentService.getDepartmentIdByOrganization(organizationId);

        //delete permission first
        QueryWrapper<Permission> permissionDeleteWrapper = new QueryWrapper<>();
        permissionDeleteWrapper.in("user_id",from,to)
                .in("department_id",departmentIdList);
        permissionService.remove(permissionDeleteWrapper);

        //add permission
        List<Permission> permissionList = new ArrayList<>();
        for (Integer departmentId: departmentIdList) {
            //old owner
            Permission permission = new Permission();
            permission.setUserId(from);
            permission.setDepartmentId(departmentId);
            permission.setAuthorityLevel(PermissionLevel.MANAGE);
            permissionList.add(permission);
            //new owner
            permission = new Permission();
            permission.setUserId(to);
            permission.setDepartmentId(departmentId);
            permission.setAuthorityLevel(PermissionLevel.OWNER);
            permissionList.add(permission);
        }
        permissionService.saveBatch(permissionList);
    }

    /**
     * search member in organization by search key<br/>
     * will not check permission
     * @param page         the configuration of the page
     * @param organizationId the organization id of member
     * @param searchKey    search key
     * @return certain page of ContactDTO
     */
    @Override
    public Page<UserPermissionDTO> searchMember(Page<UserPermissionDTO> page, Integer organizationId, String searchKey) {
        Map<String, String> map = UserService.searchKey(searchKey);
        //to the true wrapper
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("organization_id",organizationId).and(i -> {
            //add conditions into a wrapper
            for(Map.Entry<String,String> entry : map.entrySet()){
                i.or().like(entry.getKey(),entry.getValue());
            }
        });
        wrapper.ne("user_id",userService.getId());
        page = userService.getUserPermissionDTOInOrganization(page,wrapper);
        return page;
    }
}
