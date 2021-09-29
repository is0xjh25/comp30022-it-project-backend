package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dao.Department;

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

    /**
     * Get departments from organizationId
     *
     * @param organizationId the organizationId of the organization to get department
     * @return a list of departmentId
     */
    List<Integer> getDepartmentIdByOrganization(Integer organizationId);

    /**
     * Delete the department by department id <br/>
     * will delete the related entities(permissions, contacts)<br/>
     * will check the permission<br/>
     *
     * @param departmentId the id of department needed to be deleted
     */
    public void deleteDepartmentByDepartmentId(Integer departmentId);

    /**
     * Delete the departments by department id list<br/>
     * will delete the related entities(permissions, contacts)<br/>
     * will not check the permission<br/>
     *
     * @param departmentIdList the list of ids of department needed to be deleted
     */
    public void deleteDepartmentByDepartmentIdList(List<Integer> departmentIdList);


    /**
     * Delete the departments by organization id <br/>
     * will delete the related entities(permissions, contacts)<br/>
     * will not check the permission<br/>
     * @param organizationId the id of organization needed to be deleted
     */
    public void deleteDepartmentByOrganizationId(Integer organizationId);

    /**
     * create department in the organization
     * @param orgId the organization this department belong to
     * @param name the name of the department
     */
    public void createDepartment( Integer orgId, String name);

    /**
     * Check if department is exits and active
     *
     * @param departmentId
     * @return if the department with the input departmentId is exits and the status is active
     */
    public boolean checkIfValidDepartmentId(Integer departmentId);


}
