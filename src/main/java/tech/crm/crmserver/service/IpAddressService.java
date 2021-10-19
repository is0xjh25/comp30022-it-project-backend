package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dao.IpAddress;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-19
 */
public interface IpAddressService extends IService<IpAddress> {

    /**
     * check whether this ip is a new ip for the user<br/>
     * if yes, send a email to user and return true
     * if no, return false
     * @param userId the id of user
     * @param ip the ip
     * @return whether is a new ip
     */
    public boolean checkIpAddress(Integer userId, String ip);

    /**
     * save a new address according to ip
     * @param userId the id of user
     * @param ip the ip
     */
    public void saveNewIp(Integer userId, String ip);

    /**
     * clean ip address by user id
     * @param userId the id of user
     */
    public void cleanIpByUserId(Integer userId);

}
