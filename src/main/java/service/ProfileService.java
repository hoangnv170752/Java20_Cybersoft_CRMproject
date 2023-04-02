package service;

import model.TaskModel;
import repository.TaskRepository;
import java.util.List;

public class ProfileService {
    TaskRepository taskRepository = new TaskRepository();
    public List<TaskModel> getAllTaskOfUser(int userID){
        return taskRepository.getTaskListByUserId(userID);
    }
    public TaskModel getTaskById(int taskID){
        return taskRepository.getTaskByIdInProfile(taskID);
    }
    public boolean updateStatusTask(int taskID, int statusID){
        return taskRepository.updateStatusOfTask(taskID,statusID)>0;
    }
}
