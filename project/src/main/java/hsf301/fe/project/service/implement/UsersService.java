package hsf301.fe.project.service.implement;

import hsf301.fe.project.pojo.Cart;
import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.repository.CartRepository;
import hsf301.fe.project.repository.IUsersRepository;
import hsf301.fe.project.service.defines.ICartService;
import hsf301.fe.project.service.defines.IUsersService;
import jakarta.transaction.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService implements IUsersService {
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private IUsersRepository usersRepository;
    private CartRepository cartRepository;

    @Override
    @Transactional
    public void setUserActiveStatus(int userId, boolean active) {
        usersRepository.updateActive(userId, active);
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    @Transactional
    public Users updateUser(Users user) {
        return usersRepository.saveAndFlush(user);
    }

    @Override
    public Users findByUserId(int userId) {
        return usersRepository.getReferenceById(userId);
    }

    @Override
    @Transactional
    public Users createNewUsers(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setActive(true);
        LocalDate today = LocalDate.now();
        users.setRegisterDate(Date.valueOf(today));
//        Cart cart = new Cart();
//        cart.setCustomer(users);
//        cartRepository.save(cart);
        return usersRepository.save(users);
    }

    @Override
    public Users authenticateUser(String email, String password) {
        Users user = usersRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }
}