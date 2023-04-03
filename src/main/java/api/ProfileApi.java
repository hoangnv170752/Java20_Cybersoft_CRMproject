package api;

import com.google.gson.Gson;
import filter.AuthenHandling;
import model.StatusList;
import model.StatusModel;
import model.TaskModel;
import model.UserModel;
import payload.Response;
import service.HomeService;
import service.ProfileService;
import service.StatusService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProfileApi",urlPatterns = {"/api/profile","/api/profile-task-update"})
public class ProfileApi extends HttpServlet {
    UserModel user = new UserModel();
    String taskID = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Response> responseList = new ArrayList<>();
        Response Response = new Response();

        String servletPath = req.getServletPath();
        switch (servletPath){
            case "/api/profile":
                responseList = doGetOfProfile(req);
                break;
            case "/api/profile-task-update":
                responseList = doGetOfTaskUpdate(req);
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

    private List<Response> doGetOfProfile(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        Response = getTaskStatics();
        responseList.add(Response);
        Response = getAllTaskOfUser();
        responseList.add(Response);

        return responseList;
    }
    private Response getTaskStatics(){
        Response Response = new Response();

        int[] statusList = {0,0,0};   // 0-unbegun qty, 1-doing qty, 2-finish qty

        if(user != null){
            HomeService homeService = new HomeService();
            List<Integer> taskStatusList = homeService.getTaskStatus(user.getId());

            for (int i: taskStatusList) {
                if(i == StatusList.UNBEGUN.getValue()){
                    statusList[0]++;
                } else if(i == StatusList.DOING.getValue()){
                    statusList[1]++;
                } else if(i == StatusList.FINISH.getValue()){
                    statusList[2]++;
                }
            }
            if(taskStatusList.size()>0) {
                statusList[0] = Math.round(statusList[0] * 100.0f / taskStatusList.size());
                statusList[1] = Math.round(statusList[1] * 100.0f / taskStatusList.size());
                statusList[2] = Math.round(statusList[2] * 100.0f / taskStatusList.size());
            }
            Response.setStatusCode(200);
            Response.setMessage("Lấy danh sách thống kê công việc của người dùng thành công");
            Response.setData(statusList);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách thống kê công việc của người dùng thất bại");
            Response.setData(null);
        }
        return Response;
    }
    private Response getAllTaskOfUser(){
        Response Response = new Response();
        if(user != null){
            Response.setStatusCode(200);
            ProfileService profileService = new ProfileService();
            List<TaskModel> listTask = profileService.getAllTaskOfUser(user.getId());
            Response.setMessage("Lấy danh sách công việc thành công");
            Response.setData(listTask);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách công việc thất bại");
            Response.setData(null);
        }

        return Response;
    }
    private List<Response> doGetOfTaskUpdate(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        Response = getTask(taskID);
        responseList.add(Response);
        Response = getAllStatus();
        responseList.add(Response);

        taskID = null;

        return responseList;
    }

    private Response getAllStatus(){
        Response Response = new Response();
        StatusService statusService = new StatusService();
        List<StatusModel> list = statusService.getAllStatus();

        if(list.size()>0){
            Response.setStatusCode(200);
            Response.setMessage("Lấy danh sách trạng thái công việc thành công");
            Response.setData(list);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách trạng thái công việc thất bại");
            Response.setData(null);
        }

        return Response;
    }

    private Response getTask(String taskId){
        Response Response = new Response();

        if(taskId != null && !"".equals(taskId)){
            Response.setStatusCode(200);
            ProfileService profileService = new ProfileService();
            TaskModel task = profileService.getTaskById(Integer.parseInt(taskId));
            Response.setMessage("Lấy dữ liệu công việc thành công");
            Response.setData(task);
        }else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy dữ liệu công việc thất bại");
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
            case "/api/profile":
                Response = doPostOfProfile(req,resp,function);
                break;
            case "/api/profile-task-update":
                Response = doPostOfTaskUpdate(req,resp,function);
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

    private Response doPostOfProfile(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();
        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            case "goToTaskUpdate":
                taskID = req.getParameter("taskID");
                Response = goToTaskUpdate(taskID);
                break;
            default:
                break;
        }
        return Response;
    }

    private Response goToTaskUpdate(String taskId){
        Response Response = new Response();

        if(taskId != null && !"".equals(taskId) ){
            Response.setStatusCode(200);
            Response.setMessage("Truy cập vào trang cập nhập công việc");
            Response.setData("/profile-task-update");
        } else{
            Response.setStatusCode(400);
            Response.setMessage("Không tìm thấy ID của công việc muốn cập nhập");
            Response.setData(null);
        }
        return Response;
    }

    private Response doPostOfTaskUpdate(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();

        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            case "saveStatus":
                Response = updateTaskStatus(req);
                break;
            default:
                break;
        }
        return Response;
    }

    private Response updateTaskStatus(HttpServletRequest req){
        Response Response = new Response();

        String taskId = req.getParameter("taskId");
        String statusID = req.getParameter("statusId");

        if("0".equals(taskId)){
            Response.setStatusCode(400);
            Response.setMessage("Không tìm thấy dữ liệu công việc");
            Response.setData(-1);

            return Response;
        }

        ProfileService profileService = new ProfileService();
        boolean result = profileService.updateStatusTask(Integer.parseInt(taskId),Integer.parseInt(statusID));

        if(result){
            Response.setStatusCode(200);
            Response.setMessage("Cập nhập trạng thái công việc thành công");
            Response.setData(1);
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Cập nhập trạng thái công việc thất bại");
            Response.setData(0);
        }
        return Response;
    }
}
