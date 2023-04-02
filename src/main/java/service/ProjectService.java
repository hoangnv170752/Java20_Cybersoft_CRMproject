package service;

import model.ProjectModel;
import model.TaskModel;
import repository.ProjectRepository;
import repository.TaskRepository;
import java.util.List;

public class ProjectService {
    ProjectRepository projectRepository = new ProjectRepository();
    TaskRepository taskRepository = new TaskRepository();
    public List<ProjectModel> getAllProject(){
        return projectRepository.getAllProject();
    }
    public List<ProjectModel> getAllProjectByLeaderId(int leaderId){

        return projectRepository.getAllProjectByLeaderId(leaderId);
    }
    public ProjectModel getProject(int projectId, boolean changeDateFormat){

        return projectRepository.getProjectById(projectId,changeDateFormat);
    }
    public boolean deleteProject(int projectId){

        return projectRepository.deleteProjectById(projectId) > 0;
    }
    public boolean addProject(ProjectModel newProject){

        return projectRepository.addProject(newProject) > 0;
    }
    public boolean editProject(ProjectModel project){

        return projectRepository.updateProjectById(project) > 0;
    }
    public boolean checkExistingOfTaskByProjectId(int projectId){

        return taskRepository.countTaskByProjectId(projectId) > 0;
    }
    public List<Integer> getTaskStatics(int projectId){

        return taskRepository.getTaskStatusByProjectId(projectId);
    }
    public List<TaskModel> getTaskListOfProject(int projectId){

        return taskRepository.getTaskListByProjectId(projectId);
    }
}
