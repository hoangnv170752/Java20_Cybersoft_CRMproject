package service;
import model.UserModel;
import repository.UserRepository;
public class AuthService {
    UserRepository userRepository = new UserRepository();
    public UserModel getUser(String email){
        return userRepository.getUserByEmail(email);
    }
    public int getRoleByEmail(String email){
        return userRepository.getRoleByEmail(email);
    }
}
