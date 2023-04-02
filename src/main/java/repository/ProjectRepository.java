package repository;
import config.DbConfig;
import model.*;
import model.tableData.ProjectColumn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class ProjectRepository {
    private String changeDateFormat(String oldDate){
        String newDate = "";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(oldDate);
            newDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
        } catch (Exception e){
            System.out.println("Lỗi khi thay đổi định dạng ngày tháng | "+e.getMessage());
            e.printStackTrace();
        }
        return newDate;
    }
    public List<ProjectModel> getAllProject() {
        List<ProjectModel> list = new ArrayList<>();

        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT p.id,p.name,p.start_date,p.end_date,u.fullname AS leader FROM projects AS p " +
                "LEFT JOIN users AS u ON p.leader_id=u.id ORDER BY p.id";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                ProjectModel project = new ProjectModel();
                project.setId(resultSet.getInt(ProjectColumn.ID.getValue()));
                project.setName(resultSet.getString(ProjectColumn.NAME.getValue()));
                project.setStart_date(changeDateFormat(resultSet.getString(ProjectColumn.START_DATE.getValue())));
                project.setEnd_date(changeDateFormat(resultSet.getString(ProjectColumn.END_DATE.getValue())));
                UserModel user = new UserModel();
                user.setFullname(resultSet.getString("leader"));
                project.setLeader(user);
                list.add(project);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy toàn bộ project trong database | "+e.getMessage());
            e.printStackTrace();
        } finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("Lỗi xảy ra khi đóng database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
    public List<ProjectModel> getAllProjectByLeaderId(int leaderId) {
        List<ProjectModel> list = new ArrayList<>();
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT p.id,p.name,p.start_date,p.end_date,u.fullname AS leader FROM projects AS p " +
                "LEFT JOIN users AS u ON p.leader_id=u.id WHERE p.leader_id=? ORDER BY p.id";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, leaderId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                ProjectModel project = new ProjectModel();
                project.setId(resultSet.getInt(ProjectColumn.ID.getValue()));
                project.setName(resultSet.getString(ProjectColumn.NAME.getValue()));
                project.setStart_date(changeDateFormat(resultSet.getString(ProjectColumn.START_DATE.getValue())));
                project.setEnd_date(changeDateFormat(resultSet.getString(ProjectColumn.END_DATE.getValue())));
                UserModel user = new UserModel();
                user.setFullname(resultSet.getString("leader"));
                project.setLeader(user);

                list.add(project);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy toàn bộ project trong database | "+e.getMessage());
            e.printStackTrace();
        } finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("Lỗi xảy ra khi đóng database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
    public ProjectModel getProjectById(int projectId, boolean changeDateFormat){
        ProjectModel project = new ProjectModel();
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT p.id,p.name,p.start_date,p.end_date,u.id AS leader_id,u.fullname AS leader FROM projects AS p " +
                "LEFT JOIN users AS u ON p.leader_id=u.id WHERE p.id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,projectId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                project.setId(resultSet.getInt(ProjectColumn.ID.getValue()));
                project.setName(resultSet.getString(ProjectColumn.NAME.getValue()));
                if(changeDateFormat){
                    project.setStart_date(changeDateFormat(resultSet.getString(ProjectColumn.START_DATE.getValue())));
                    project.setEnd_date(changeDateFormat(resultSet.getString(ProjectColumn.END_DATE.getValue())));
                } else {
                    project.setStart_date(resultSet.getString(ProjectColumn.START_DATE.getValue()));
                    project.setEnd_date(resultSet.getString(ProjectColumn.END_DATE.getValue()));
                }
                UserModel user = new UserModel();
                user.setFullname(resultSet.getString("leader"));
                user.setId(Integer.parseInt(resultSet.getString("leader_id")));
                project.setLeader(user);
            }
        }catch (Exception e){
            System.out.println("Lỗi khi lấy 1 project trong database | "+e.getMessage());
            e.printStackTrace();
        }finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("Lỗi khi đóng database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return project;
    }
    public int deleteProjectById(int projectId){
        int result = -1;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "DELETE FROM projects AS p WHERE p.id = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,projectId);
            result = statement.executeUpdate();
        } catch (Exception e){
            System.out.println("Lỗi khi xóa 1 project trong database | "+e.getMessage());
            e.printStackTrace();
        } finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("Lỗi khi đóng database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public int addProject(ProjectModel newProject){
        int result = -1;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "INSERT INTO projects (name,start_date,end_date,leader_id) VALUES (?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,newProject.getName());
            statement.setString(2,newProject.getStart_date());
            statement.setString(3,newProject.getEnd_date());
            statement.setInt(4,newProject.getLeader().getId());
            result = statement.executeUpdate();
        } catch (Exception e){
            System.out.println("Lỗi xảy ra khi thêm 1 project và | "+e.getMessage());
            e.printStackTrace();
        } finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e){
                    System.out.println("Lỗi khi đóng database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public int updateProjectById(ProjectModel project){
        int result = -1;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "UPDATE projects AS p SET p.name = ?,p.start_date = ?,p.end_date = ?,p.leader_id = ? " +
                "WHERE p.id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,project.getName());
            statement.setString(2,project.getStart_date());
            statement.setString(3,project.getEnd_date());
            statement.setInt(4,project.getLeader().getId());
            statement.setInt(5,project.getId());
            result = statement.executeUpdate();
        } catch (Exception e){
            System.out.println("Lỗi xảy ra khi cập nhật project trên database | "+e.getMessage());
            e.printStackTrace();
        } finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e){
                    System.out.println("Lỗi khi đóng database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public int countProjectByUserId(int leaderId){
        int count = 0;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT count(*) as count FROM projects AS p WHERE p.leader_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,leaderId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                count = resultSet.getInt("count");
            }
        }catch (Exception e){
            System.out.println("Lỗi xảy ra khi đếm project với leaderId| "+e.getMessage());
            e.printStackTrace();
        }finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("Lỗi khi đóng database | "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return count;
    }
}
