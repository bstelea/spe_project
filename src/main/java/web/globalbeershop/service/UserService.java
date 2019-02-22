package web.globalbeershop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import web.globalbeershop.data.User;
import web.globalbeershop.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public User createUser(String username, String role, String password) {
        User s = new User();

        s.setUsername(username);
        s.setRole(role);
        s.setEnabled(1);
        s.setPassword(password);
        saveUser(s);

        return s;
    }


    public User saveUser(User user) {
        System.out.printf("\n" + passwordEncoder.encode(user.getPassword())+"\n");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(1);
        return userRepository.save(user);
    }
}