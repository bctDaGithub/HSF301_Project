package hsf301.fe.project.service.defines;

import java.util.List;

import hsf301.fe.project.pojo.Users;

public interface IUsersService {
    public void setUserActiveStatus(int userId, boolean active);
    public List<Users> getAllUsers();
    public Users createNewUsers(Users users);
    public Users findByUserId(int userId);
    public Users updateUser(Users user);
    public Users authenticateUser(String email, String password);
    public Users findByEmail(String email);
    public List<Users> getAdminUsers();
}
