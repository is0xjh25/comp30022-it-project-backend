package tech.crm.crmserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.service.OrganizationService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganizationTest {

    @Autowired
    public OrganizationService organizationService;

    @Test
    public void testOrganization() {
        List<Organization> orgList = organizationService.getAllOrgUserOwnAndBelongTo(5);
        System.out.println(orgList);
    }
}
