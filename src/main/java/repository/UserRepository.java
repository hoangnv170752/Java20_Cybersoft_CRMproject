package repository;
import config.DbConfig;
import filter.AuthList;
import model.RoleModel;
import model.UserColumn;
import model.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class UserRepository {
    public int countUserByEmailNPassword(String email, String password){
        int count = 0;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT count(*) as count FROM users u WHERE u.email = ? and u.password = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,email);
            statement.setString(2,password);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                count = resultSet.getInt("count");
            }
        }catch (Exception e){
            System.out.println("An error occurred when count user in database | "+e.getMessage());
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
    public UserModel getUserByEmail(String email){
        UserModel user = new UserModel();

        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT * FROM users u WHERE u.email = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,email);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                user.setId(resultSet.getInt(UserColumn.ID.getValue()));
                user.setEmail(resultSet.getString(UserColumn.EMAIL.getValue()));
                user.setFullname(resultSet.getString(UserColumn.FULLNAME.getValue()));
                user.setAvatar(resultSet.getString(UserColumn.AVATAR.getValue()));
                RoleModel role = new RoleModel();
                role.setId(resultSet.getInt(UserColumn.ROLE_ID.getValue()));
                user.setRole(role);
            }
        }catch (Exception e){
            System.out.println("An error occurred when get user info in database | "+e.getMessage());
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
        return user;
    }
    public List<UserModel>  getAllUser(){
        List<UserModel> list = new ArrayList<>();
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT u.id, u.email, u.fullname, r.name AS role_name FROM users AS u " +
                "LEFT JOIN roles AS r ON u.role_id = r.id ORDER BY u.role_id";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                UserModel user = new UserModel();
                user.setId(resultSet.getInt(UserColumn.ID.getValue()));
                user.setEmail(resultSet.getString(UserColumn.EMAIL.getValue()));
                user.setFullname(resultSet.getString(UserColumn.FULLNAME.getValue()));
                RoleModel role = new RoleModel();
                role.setName(resultSet.getString("role_name"));
                user.setRole(role);
                list.add(user);
            }
        }catch (Exception e){
            System.out.println("An error occurred when get all user in database | "+e.getMessage());
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
    public int addUser(UserModel newUser){
        int result = -1;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "INSERT INTO users (email,password,fullname,avatar,role_id) " +
                "VALUES (?,?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,newUser.getEmail());
            statement.setString(2,newUser.getPassword());
            statement.setString(3,newUser.getFullname());
            statement.setString(4,newUser.getAvatar());
            statement.setInt(5,newUser.getRole().getId());

            result = statement.executeUpdate();
        }catch (Exception e){
            System.out.println("An error occurred when insert user in database | "+e.getMessage());
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
    public int deleteUserById(int userID){
        int result = -1;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "DELETE FROM users WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userID);

            result = statement.executeUpdate();
        }catch (Exception e){
            System.out.println("An error occurred when delete user in database | "+e.getMessage());
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
    public UserModel getUserById(int userID){
        UserModel user = new UserModel();
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT u.id,u.email,u.password,u.fullname,u.avatar,u.role_id,r.name AS role_name " +
                "FROM users AS u LEFT JOIN roles AS r ON u.role_id = r.id WHERE u.id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                user.setId(resultSet.getInt(UserColumn.ID.getValue()));
                user.setEmail(resultSet.getString(UserColumn.EMAIL.getValue()));
                user.setPassword(resultSet.getString(UserColumn.PASSWORD.getValue()));
                user.setFullname(resultSet.getString(UserColumn.FULLNAME.getValue()));
                user.setAvatar(resultSet.getString(UserColumn.AVATAR.getValue()));
                RoleModel role = new RoleModel();
                role.setId(resultSet.getInt(UserColumn.ROLE_ID.getValue()));
                role.setName(resultSet.getString("role_name"));
                user.setRole(role);
            }
        }catch (Exception e){
            System.out.println("An error occurred when get a user in database | "+e.getMessage());
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
        return user;
    }
    public int updateUserById(UserModel user){
        int result = -1;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "UPDATE users AS u SET u.email = ?, u.password = ?, u.fullname = ?, " +
                "u.avatar = ?, u.role_id = ? WHERE u.id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,user.getEmail());
            statement.setString(2,user.getPassword());
            statement.setString(3,user.getFullname());
            statement.setString(4,user.getAvatar());
            statement.setInt(5,user.getRole().getId());
            statement.setInt(6,user.getId());

            result = statement.executeUpdate();
        }catch (Exception e){
            System.out.println("An error occurred when update user in database | "+e.getMessage());
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
    public int countUserByRoleId(int roleId){
        int count = 0;
        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT count(*) as count FROM users AS u WHERE u.role_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,roleId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                count = resultSet.getInt("count");
            }
        }catch (Exception e){
            System.out.println("An error occurred when count user by roleId in database | "+e.getMessage());
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

    public List<UserModel> getAllUserExceptAdmin(){
        List<UserModel> list = new ArrayList<>();

        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT u.id,u.fullname FROM users AS u WHERE u.role_id != ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, AuthList.ADMIN.getValue());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                UserModel user = new UserModel();
                user.setId(resultSet.getInt(UserColumn.ID.getValue()));
                user.setFullname(resultSet.getString(UserColumn.FULLNAME.getValue()));

                list.add(user);
            }
        }catch (Exception e){
            System.out.println("An error occurred when get all user except admin in database | "+e.getMessage());
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

    public List<UserModel> getAllLeader(){
        List<UserModel> list = new ArrayList<>();

        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT u.id,u.fullname FROM users AS u WHERE u.role_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, AuthList.LEADER.getValue());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                UserModel user = new UserModel();
                user.setId(resultSet.getInt(UserColumn.ID.getValue()));
                user.setFullname(resultSet.getString(UserColumn.FULLNAME.getValue()));

                list.add(user);
            }
        }catch (Exception e){
            System.out.println("An error occurred when get leader in database | "+e.getMessage());
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
    public int getRoleByEmail(String email){
        int roleId = 0;

        Connection connection = DbConfig.getMySQLConnection();
        String query = "SELECT u.role_id FROM users u WHERE u.email = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,email);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                roleId = resultSet.getInt(UserColumn.ROLE_ID.getValue());
            }
        }catch (Exception e){
            System.out.println("An error occurred when get role by email info in database | "+e.getMessage());
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
        return roleId;
    }
}
