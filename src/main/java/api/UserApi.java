package api;

import com.google.gson.Gson;
import model.StatusList;
import model.TaskModel;
import service.HomeService;
import filter.AuthList;
import model.RoleModel;
import model.UserModel;
import payload.Response;
import service.UserService;
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

@WebServlet(name = "UserApi",urlPatterns = {"/api/user","/api/user-detail","/api/user-add","/api/user-edit"})
public class UserApi extends HttpServlet {
    UserModel user = new UserModel();
    String memberID = null;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Response> responseList = new ArrayList<>();
        Response Response = new Response();

        String servletPath = req.getServletPath();
        switch (servletPath){
            case "/api/user":
                responseList = doGetOfUser(req);
                break;
            case "/api/user-add":
                responseList = doGetOfAddUser(req);
                break;
            case "/api/user-edit":
                responseList = doGetOfEditUser(req);
                break;
            case "/api/user-detail":
                responseList = doGetOfUserDetail(req);
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

    private List<Response> doGetOfUser(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        // Lấy toàn bộ danh sách user
        Response = getAllUser();
        responseList.add(Response);

        return responseList;
    }

    private Response getAllUser(){
        Response Response = new Response();
        UserService userService = new UserService();
        List<UserModel> listUser = userService.getAllUser();

        if(listUser.size()>0){
            Response.setStatusCode(200);
            Response.setMessage("Lấy danh sách thành viên thành công");
            Response.setData(listUser);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách thành viên thất bại");
            Response.setData(null);
        }

        return Response;
    }

    private List<Response> doGetOfAddUser(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        // Lấy danh sách role
        Response = getRoleList();
        responseList.add(Response);

        return responseList;
    }

    private Response getRoleList(){
        Response Response = new Response();
        UserService userService = new UserService();

        List<RoleModel> listRole = userService.getAllRole();

        if(listRole.size()>0){
            Response.setStatusCode(200);
            Response.setMessage("Lấy danh sách quyền thành công");
            Response.setData(listRole);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách quyền thất bại");
            Response.setData(null);
        }

        return Response;
    }

    private List<Response> doGetOfEditUser(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user đang đăng nhập
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        // Lấy thông tin user để edit
        Response = getMember(memberID);
        responseList.add(Response);

        // Lấy danh sách role để edit
        Response = getRoleList();
        responseList.add(Response);

        memberID = null;
        return responseList;
    }

    private Response getMember(String memberId){
        Response Response = new Response();

        if(memberId != null && !"".equals(memberId)){
            UserService userService = new UserService();
            UserModel user = userService.getMember(Integer.parseInt(memberId));
            Response.setStatusCode(200);
            Response.setMessage("Lấy thông tin thành viên thành công");
            Response.setData(user);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy thông tin thành viên thất bại");
            Response.setData(null);
        }

        return Response;
    }

    private List<Response> doGetOfUserDetail(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user đang đăng nhập
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        // Lấy thông tin chi tiết của thành viên
        Response = getMember(memberID);
        responseList.add(Response);

        // Thống kê task của thành viên
        Response = getTaskStatics(memberID);
        responseList.add(Response);

        // Lấy danh sách chi tiết task của thành viên
        Response = getTaskList(memberID);
        responseList.add(Response);

        memberID = null;
        return responseList;
    }

    private Response getTaskStatics(String memberId){
        Response Response = new Response();
        int[] staticsList = {0,0,0};   //1-unbegun num, 2-doing, 3-finish

        if(memberId != null && !"".equals(memberId)){
            HomeService homeService = new HomeService();
            List<Integer> taskStatusList = homeService.getTaskStatus(Integer.parseInt(memberId));
            if(taskStatusList.size()>0) {
                for (int i: taskStatusList) {
                    if(i == StatusList.UNBEGUN.getValue()){
                        staticsList[0]++;
                    } else if(i == StatusList.DOING.getValue()){
                        staticsList[1]++;
                    } else if(i == StatusList.FINISH.getValue()){
                        staticsList[2]++;
                    }
                }
                staticsList[0] = Math.round(staticsList[0] * 100.0f / taskStatusList.size());
                staticsList[1] = Math.round(staticsList[1] * 100.0f / taskStatusList.size());
                staticsList[2] = Math.round(staticsList[2] * 100.0f / taskStatusList.size());
            }
            Response.setStatusCode(200);
            Response.setMessage("Lấy danh sách thống kê công việc của thành viên thành công");
            Response.setData(staticsList);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách thống kê công việc của thành viên thất bại");
            Response.setData(null);
        }

        return Response;
    }

    private Response getTaskList(String memberId){
        Response Response = new Response();

        if(memberId != null && !"".equals(memberId)){
            UserService userService = new UserService();
            List<TaskModel> taskList = userService.getTaskListOfMember(Integer.parseInt(memberId));
            Response.setStatusCode(200);
            Response.setMessage("Lấy danh sách công việc thành công");
            Response.setData(taskList);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách công việc thất bại");
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
            case "/api/user":
                Response = doPostOfUser(req,resp,function);
                break;
            case "/api/user-add":
                Response = doPostOfAddUser(req,resp,function);
                break;
            case "/api/user-edit":
                Response = doPostOfEditUser(req,resp,function);
                break;
            case "/api/user-detail":
                Response = doPostOfUserDetail(req,resp,function);
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

    private Response doPostOfUser(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();

        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            case "goToAddUser":
                Response = goToAddUser();
                break;
            case "deleteUser":
                int memberId = Integer.parseInt(req.getParameter("memberID"));
                Response = deleteUser(memberId);
                break;
            case "goToEditUser":
                memberID = req.getParameter("memberID");
                Response = goToEditUser(memberID);
                break;
            case "goToUserDetail":
                memberID = req.getParameter("memberID") ;
                Response = goToUserDetail(memberID);
                break;
            default:
                break;
        }

        return Response;
    }

    private Response goToAddUser(){
        Response Response = new Response();

        Response.setStatusCode(200);
        Response.setMessage("Truy cập vào trang thêm thành viên");
        Response.setData("/user-add");

        return Response;
    }

    private Response deleteUser(int memberId){
        Response Response = new Response();
        UserService userService = new UserService();

        if(user.getRole().getId() == AuthList.ADMIN.getValue()){
            if(userService.checkExistingOfTaskByUserId(memberId)){
                Response.setStatusCode(400);
                Response.setMessage("Không thể xóa thành viên này");
                Response.setData(-1);
            } else if(userService.checkExistingOfProjectByLeaderId(memberId)){
                Response.setStatusCode(400);
                Response.setMessage("Không thể xóa thành viên này");
                Response.setData(-2);
            }
            else {
                if(userService.deleteUser(memberId)){
                    Response.setStatusCode(200);
                    Response.setMessage("Xóa thành viên thành công");
                    Response.setData(1);
                }else {
                    Response.setStatusCode(400);
                    Response.setMessage("Xóa thành viên thất bại");
                    Response.setData(0);
                }
            }
        } else {
            Response.setStatusCode(403);
            Response.setMessage("Người dùng không có quyền xóa");
            Response.setData(403);
        }

        return Response;
    }

    private Response goToEditUser(String memberId){
        Response Response = new Response();

        if(memberId != null && !"".equals(memberId)){
            Response.setStatusCode(200);
            Response.setMessage("Truy cập vào trang chỉnh sửa thành viên");
            Response.setData("/user-edit");
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Không tìm thấy ID của thành viên muốn chỉnh sửa");
            Response.setData(null);
        }

        return Response;
    }

    private Response goToUserDetail(String memberId){
        Response Response = new Response();

        if(memberId != null && !"".equals(memberId)){
            Response.setStatusCode(200);
            Response.setMessage("Truy cập vào trang chi tiết thành viên");
            Response.setData("/user-detail");
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Không tìm thấy ID của thành viên muốn xem");
            Response.setData(null);
        }

        return Response;
    }

    private Response doPostOfAddUser(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();

        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            case "addUser":
                Response = addUser(req);
                break;
            default:
                break;
        }

        return Response;
    }

    private Response addUser(HttpServletRequest req){
        Response Response = new Response();

        UserModel newUser = new UserModel();
        newUser.setEmail(req.getParameter("email"));
        newUser.setPassword(req.getParameter("password"));
        newUser.setFullname(req.getParameter("fullname"));
        newUser.setAvatar(req.getParameter("avatar"));
        RoleModel role = new RoleModel();
        role.setId(Integer.parseInt(req.getParameter("role-id")));
        newUser.setRole(role);

        String confirmPassword = req.getParameter("confirm-password");

        if(!newUser.getPassword().equals(confirmPassword)){
            Response.setStatusCode(400);
            Response.setMessage("Xác nhận mật khẩu không khớp");
            Response.setData(-1);

            return Response;
        } else if("".equals(newUser.getEmail())) {
            Response.setStatusCode(400);
            Response.setMessage("Email chưa được nhập!");
            Response.setData(-2);

            return Response;
        }

        UserService userService = new UserService();
        if(userService.addUser(newUser)){
            Response.setStatusCode(200);
            Response.setMessage("Thêm thành viên thành công");
            Response.setData(1);
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Thêm thành viên thất bại");
            Response.setData(0);
        }

        return Response;
    }

    private Response doPostOfEditUser(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();

        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            case "editUser":
                Response = editMember(req);
                break;
            default:
                break;
        }

        return Response;
    }

