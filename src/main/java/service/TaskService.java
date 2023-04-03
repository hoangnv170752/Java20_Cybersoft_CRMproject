package service;

import model.ProjectModel;
import model.StatusModel;
import model.TaskModel;
import model.UserModel;
import repository.ProjectRepository;
import repository.StatusRepository;
import repository.TaskRepository;
import repository.UserRepository;

import java.util.List;

public class TaskService {
    TaskRepository taskRepository = new TaskRepository();
    ProjectRepository projectRepository = new ProjectRepository();
    UserRepository userRepository = new UserRepository();
    StatusRepository statusRepository = new StatusRepository();

    public TaskModel getTaskById(int taskId){

        return taskRepository.getTaskById(taskId);
    }

    public List<TaskModel> getAllTask(){

        return taskRepository.getAllTask();
    }

    public List<TaskModel> getAllTaskByLeaderId(int leaderId){

        return taskRepository.getAllTaskByLeaderIdOfProject(leaderId);
    }

    public boolean addTask(TaskModel newTask){

        return taskRepository.addTask(newTask) > 0;
    }

    public boolean deleteTask(int taskId){

        return taskRepository.deleteTaskById(taskId) > 0;
    }

    public boolean editTask(TaskModel task){

        return taskRepository.updateTask(task) > 0;
    }

    public List<ProjectModel> getProjectList(){

        return projectRepository.getAllProject();
    }

    public List<UserModel> getMemberList(){

        return userRepository.getAllUserExceptAdmin();
    }

    public List<StatusModel> getStatusList(){

        return statusRepository.getAllStatus();
    }
}
