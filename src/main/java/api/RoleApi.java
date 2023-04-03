package api;

import com.google.gson.Gson;
import model.RoleModel;
import model.UserModel;
import payload.Response;
import service.RoleService;
import filter.AuthenHandling;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RoleApi",urlPatterns = {"/api/role","/api/role-add","/api/role-edit"})
public class RoleApi extends HttpServlet {
    UserModel user = new UserModel();
    String roleID = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Response> responseList = new ArrayList<>();
        Response Response = new Response();

        String servletPath = req.getServletPath();
        switch (servletPath){
            case "/api/role":
                responseList = doGetOfRole(req);
                break;
            case "/api/role-add":
                responseList = doGetOfAddRole(req);
                break;
            case "/api/role-edit":
                responseList = doGetOfEditRole(req);
                break;
            default:
                Response.setStatusCode(404);
                Response.setMessage("Không tồn tại URL");

                responseList.add(Response);
                break;
        }

        Gson gson = new Gson();
        String dataJson = gson.toJson(responseList);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        PrintWriter printWriter = resp.getWriter();
        printWriter.print(dataJson);
        printWriter.flush();
        printWriter.close();
    }

    private List<Response> doGetOfRole(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        // Lấy toàn bộ danh sách role
        Response = getAllRole();
        responseList.add(Response);

        return responseList;
    }

    private Response getAllRole(){
        Response Response = new Response();
        RoleService roleService = new RoleService();
        List<RoleModel> listRole = roleService.getAllRole();

        if(listRole.size()>0){
            Response.setStatusCode(200);
            Response.setMessage("Lấy danh sách tất cả role thành công");
            Response.setData(listRole);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách tất cả role thất bại");
            Response.setData(null);
        }

        return Response;
    }

    private List<Response> doGetOfAddRole(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        return responseList;
    }



    private List<Response> doGetOfEditRole(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        // Lấy thông tin role để edit
        Response = getRole(roleID);
        responseList.add(Response);

        roleID = null;

        return responseList;
    }

    private Response getRole(String roleId){
        Response Response = new Response();

        if(roleId != null && !"".equals(roleId)){
            RoleService roleService = new RoleService();
            RoleModel role = roleService.getRole(Integer.parseInt(roleId));
            Response.setStatusCode(200);
            Response.setMessage("Lấy thông tin role thành công");
            Response.setData(role);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy thông tin role thất bại");
            Response.setData(null);
        }

        return Response;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response Response = new Response();

        String servletPath = req.getServletPath();
        String function = req.getParameter("function");;
        switch (servletPath){
            case "/api/role":
                Response = doPostOfRole(req,resp,function);
                break;
            case "/api/role-add":
                Response = doPostOfAddRole(req,resp,function);
                break;
            case "/api/role-edit":
                Response = doPostOfEditRole(req,resp,function);
                break;
            default:
                Response.setStatusCode(404);
                Response.setMessage("Không tồn tại URL");
                break;
        }

        Gson gson = new Gson();
        String dataJson = gson.toJson(Response);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        PrintWriter printWriter = resp.getWriter();
        printWriter.print(dataJson);
        printWriter.flush();
        printWriter.close();
    }

    private Response doPostOfRole(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();

        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            case "goToAddRole":
                Response = goToAddRole();
                break;
            case "deleteRole":
                int roleId = Integer.parseInt(req.getParameter("roleID"));
                Response = deleteRole(roleId);
                break;
            case "goToEditRole":
                roleID = req.getParameter("roleID");
                Response = goToEditRole(roleID);
                break;
            default:
                break;
        }

        return Response;
    }

    private Response goToAddRole(){
        Response Response = new Response();

        Response.setStatusCode(200);
        Response.setMessage("Truy cập vào trang thêm thành viên");
        Response.setData("/role-add");

        return Response;
    }

    private Response deleteRole(int roleId){
        Response Response = new Response();
        RoleService roleService = new RoleService();

        if(!roleService.checkExistingOfUserByRoleId(roleId)){
            if(roleService.delelteRole(roleId)){
                Response.setStatusCode(200);
                Response.setMessage("Xóa quyền thành công");
                Response.setData(1);
            }else {
                Response.setStatusCode(400);
                Response.setMessage("Xóa quyền thất bại");
                Response.setData(0);
            }
        } else {
            Response.setStatusCode(400);
            Response.setMessage("Không thể xóa quyền này");
            Response.setData(-1);
        }


        return Response;
    }

    private Response goToEditRole(String roleId){
        Response Response = new Response();

        if(roleId != null && !"".equals(roleId)){
            Response.setStatusCode(200);
            Response.setMessage("Truy cập vào trang chỉnh sửa quyền");
            Response.setData("/role-edit");
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Không tìm thấy ID của quyền muốn chỉnh sửa");
            Response.setData(null);
        }

        return Response;
    }

    private Response doPostOfAddRole(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();

        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            case "addRole":
                Response = addRole(req);
                break;
            default:
                break;
        }

        return Response;
    }

    private Response addRole(HttpServletRequest req){
        Response Response = new Response();

        RoleModel newRole = new RoleModel();
        newRole.setName(req.getParameter("name"));
        newRole.setDescription(req.getParameter("description"));

        if("".equals(newRole.getName())) {
            Response.setStatusCode(400);
            Response.setMessage("Tên quyền chưa được nhập!");
            Response.setData(-1);

            return Response;
        }

        RoleService roleService = new RoleService();
        if(roleService.addRole(newRole)){
            Response.setStatusCode(200);
            Response.setMessage("Thêm quyền thành công");
            Response.setData(1);
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Thêm quyền thất bại");
            Response.setData(0);
        }

        return Response;
    }

    private Response doPostOfEditRole(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();

        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            case "editRole":
                Response = editRole(req);
                break;
            default:
                break;
        }

        return Response;
    }

    private Response editRole(HttpServletRequest req){
        Response Response = new Response();

        String roleId = req.getParameter("id");
        if("0".equals(roleId)){
            Response.setStatusCode(400);
            Response.setMessage("Không tìm thấy dữ liệu quyền");
            Response.setData(-2);

            return Response;
        }

        RoleModel role = new RoleModel();
        role.setId(Integer.parseInt(roleId));
        role.setName(req.getParameter("name"));
        role.setDescription(req.getParameter("description"));

        if("".equals(role.getName())) {
            Response.setStatusCode(400);
            Response.setMessage("Tên quyền trống");
            Response.setData(-1);

            return Response;
        }

        RoleService roleService = new RoleService();
        if(roleService.editRole(role)){
            Response.setStatusCode(200);
            Response.setMessage("Chỉnh sửa quyền thành công");
            Response.setData(1);
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Chỉnh sửa quyền thất bại");
            Response.setData(0);
        }

        return Response;
    }
}
