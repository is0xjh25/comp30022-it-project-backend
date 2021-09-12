package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.mapper.OrganizationMapper;
import tech.crm.crmserver.service.OrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

    @Override
    public List<Organization> getAllOrgUserOwnAndBelongTo(Integer userId) {
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put("owner", userId);
        // Own
        List<Organization> orgOwnByUser = organizationMapper.selectByMap(selectMap);
        // Belong to
        List<Organization> orgBelongsToUser = organizationMapper.getOrganizationUserBelongsTo(userId);
        orgOwnByUser.addAll(orgBelongsToUser);
        return orgOwnByUser;
    }


    @Override
    public List<Organization> getOrgBasedOnName(String orgNameName) {
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", orgNameName).likeLeft("name", orgNameName).likeRight("name", orgNameName);
        List<Organization> organizations = organizationMapper.selectList(queryWrapper);
        return organizations;
    }

    @Override
    public List<Organization> getOrgBasedOnExactName(String organizationName) {
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", organizationName);
        List<Organization> organizations = organizationMapper.selectList(queryWrapper);
        return organizations;
    }
}
