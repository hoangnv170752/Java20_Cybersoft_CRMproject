package model;

public class TaskModel {
    private int id;
    private String name;
    private String start_date;
    private String end_date;
    private UserModel user;
    private ProjectModel project;
    private StatusModel status;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStart_date() {
        return start_date;
    }
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
    public String getEnd_date() {
        return end_date;
    }
    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
    public UserModel getUser() {
        return user;
    }
    public void setUser(UserModel user) {
        this.user = user;
    }
    public ProjectModel getProject() {
        return project;
    }
    public void setProject(ProjectModel project) {
        this.project = project;
    }
    public StatusModel getStatus() {
        return status;
    }
    public void setStatus(StatusModel status) {
        this.status = status;
    }
}
