package model;
import java.util.List;
public class MemberTaskModel {
    private UserModel user;
    private List<TaskModel> taskList;
    public UserModel getUser() {
        return user;
    }
    public void setUser(UserModel user) {
        this.user = user;
    }
    public List<TaskModel> getTaskList() {
        return taskList;
    }
    public void setTaskList(List<TaskModel> taskList) {
        this.taskList = taskList;
    }
}
