package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.common.constants.EmailConstants;
import tech.crm.crmserver.common.utils.IpUtil;
import tech.crm.crmserver.dao.IpAddress;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.mapper.IpAddressMapper;
import tech.crm.crmserver.service.IpAddressService;
import tech.crm.crmserver.service.MailService;
import tech.crm.crmserver.service.UserService;

/**
 * <p>
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-19
 */
@Service
public class IpAddressServiceImpl extends ServiceImpl<IpAddressMapper, IpAddress> implements IpAddressService {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    /**
     * check whether this ip is a new ip for the user<br/>
     * if yes, send a email to user and return true
     * if no, return false
     *
     * @param userId the id of user
     * @param ip     the ip of user
     * @return whether is a new ip
     */
    @Override
    public boolean checkIpAddress(Integer userId, String ip) {
        if(ip == null){
            return false;
        }
        IpAddress ipAddress = IpUtil.sendPostRequest(ip);
        // maybe we exceed the limit of 45 request / minutes
        if(ipAddress == null){
            return false;
        }
        ipAddress.setUserId(userId);

        QueryWrapper<IpAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId).eq("country",ipAddress.getCountry())
                .eq("region_name",ipAddress.getRegionName())
                .eq("city",ipAddress.getCity());

        IpAddress ipAddress1 = this.baseMapper.selectOne(wrapper);
        if(ipAddress1 != null){
            return false;
        }
        else {
            save(ipAddress);
            User user = userService.getById(userId);
            mailService.sendSimpleMail(user.getEmail(), EmailConstants.NEW_LOGIN_TITLE,
                    "New login to your ConnecTi account from " + ipAddress.getCity() + " " + ipAddress.getRegionName() + " " + ipAddress.getCountry());
            return true;
        }
    }

    /**
     * save a new address according to ip
     *
     * @param userId the id of user
     * @param ip     the ip
     */
    @Override
    public void saveNewIp(Integer userId, String ip) {
        if(ip == null){
            return;
        }
        IpAddress ipAddress = IpUtil.sendPostRequest(ip);
        if(ipAddress == null){
            return;
        }
        ipAddress.setUserId(userId);
        save(ipAddress);
    }

    /**
     * clean ip address by user id
     *
     * @param userId the id of user
     */
    @Override
    public void cleanIpByUserId(Integer userId) {
        QueryWrapper<IpAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        this.baseMapper.delete(wrapper);
    }
}
