package web.globalbeershop.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import web.globalbeershop.data.User;
import web.globalbeershop.data.UserDTO;

public interface IUserService extends UserDetailsService {

    User findByEmail(String email);

    User save(UserDTO user);
}