package repository;

import config.DbConfig;
import model.tableData.RoleColumn;
import model.RoleModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleRepository {
    public List<RoleModel> getAllRole(){
        List<RoleModel> listRoe = new ArrayList<>();
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT * FROM roles AS r ORDER BY r.id";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                RoleModel roleModel = new RoleModel();
                roleModel.setId(resultSet.getInt(RoleColumn.ID.getValue()));
                roleModel.setName(resultSet.getString(RoleColumn.NAME.getValue()));
                roleModel.setDescription(resultSet.getString(RoleColumn.DESCRIPTION.getValue()));

                listRoe.add(roleModel);
            }
        }catch (Exception e){
            System.out.println("An error occurred when get all role in database | "+e.getMessage());
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
        return listRoe;
    }
    public RoleModel getRole(int roleId){
        RoleModel roleModel = new RoleModel();
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT * FROM roles AS r WHERE r.id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,roleId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                roleModel.setId(resultSet.getInt(RoleColumn.ID.getValue()));
                roleModel.setName(resultSet.getString(RoleColumn.NAME.getValue()));
                roleModel.setDescription(resultSet.getString(RoleColumn.DESCRIPTION.getValue()));
            }
        }catch (Exception e){
            System.out.println("An error occurred when get role in database | "+e.getMessage());
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

        return roleModel;
    }
    public int addRole(RoleModel newRole){
        int result = -1;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "INSERT INTO roles (name,description) VALUES (?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,newRole.getName());
            statement.setString(2,newRole.getDescription());

            result = statement.executeUpdate();
        }catch (Exception e){
            System.out.println("An error occurred when insert role in database | "+e.getMessage());
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
    public int deleteRoleById(int roleID){
        int result = -1;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "DELETE FROM roles WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,roleID);

            result = statement.executeUpdate();
        }catch (Exception e){
            System.out.println("An error occurred when delete role in database | "+e.getMessage());
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
    public int updateRoleById(RoleModel role){
        int result = -1;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "UPDATE roles AS r SET r.name = ?,r.description = ? WHERE r.id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,role.getName());
            statement.setString(2,role.getDescription());
            statement.setInt(3,role.getId());
            result = statement.executeUpdate();
        }catch (Exception e){
            System.out.println("An error occurred when update role in database | "+e.getMessage());
            e.printStackTrace();
        } finally {
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
}
