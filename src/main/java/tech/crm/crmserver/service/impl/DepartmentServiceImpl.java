package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.crm.crmserver.dao.Department;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.mapper.DepartmentMapper;
import tech.crm.crmserver.mapper.OrganizationMapper;
import tech.crm.crmserver.service.DepartmentService;
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

    @Override
    public Integer getDepartIdByExactName(String departmentName) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", departmentName);
        List<Department> departments = departmentMapper.selectList(queryWrapper);
        Integer departmentId = departments.get(0).getId();
        return departmentId;
    }

}
