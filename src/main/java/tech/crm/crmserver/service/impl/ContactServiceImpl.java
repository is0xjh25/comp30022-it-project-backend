package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.mapper.ContactMapper;
import tech.crm.crmserver.mapper.OrganizationMapper;
import tech.crm.crmserver.service.ContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.service.DepartmentService;
import tech.crm.crmserver.service.PermissionService;

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
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact> implements ContactService {

    @Autowired
    public ContactMapper contactMapper;

    @Autowired
    public DepartmentService departmentService;

    @Autowired
    public PermissionService permissionService;


    @Override
    public List<Contact> getContactByOrgIdAndDepartmentId(Integer organizationId, Integer departmentId, Integer userId) {

        QueryWrapper<Contact> queryWrapper = new QueryWrapper<>();
        List<Contact> contacts = new ArrayList<>();
        if (departmentId == null) {

            // Org的departmentId
            List<Integer> departmentIdList = departmentService.getDepartmentIdByOrganization(organizationId);

            // User加入的departmentId
            List<Permission> permissionList = permissionService.getPermissionByUserId(userId);
            List<Integer> departmentIdJoinList = new ArrayList<>();
            for (Permission permission : permissionList) {
                departmentIdJoinList.add(permission.getDepartmentId());
            }

            List<Integer> departmentInOrgAndJoin = new ArrayList<>();
            for (Integer id : departmentIdList) {
                if (departmentIdJoinList.contains(id)) {
                    departmentInOrgAndJoin.add(id);
                }
            }

            for (Integer department : departmentInOrgAndJoin) {
                queryWrapper.or().eq("department_id", department);
            }
            contacts = contactMapper.selectList(queryWrapper);
        } else {
            queryWrapper.eq("department_id", departmentId);
            contacts = contactMapper.selectList(queryWrapper);
        }

        return contacts;
    }
}
