package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.dao.BelongTo;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.mapper.OrganizationMapper;
import tech.crm.crmserver.mapper.PermissionMapper;
import tech.crm.crmserver.service.DepartmentService;
import tech.crm.crmserver.service.OrganizationService;
import tech.crm.crmserver.service.PermissionService;
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
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    public DepartmentService departmentService;

    @Autowired
    public PermissionMapper permissionMapper;

    @Override
    public boolean checkPendingPermission(Integer departmentId) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department_id", departmentId);
        queryWrapper.eq("authority_level", PermissionLevel.PENDING);
        return false;
    }

    @Override
    public boolean checkPendingPermission(Integer organizationId, Integer departmentId) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();

        List<Integer> departmentIdList = new ArrayList<>();
        if (departmentId != null) {
            departmentIdList.add(departmentId);
        } else if (organizationId != null) {
            departmentIdList.addAll(departmentService.getDepartmentIdByOrganization(organizationId));
        }
        for (Integer department : departmentIdList) {
            if(checkPendingPermission(department)) {
                return true;
            };
        }
        return false;
    }

    @Override
    public boolean getAllPermissionInDepartmentOrdered(Integer departmentId) {
        // figure out all pending permission
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department_id", departmentId).eq("authority_level", PermissionLevel.PENDING.getLevel());


        // figure out all other



        return false;
    }

    @Override
    public boolean getAllPermissionInDepartmentOrdered(Integer departmentId, Integer page, Integer total) {
        // figure out all pending permission
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department_id", departmentId).eq("authority_level", PermissionLevel.PENDING.getLevel());


        // figure out all other





        return false;
    }
}
