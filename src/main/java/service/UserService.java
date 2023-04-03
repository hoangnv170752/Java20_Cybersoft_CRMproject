package service;

import model.RoleModel;
import model.TaskModel;
import model.UserModel;
import repository.ProjectRepository;
import repository.RoleRepository;
import repository.TaskRepository;
import repository.UserRepository;
import java.util.List;

public class UserService {
    UserRepository userRepository = new UserRepository();
    RoleRepository roleRepository = new RoleRepository();
    TaskRepository taskRepository = new TaskRepository();
    ProjectRepository projectRepository = new ProjectRepository();
    public List<UserModel> getAllUser(){
        return userRepository.getAllUser();
    }

    public List<RoleModel> getAllRole(){
        return roleRepository.getAllRole();
    }

    public boolean addUser(UserModel user){
        return userRepository.addUser(user)>0;
    }
    public boolean deleteUser(int userID){
        return userRepository.deleteUserById(userID)>0;
    }
    public UserModel getMember(int userID){
        return userRepository.getUserById(userID);
    }
    public boolean updateUser(UserModel user){
        return userRepository.updateUserById(user)>0;
    }
    public List<TaskModel> getTaskListOfMember(int userID){
        return taskRepository.getTaskListByMemberId(userID);
    }
    public boolean checkExistingOfTaskByUserId(int userId){
        return taskRepository.countTaskByUserId(userId) > 0;
    }
    public boolean checkExistingOfProjectByLeaderId(int leaderId){
        return projectRepository.countProjectByUserId(leaderId) > 0;
    }
    public List<UserModel> getLeaderList(){
        return userRepository.getAllLeader();
    }
}
