package repository;

import config.DbConfig;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskRepository {

    private String changeDateFormat(String oldDate){
        String newDate = "";

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(oldDate);
            newDate = new SimpleDateFormat("dd/MM/yyyy").format(date);

        } catch (Exception e){
            System.out.println("An error occurred when change date format | "+e.getMessage());
            e.printStackTrace();
        }

        return newDate;
    }

    public TaskModel getTaskById(int taskId){
        TaskModel task = new TaskModel();
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT * FROM tasks AS t WHERE t.id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,taskId);
            ResultSet resultSet = statement.executeQuery();;
            while (resultSet.next()){
                task.setId(resultSet.getInt(TaskColumn.ID.getValue()));
                task.setName(resultSet.getString(TaskColumn.NAME.getValue()));
                task.setStart_date(resultSet.getString(TaskColumn.START_DATE.getValue()));
                task.setEnd_date(resultSet.getString(TaskColumn.END_DATE.getValue()));
                ProjectModel project = new ProjectModel();
                project.setId(resultSet.getInt(TaskColumn.PROJECT_ID.getValue()));
                task.setProject(project);
                UserModel member = new UserModel();
                member.setId(resultSet.getInt(TaskColumn.USER_ID.getValue()));
                task.setUser(member);
                StatusModel status = new StatusModel();
                status.setId(resultSet.getInt(TaskColumn.STATUS_ID.getValue()));
                task.setStatus(status);
            }
        } catch (Exception e){
            System.out.println("An error occurred when get one task in database | "+e.getMessage());
            e.printStackTrace();
        } finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return task;
    }


    public List<TaskModel> getAllTask(){
        List<TaskModel> taskList = new ArrayList<>();
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT t.id,t.name,p.name AS projectName,u.fullname AS userName," +
                "t.start_date,t.end_date,s.name AS status FROM tasks AS t " +
                "LEFT JOIN users AS u ON t.user_id=u.id " +
                "LEFT JOIN projects AS p ON t.project_id=p.id " +
                "LEFT JOIN statuses AS s ON t.status_id=s.id ORDER BY t.project_id";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                TaskModel task = new TaskModel();
                task.setId(resultSet.getInt(TaskColumn.ID.getValue()));
                task.setName(resultSet.getString(TaskColumn.NAME.getValue()));
                task.setStart_date(changeDateFormat(resultSet.getString(TaskColumn.START_DATE.getValue())));
                task.setEnd_date(changeDateFormat(resultSet.getString(TaskColumn.END_DATE.getValue())));
                ProjectModel project = new ProjectModel();
                project.setName(resultSet.getString("projectName"));
                task.setProject(project);
                UserModel user = new UserModel();
                user.setFullname(resultSet.getString("userName"));
                task.setUser(user);
                StatusModel status = new StatusModel();
                status.setName(resultSet.getString("status"));
                task.setStatus(status);

                taskList.add(task);
            }
        } catch (Exception e){
            System.out.println("An error occurred when get all task in database | "+e.getMessage());
            e.printStackTrace();
        } finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return taskList;
    }

    public List<TaskModel> getAllTaskByLeaderIdOfProject(int leaderId){
        List<TaskModel> taskList = new ArrayList<>();
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT t.id,t.name,p.name AS projectName,u.fullname AS userName," +
                "t.start_date,t.end_date,s.name AS status FROM tasks AS t " +
                "LEFT JOIN users AS u ON t.user_id=u.id " +
                "LEFT JOIN projects AS p ON t.project_id=p.id " +
                "LEFT JOIN statuses AS s ON t.status_id=s.id WHERE p.leader_id=? ORDER BY t.project_id";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,leaderId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                TaskModel task = new TaskModel();
                task.setId(resultSet.getInt(TaskColumn.ID.getValue()));
                task.setName(resultSet.getString(TaskColumn.NAME.getValue()));
                task.setStart_date(changeDateFormat(resultSet.getString(TaskColumn.START_DATE.getValue())));
                task.setEnd_date(changeDateFormat(resultSet.getString(TaskColumn.END_DATE.getValue())));
                ProjectModel project = new ProjectModel();
                project.setName(resultSet.getString("projectName"));
                task.setProject(project);
                UserModel user = new UserModel();
                user.setFullname(resultSet.getString("userName"));
                task.setUser(user);
                StatusModel status = new StatusModel();
                status.setName(resultSet.getString("status"));
                task.setStatus(status);

                taskList.add(task);
            }
        } catch (Exception e){
            System.out.println("An error occurred when get all task in database | "+e.getMessage());
            e.printStackTrace();
        } finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return taskList;
    }

    public int deleteTaskById(int taskId){
        int result = -1;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "DELETE FROM tasks AS t WHERE t.id = ? ";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,taskId);

            result = statement.executeUpdate();
        } catch (Exception e){
            System.out.println("An error occurred when delete task in database | "+e.getMessage());
            e.printStackTrace();
        } finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public int addTask(TaskModel newTask){
        int result = -1;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "INSERT INTO tasks (name,start_date,end_date,user_id,project_id,status_id) " +
                "VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,newTask.getName());
            statement.setString(2,newTask.getStart_date());
            statement.setString(3,newTask.getEnd_date());
            statement.setInt(4,newTask.getUser().getId());
            statement.setInt(5,newTask.getProject().getId());
            statement.setInt(6,newTask.getStatus().getId());

            result = statement.executeUpdate();
        }catch (Exception e){
            System.out.println("An error occurred when insert task in database | "+e.getMessage());
            e.printStackTrace();
        }finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public int updateTask(TaskModel task){
        int result = -1;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "UPDATE tasks AS t SET t.name=?,t.start_date=?,t.end_date=?,t.user_id=?,t.project_id=?, " +
                "t.status_id=? WHERE t.id=?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,task.getName());
            statement.setString(2,task.getStart_date());
            statement.setString(3,task.getEnd_date());
            statement.setInt(4,task.getUser().getId());
            statement.setInt(5,task.getProject().getId());
            statement.setInt(6,task.getStatus().getId());
            statement.setInt(7,task.getId());

            result = statement.executeUpdate();
        }catch (Exception e){
            System.out.println("An error occurred when update task in database | "+e.getMessage());
            e.printStackTrace();
        }finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public List<Integer> getTaskStatusByUserId(int userID){
        List<Integer> taskList = new ArrayList<>();

        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT t.status_id FROM tasks as t WHERE t.user_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                taskList.add(resultSet.getInt("status_id"));
            }
        }catch (Exception e){
            System.out.println("An error occurred when get task status in database | "+e.getMessage());
            e.printStackTrace();
        }finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return taskList;
    }

    public List<TaskModel> getTaskListByUserId(int userID){
        List<TaskModel> list = new ArrayList<>();

        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT t.id,t.name,p.name AS project,t.start_date,t.end_date,s.name AS status " +
                "FROM tasks AS t LEFT JOIN projects AS p ON t.project_id = p.id " +
                "LEFT JOIN statuses AS s ON t.status_id = s.id " +
                "WHERE t.user_id = ? ORDER BY t.start_date";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,String.valueOf(userID));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                TaskModel task = new TaskModel();
                task.setId(resultSet.getInt(TaskColumn.ID.getValue()));
                task.setName(resultSet.getString(TaskColumn.NAME.getValue()));
                task.setStart_date(changeDateFormat(resultSet.getString(TaskColumn.START_DATE.getValue())));
                task.setEnd_date(changeDateFormat(resultSet.getString(TaskColumn.END_DATE.getValue())));
                ProjectModel project = new ProjectModel();
                project.setName(resultSet.getString("project"));
                task.setProject(project);
                StatusModel status = new StatusModel();
                status.setName(resultSet.getString("status"));
                task.setStatus(status);

                list.add(task);
            }
        }catch (Exception e){
            System.out.println("An error occurred when get task list of user in database | "+e.getMessage());
            e.printStackTrace();
        }finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    public TaskModel getTaskByIdInProfile(int taskID){
        TaskModel task = new TaskModel();

        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT t.id,p.name AS project,t.name,t.start_date,t.end_date,t.status_id " +
                "FROM tasks AS t LEFT JOIN projects AS p ON t.project_id = p.id WHERE t.id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,taskID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                task.setId(resultSet.getInt(TaskColumn.ID.getValue()));
                task.setName(resultSet.getString(TaskColumn.NAME.getValue()));
                task.setStart_date(changeDateFormat(resultSet.getString(TaskColumn.START_DATE.getValue())));
                task.setEnd_date(changeDateFormat(resultSet.getString(TaskColumn.END_DATE.getValue())));
                ProjectModel project = new ProjectModel();
                project.setName(resultSet.getString("project"));
                task.setProject(project);
                StatusModel status = new StatusModel();
                status.setId(resultSet.getInt(TaskColumn.STATUS_ID.getValue()));
                task.setStatus(status);
            }
        }catch (Exception e){
            System.out.println("An error occurred when get one task in database | "+e.getMessage());
            e.printStackTrace();
        }finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return task;
    }

    public int updateStatusOfTask(int taskID, int statusID){
        int result = -1;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "UPDATE tasks SET status_id = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,statusID);
            statement.setInt(2,taskID);

            result = statement.executeUpdate();
        }catch (Exception e){
            System.out.println("An error occurred when update status of task in database | "+e.getMessage());
            e.printStackTrace();
        }finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public List<TaskModel> getTaskListByMemberId(int userID){
        List<TaskModel> list = new ArrayList<>();

        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT t.status_id,p.name AS project,t.name,t.start_date,t.end_date FROM tasks AS t " +
                "LEFT JOIN projects AS p ON t.project_id = p.id WHERE t.user_id = ? ORDER BY t.start_date";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                TaskModel task = new TaskModel();
                task.setName(resultSet.getString(TaskColumn.NAME.getValue()));
                task.setStart_date(changeDateFormat(resultSet.getString(TaskColumn.START_DATE.getValue())));
                task.setEnd_date(changeDateFormat(resultSet.getString(TaskColumn.END_DATE.getValue())));
                ProjectModel project = new ProjectModel();
                project.setName(resultSet.getString("project"));
                task.setProject(project);
                StatusModel status = new StatusModel();
                status.setId(resultSet.getInt(TaskColumn.STATUS_ID.getValue()));
                task.setStatus(status);

                list.add(task);
            }
        }catch (Exception e){
            System.out.println("An error occurred when get task list of member in database | "+e.getMessage());
            e.printStackTrace();
        }finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    public int countTaskByUserId(int userId){
        int count = 0;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT count(*) as count FROM tasks AS t WHERE t.user_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                count = resultSet.getInt("count");
            }
        }catch (Exception e){
            System.out.println("An error occurred when count task by userId in database | "+e.getMessage());
            e.printStackTrace();
        }finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return count;
    }

    public int countTaskByProjectId(int projectId){
        int count = 0;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT count(*) as count FROM tasks AS t WHERE t.project_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,projectId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                count = resultSet.getInt("count");
            }
        }catch (Exception e){
            System.out.println("An error occurred when count task by projectId in database | "+e.getMessage());
            e.printStackTrace();
        }finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return count;
    }

    public List<Integer> getTaskStatusByProjectId(int projectId){
        List<Integer> list = new ArrayList<>();
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT t.status_id FROM tasks AS t WHERE t.project_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,projectId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                list.add(resultSet.getInt("status_id"));
            }
        } catch (Exception e){
            System.out.println("An error occurred when get task status by projectId in database | "+e.getMessage());
            e.printStackTrace();
        } finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    public List<TaskModel> getTaskListByProjectId(int projectId){
        List<TaskModel> list = new ArrayList<>();
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT u.id AS userId,u.fullname AS userName,u.avatar AS userAvatar,t.name,t.start_date,t.end_date,t.status_id " +
                "FROM tasks AS t LEFT JOIN users AS u ON t.user_id=u.id WHERE t.project_id = ? ORDER BY t.start_date";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,projectId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                TaskModel task = new TaskModel();
                task.setName(resultSet.getString(TaskColumn.NAME.getValue()));
                task.setStart_date(changeDateFormat(resultSet.getString(TaskColumn.START_DATE.getValue())));
                task.setEnd_date(changeDateFormat(resultSet.getString(TaskColumn.END_DATE.getValue())));
                UserModel user = new UserModel();
                user.setId(resultSet.getInt("userId"));
                user.setFullname(resultSet.getString("userName"));
                user.setAvatar(resultSet.getString("userAvatar"));
                task.setUser(user);
                StatusModel status = new StatusModel();
                status.setId(resultSet.getInt(TaskColumn.STATUS_ID.getValue()));
                task.setStatus(status);
                list.add(task);
            }
        } catch (Exception e){
            System.out.println("An error occurred when get task by projectId in database | "+e.getMessage());
            e.printStackTrace();
        } finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("An error occurred when close database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}
