package service;

import model.RoleModel;
import repository.RoleRepository;
import repository.UserRepository;
import java.util.List;

public class RoleService {
    RoleRepository roleRepository = new RoleRepository();
    UserRepository userRepository = new UserRepository();

    public List<RoleModel> getAllRole(){
        return roleRepository.getAllRole();
    }
    public boolean addRole(RoleModel role){
        return roleRepository.addRole(role) > 0;
    }
    public boolean delelteRole(int roleID){
        return roleRepository.deleteRoleById(roleID) > 0;
    }
    public RoleModel getRole(int roleID){
        return roleRepository.getRole(roleID);
    }
    public boolean editRole(RoleModel role){
        return roleRepository.updateRoleById(role) > 0;
    }
    public boolean checkExistingOfUserByRoleId(int roleId){
        return userRepository.countUserByRoleId(roleId) > 0;
    }
}
