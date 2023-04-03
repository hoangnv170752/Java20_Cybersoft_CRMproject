package api;

import com.google.gson.Gson;
import filter.AuthenHandling;
import filter.AuthList;
import model.*;
import payload.Response;
import service.StatusService;
import service.TaskService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "TaskApi",urlPatterns = {"/api/task","/api/task-add","/api/task-edit"})
public class TaskApi extends HttpServlet {
    UserModel user = new UserModel();
    String taskID = null;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Response> responseList = new ArrayList<>();
        Response Response = new Response();

        String servletPath = req.getServletPath();
        switch (servletPath){
            case "/api/task":
                responseList = doGetOfTask(req);
                break;
            case "/api/task-add":
                responseList = doGetOfAddTask(req);
                break;
            case "/api/task-edit":
                responseList = doGetOfEditTask(req);
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

    private List<Response> doGetOfTask(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        // Lấy toàn bộ danh sách task
        Response = getAllTask();
        responseList.add(Response);

        return responseList;
    }

    private Response getAllTask(){
        Response Response = new Response();
        TaskService taskService = new TaskService();
        List<TaskModel> taskList;

        if(user.getRole().getId() == AuthList.ADMIN.getValue()){
            taskList = taskService.getAllTask();
        } else {
            taskList = taskService.getAllTaskByLeaderId(user.getId());
        }

        if(taskList.size()>0){
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

    private List<Response> doGetOfAddTask(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        // Lấy danh sách project
        Response = getProjectList();
        responseList.add(Response);

        // Lấy danh sách user
        Response = getMemberList();
        responseList.add(Response);

        return responseList;
    }

    private Response getProjectList(){
        Response Response = new Response();
        TaskService taskService = new TaskService();
        List<ProjectModel> projectList = taskService.getProjectList();

        if(projectList.size()>0){
            Response.setStatusCode(200);
            Response.setMessage("Lấy danh sách dự án trong thêm/chỉnh sửa công việc thành công");
            Response.setData(projectList);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách dự án trong thêm/chỉnh sửa công việc thất bại");
            Response.setData(null);
        }

        return Response;
    }

    private Response getMemberList(){
        Response Response = new Response();
        TaskService taskService = new TaskService();
        List<UserModel> userList = taskService.getMemberList();

        if(userList.size()>0){
            Response.setStatusCode(200);
            Response.setMessage("Lấy danh sách thành viên trong thêm/chỉnh sửa công việc thành công");
            Response.setData(userList);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách thành viên trong thêm/chỉnh sửa công việc thất bại");
            Response.setData(null);
        }

        return Response;
    }

    private List<Response> doGetOfEditTask(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        // Lấy thông tin công việc
        Response = getTaskInfo(taskID);
        responseList.add(Response);

        // Lấy danh sách project
        Response = getProjectList();
        responseList.add(Response);

        // Lấy danh sách user
        Response = getMemberList();
        responseList.add(Response);

        // Lấy danh sách status
        Response = getStatusList();
        responseList.add(Response);

        taskID = null;
        return responseList;
    }

    private Response getTaskInfo(String taskId){
        Response Response = new Response();

        if(taskId != null && !"".equals(taskId)){
            TaskService taskService = new TaskService();
            TaskModel task = taskService.getTaskById(Integer.parseInt(taskId));
            Response.setStatusCode(200);
            Response.setMessage("Lấy thông tin công việc thành công");
            Response.setData(task);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy thông tin công việc thất bại");
            Response.setData(null);
        }

        return Response;
    }

    private Response getStatusList(){
        Response Response = new Response();
        StatusService statusService = new StatusService();
        List<StatusModel> statusList = statusService.getAllStatus();

        if(statusList.size()>0){
            Response.setStatusCode(200);
            Response.setMessage("Lấy danh sách trạng thái trong chỉnh sửa công việc thành công");
            Response.setData(statusList);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách trạng thái trong chỉnh sửa công việc thất bại");
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
            case "/api/task":
                Response = doPostOfTask(req,resp,function);
                break;
            case "/api/task-add":
                Response = doPostOfAddTask(req,resp,function);
                break;
            case "/api/task-edit":
                Response = doPostOfEditTask(req,resp,function);
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

    private Response doPostOfTask(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();

        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            case "goToAddTask":
                Response = goToAddTask();
                break;
            case "deleteTask":
                int taskId = Integer.parseInt(req.getParameter("taskID"));
                Response = deleteTask(taskId);
                break;
            case "goToEditTask":
                taskID = req.getParameter("taskID");
                Response = goToEditTask(taskID);
                break;
            default:
                break;
        }

        return Response;
    }

    private Response goToAddTask(){
        Response Response = new Response();

        Response.setStatusCode(200);
        Response.setMessage("Truy cập vào trang thêm dự án");
        Response.setData("/task-add");

        return Response;
    }

    private Response deleteTask(int taskId){
        Response Response = new Response();
        TaskService taskService = new TaskService();

        if(taskService.deleteTask(taskId)){
            Response.setStatusCode(200);
            Response.setMessage("Xóa công việc thành công");
            Response.setData(true);
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Xóa công việc thất bại");
            Response.setData(false);
        }

        return Response;
    }

    private Response goToEditTask(String taskId) {
        Response Response = new Response();

        if (taskId != null && !"".equals(taskId)) {
            Response.setStatusCode(200);
            Response.setMessage("Truy cập vào trang chỉnh sửa công việc");
            Response.setData("/task-edit");
        } else {
            Response.setStatusCode(400);
            Response.setMessage("Không tìm thấy ID của công việc muốn chỉnh sửa");
            Response.setData(null);
        }

        return Response;
    }

    private Response doPostOfAddTask(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();

        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            case "addTask":
                Response = addTask(req);
                break;
            default:
                break;
        }

        return Response;
    }

    private Response addTask(HttpServletRequest req){
        Response Response = new Response();

        TaskModel newTask = checkMissingInputAdd(req);
        if(newTask == null) {
            Response.setStatusCode(400);
            Response.setMessage("Dữ liệu chưa được nhập đủ");
            Response.setData(-1);

            return Response;
        }

        TaskService taskService = new TaskService();
        if(taskService.addTask(newTask)){
            Response.setStatusCode(200);
            Response.setMessage("Thêm công việc thành công");
            Response.setData(1);
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Thêm công việc thất bại");
            Response.setData(0);
        }

        return Response;
    }

    private TaskModel checkMissingInputAdd(HttpServletRequest req){
        boolean missingInput = false;

        TaskModel task = new TaskModel();
        task.setName(req.getParameter("taskName"));
        task.setStart_date(req.getParameter("start-date"));
        task.setEnd_date(req.getParameter("end-date"));
        if("".equals(task.getName()) || "".equals(task.getStart_date())
                || "".equals(task.getEnd_date())){
            missingInput = true;
        }

        String projectId = req.getParameter("projectID");
        if("".equals(projectId) || projectId == null){
            missingInput = true;
        } else {
            ProjectModel project = new ProjectModel();
            project.setId(Integer.parseInt(projectId));
            task.setProject(project);
        }

        String memberId = req.getParameter("memberID");
        if("".equals(memberId) || memberId == null){
            missingInput = true;
        } else {
            UserModel member = new UserModel();
            member.setId(Integer.parseInt(memberId));
            task.setUser(member);
        }

        StatusModel status = new StatusModel();
        status.setId(StatusList.UNBEGUN.getValue());
        task.setStatus(status);

        if(missingInput){
            return null;
        } else {
            return task;
        }
    }

    private Response doPostOfEditTask(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();

        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            case "editTask":
                Response = editTask(req);
                break;
            default:
                break;
        }

        return Response;
    }

    private Response editTask(HttpServletRequest req){
        Response Response = new Response();

        String taskId = req.getParameter("id");
        if("0".equals(taskId)){
            Response.setStatusCode(400);
            Response.setMessage("Không tìm thấy dữ liệu công việc");
            Response.setData(-2);

            return Response;
        }

        TaskModel task = checkMissingInputEdit(req);
        if(task == null) {
            Response.setStatusCode(400);
            Response.setMessage("Dữ liệu chưa được nhập đủ");
            Response.setData(-1);

            return Response;
        }

        TaskService taskService = new TaskService();
        if(taskService.editTask(task)){
            Response.setStatusCode(200);
            Response.setMessage("Chỉnh sửa công việc thành công");
            Response.setData(1);
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Chỉnh sửa công việc thất bại");
            Response.setData(0);
        }

        return Response;
    }

    private TaskModel checkMissingInputEdit(HttpServletRequest req){
        boolean missingInput = false;

        TaskModel task = new TaskModel();
        task.setId(Integer.parseInt(req.getParameter("id")));
        task.setName(req.getParameter("taskName"));
        task.setStart_date(req.getParameter("start-date"));
        task.setEnd_date(req.getParameter("end-date"));
        if("".equals(task.getName()) || "".equals(task.getStart_date())
                || "".equals(task.getEnd_date())){
            missingInput = true;
        }

        String projectId = req.getParameter("projectID");
        if("".equals(projectId) || projectId == null){
            missingInput = true;
        } else {
            ProjectModel project = new ProjectModel();
            project.setId(Integer.parseInt(projectId));
            task.setProject(project);
        }

        String memberId = req.getParameter("memberID");
        if("".equals(memberId) || memberId == null){
            missingInput = true;
        } else {
            UserModel member = new UserModel();
            member.setId(Integer.parseInt(memberId));
            task.setUser(member);
        }

        String statusId = req.getParameter("statusID");
        if("".equals(statusId) || statusId == null){
            missingInput = true;
        } else {
            StatusModel status = new StatusModel();
            status.setId(Integer.parseInt(statusId));
            task.setStatus(status);
        }

        if(missingInput){
            return null;
        } else {
            return task;
        }
    }
}
