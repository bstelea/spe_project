package web.globalbeershop;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Before
    public void setupDatabase() {
        User user = new User("John", "Smith", "johnsmith@test.com", "Password1!");
        user.setEnabled(true);
        userRepository.save(user);
        user = new User("Bill", "Johnson", "billjohnson@test.com", "Password1!");
        user.setEnabled(true);
        userRepository.save(user);
        user = new User("Sam", "Barclay", "sambarclay@test.com", "Password1!");
        user.setEnabled(true);
        userRepository.save(user);
        user = new User("Charlie", "Figuero", "charliefiguero@test.com", "Password1!");
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Test
    public void userRepositoryCountIsCorrectTest() {
        Assert.assertTrue(userRepository.count() == 4);
    }

    @Test
    public void addingAndRemovingAnotherUserTest() {

        User user = new User("John", "Jackson", "Email@test.com", "Password1!");
        userRepository.save(user);

        User user2 = userRepository.findByEmail("Email@test.com");
        Assert.assertEquals("John", user2.getFirstName());
        Assert.assertTrue(userRepository.count() == 5);
        userRepository.delete(user);
        Assert.assertTrue(userRepository.count() == 4);
    }

    @Test
    public void findByEmailTest() {
        User user = userRepository.findByEmail("billjohnson@test.com");

        Assert.assertEquals("Bill", user.getFirstName());

        user = userService.findByEmail("billjohnson@test.com");

        Assert.assertEquals("Bill", user.getFirstName());
    }

    @Test
    public void loadByIncorrectUsernameThrowsExceptionTest() throws UsernameNotFoundException {
        try {
            UserDetails user = userService.loadUserByUsername("bijohnson@test.com");
        }
        catch (UsernameNotFoundException err) {
            Assert.assertEquals("Bill", userRepository.findByEmail("billjohnson@test.com").getFirstName());
        }
    }

    @Test
    public void checkUsersAreEnabledTest() {
        Assert.assertTrue(userRepository.findByEmail("charliefiguero@test.com").getEnabled());
    }

    @Test
    public void userCanBeDisabledTest() {
        userRepository.findByEmail("charliefiguero@test.com").setEnabled(false);
        Assert.assertFalse(userRepository.findByEmail("charliefiguero@test.com").getEnabled());
    }
}