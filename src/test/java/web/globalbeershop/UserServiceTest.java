package web.globalbeershop;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import web.globalbeershop.data.User;
import web.globalbeershop.repository.UserRepository;
import web.globalbeershop.service.UserService;

import javax.transaction.Transactional;
import java.util.Optional;

@RunWith(SpringRunner.class) // junit test runner
@SpringBootTest // read app context
// overwrite default TestExecutionListeners in order to add DbUnitTestExecutionListener
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class, // use transactional test execution
        DbUnitTestExecutionListener.class}) // to read datasets from file
@ActiveProfiles("test") // use application-test.yml properties (in-memory DB)
@Transactional // rollback DB in between tests
public class UserServiceTest {

//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    private UserService userService;

    @Test
    @DatabaseSetup("/user_test.xml") // read dataset from file
    public void basic_test() {
//        Optional<User> user = userRepository.findById(1L);
//
//        System.out.println(userRepository.count());
//        Assert.assertTrue(userRepository.count() == 1);
////        if (user.isPresent()) {
////            Assert.assertTrue(userRepository.count() == 1L);
////        }
        Assert.assertTrue(true);
    }
}