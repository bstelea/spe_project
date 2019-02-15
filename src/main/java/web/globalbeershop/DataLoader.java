package web.globalbeershop;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import web.globalbeershop.repository.UserRepository;
import web.globalbeershop.service.UserService;

@Component
public class DataLoader implements ApplicationRunner {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    public void run(ApplicationArguments args) {

        if (userRepository.findByUsername("admin") == null) {
            userService.createUser("admin", "ADMIN", "password");
        }
        if (userRepository.findByUsername("user") == null) {
            userService.createUser("user", "USER", "password");
        }

    }
}