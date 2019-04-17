package web.globalbeershop;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test") // use application-test.yml properties (in-memory DB)
@Transactional // rollback DB in between tests
public class IntegrationTest {

    @Test
    public void integrationLoads() {
        Assert.assertTrue(true);
    }
}
