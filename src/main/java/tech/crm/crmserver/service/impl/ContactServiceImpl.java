package tech.crm.crmserver.service.impl;

import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.mapper.ContactMapper;
import tech.crm.crmserver.service.ContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-22
 */
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact> implements ContactService {

}
