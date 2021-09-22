package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dao.Department;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.exception.DepartmentAlreadyExistException;
import tech.crm.crmserver.exception.NotEnoughPermissionException;
import tech.crm.crmserver.exception.OrganizationNotExistException;
import tech.crm.crmserver.mapper.DepartmentMapper;
import tech.crm.crmserver.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    public DepartmentMapper departmentMapper;

    @Autowired
    public OrganizationService organizationService;

    @Autowired
    public ContactService contactService;

    @Autowired
    public PermissionService permissionService;

    @Autowired
    public UserService userService;


    /**
     * Get departments from organizationId
     *
     * @param organizationId the organizationId of the organization to get department
     * @return a list of departmentId
     */
    @Override
    public List<Integer> getDepartmentIdByOrganization(Integer organizationId) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("organization_id", organizationId);
        List<Department> departments = departmentMapper.selectList(queryWrapper);
        List<Integer> departmentIdList = new ArrayList<>();
        for (Department department : departments) {
            departmentIdList.add(department.getId());
        }
        return departmentIdList;
    }

    /**
     * Delete the department by department id <br/>
     * will delete the related entities(permissions, contacts)<br/>
     * will check the permission<br/>
     *
     * @param departmentId the id of department needed to be deleted
     */
    @Override
    public void deleteDepartmentByDepartmentId(Integer departmentId) {
        //check permission
        Permission permission = permissionService.findPermission(departmentId, userService.getId());
        if(permission == null || !permission.getAuthorityLevel().equal(PermissionLevel.OWNER)){
            throw new NotEnoughPermissionException();
        }
        //delete
        permissionService.deletePermissionByDepartmentId(departmentId);
        contactService.deleteContactByDepartmentId(departmentId);
        baseMapper.deleteById(departmentId);
    }

    /**
     * Delete the departments by department id list<br/>
     * will delete the related entities(permissions, contacts)<br/>
     * will not check the permission<br/>
     *
     * @param departmentIdList the list of ids of department needed to be deleted
     */
    @Override
    public void deleteDepartmentByDepartmentIdList(List<Integer> departmentIdList) {
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.in("id",departmentIdList);
        //delete
        permissionService.deletePermissionByDepartmentIdList(departmentIdList);
        contactService.deleteContactByDepartmentIdList(departmentIdList);
        baseMapper.delete(wrapper);
    }

    /**
     * Delete the departments by organization id <br/>
     * will delete the related entities(permissions, contacts)<br/>
     * will not check the permission<br/>
     *
     * @param organizationId the id of organization needed to be deleted
     */
    @Override
    public void deleteDepartmentByOrganizationId(Integer organizationId) {
        deleteDepartmentByDepartmentIdList(getDepartmentIdByOrganization(organizationId));
    }

    /**
     * create department in the organization
     *
     * @param orgId the organization this department belong to
     * @param name  the name of the department
     */
    @Override
    public void createDepartment(Integer orgId, String name) {
        Organization organization = null;
        try{
            organization = organizationService.getById(orgId);
        }
        catch (Exception e){
            throw new OrganizationNotExistException();
        }
        //organization not exist
        if(organization == null){
            throw new OrganizationNotExistException();
        }
        //check the authority(creator should be the owner of the organization)
        if(!organization.getOwner().equals(userService.getId())){
            throw new NotEnoughPermissionException();
        }
        Department department = new Department();
        department.setOrganizationId(orgId);
        department.setName(name);
        //check whether there already exist a department with same name
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("organization_id",department.getOrganizationId());
        wrapper.eq("name", department.getName());
        //department already exist in this organization
        if(getOne(wrapper) != null){
            throw new DepartmentAlreadyExistException();
        }
        save(department);
        //give the owner the owner permission
        department = getOne(wrapper);
        permissionService.createPermission(department.getId(),userService.getId(),PermissionLevel.OWNER.getLevel());
    }

}
