package service;

import model.UserModel;
import repository.TaskRepository;
import repository.UserRepository;
import java.util.List;

public class HomeService {
    public UserModel getUserInfo(String email){
        UserRepository userRepository = new UserRepository();

        return userRepository.getUserByEmail(email);
    }
    public List<Integer> getTaskStatus(int userID){
        TaskRepository taskRepository = new TaskRepository();

        return taskRepository.getTaskStatusByUserId(userID);
    }
}
