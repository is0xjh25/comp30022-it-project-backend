package tech.crm.crmserver.service;

import tech.crm.crmserver.dao.Department;
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
public interface DepartmentService extends IService<Department> {

    List<Integer> getDepartmentIdByOrganization(Integer organizationId);

}
