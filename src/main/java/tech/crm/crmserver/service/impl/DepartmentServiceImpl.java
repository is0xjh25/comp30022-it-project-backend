package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dao.Department;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.exception.NotEnoughPermissionException;
import tech.crm.crmserver.mapper.DepartmentMapper;
import tech.crm.crmserver.service.ContactService;
import tech.crm.crmserver.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.service.PermissionService;
import tech.crm.crmserver.service.UserService;

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
    public ContactService contactService;

    @Autowired
    public PermissionService permissionService;

    @Autowired
    public UserService userService;


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
     * delete the department by department id <br/>
     * will delete the related entities(permissions, contacts)<br/>
     * will check the permission<br/>
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
     * delete the departments by department id list<br/>
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
     * delete the departments by organization id <br/>
     * will delete the related entities(permissions, contacts)<br/>
     * will not check the permission<br/>
     *
     * @param organizationId the id of organization needed to be deleted
     */
    @Override
    public void deleteDepartmentByOrganizationId(Integer organizationId) {
        deleteDepartmentByDepartmentIdList(getDepartmentIdByOrganization(organizationId));
    }
}
