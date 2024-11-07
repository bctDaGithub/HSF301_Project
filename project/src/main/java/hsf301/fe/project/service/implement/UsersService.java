package hsf301.fe.project.service.implement;

import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.repository.IUsersRepository;
import hsf301.fe.project.service.defines.IUsersService;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService implements IUsersService {
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private IUsersRepository usersRepository;

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
}