    private Response editMember(HttpServletRequest req){
        Response Response = new Response();

        String userId = req.getParameter("id");
        if("0".equals(userId)){
            Response.setStatusCode(400);
            Response.setMessage("Không tìm thấy dữ liệu thành viên");
            Response.setData(-3);

            return Response;
        }

        UserModel user = new UserModel();
        user.setId(Integer.parseInt(userId));
        user.setEmail(req.getParameter("email"));
        user.setPassword(req.getParameter("password"));
        user.setFullname(req.getParameter("fullname"));
        user.setAvatar(req.getParameter("avatar"));
        RoleModel role = new RoleModel();
        role.setId(Integer.parseInt(req.getParameter("role-id")));
        user.setRole(role);

        String confirmPassword = req.getParameter("confirm-password");

        if(!user.getPassword().equals(confirmPassword)){
            Response.setStatusCode(400);
            Response.setMessage("Xác nhận mật khẩu không khớp");
            Response.setData(-1);

            return Response;
        } else if("".equals(user.getEmail())) {
            Response.setStatusCode(400);
            Response.setMessage("Email bị bỏ trống");
            Response.setData(-2);

            return Response;
        }

        UserService userService = new UserService();
        if(userService.updateUser(user)){
            Response.setStatusCode(200);
            Response.setMessage("Chỉnh sửa thông tin thành viên thành công");
            Response.setData(1);
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Chỉnh sửa thông tin thành viên thất bại");
            Response.setData(0);
        }

        return Response;
    }

    private Response doPostOfUserDetail(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();

        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            default:
                break;
        }

        return Response;
    }

}
