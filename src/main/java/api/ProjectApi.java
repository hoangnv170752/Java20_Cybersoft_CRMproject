package api;

import com.google.gson.Gson;
import filter.AuthenHandling;
import filter.AuthList;
import model.*;
import payload.Response;
import service.ProjectService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProjectApi",urlPatterns = {"/api/project","/api/project-add","/api/project-edit","/api/project-detail"})
public class ProjectApi extends HttpServlet {
    UserModel user = new UserModel();
    String projectID = null;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Response> responseList = new ArrayList<>();
        Response Response = new Response();

        String servletPath = req.getServletPath();
        switch (servletPath){
            case "/api/project":
                responseList = doGetOfProject(req);
                break;
            case "/api/project-add":
                responseList = doGetOfAddProject(req);
                break;
            case "/api/project-edit":
                responseList = doGetOfEditProject(req);
                break;
            case "/api/project-detail":
                responseList = doGetOfProjectDetail(req);
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

    private List<Response> doGetOfProject(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        // Lấy toàn bộ danh sách user
        Response = getAllProject();
        responseList.add(Response);

        return responseList;
    }

    private Response getAllProject(){
        Response Response = new Response();
        ProjectService projectService = new ProjectService();
        List<ProjectModel> listProject;

        if(user.getRole().getId() == AuthList.ADMIN.getValue()){
            listProject = projectService.getAllProject();
        } else {
            listProject = projectService.getAllProjectByLeaderId(user.getId());
        }

        if(listProject.size()>0){
            Response.setStatusCode(200);
            Response.setMessage("Lấy danh sách dự án thành công");
            Response.setData(listProject);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách dự án thất bại");
            Response.setData(null);
        }

        return Response;
    }

    private List<Response> doGetOfAddProject(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        // Lấy danh sách leader
        Response = getLeaderList();
        responseList.add(Response);

        return responseList;
    }

    private Response getLeaderList(){
        Response Response = new Response();
        UserService userService = new UserService();
        List<UserModel> userList = userService.getLeaderList();

        if(userList.size()>0){
            Response.setStatusCode(200);
            Response.setMessage("Lấy danh sách quản lý thành công");
            Response.setData(userList);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách quản lý thất bại");
            Response.setData(null);
        }

        return Response;
    }

    private List<Response> doGetOfEditProject(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user đang đăng nhập
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        // Lấy thông tin dự án để edit
        Response = getProject(projectID,false);
        responseList.add(Response);

        // Lấy danh sách leader
        Response = getLeaderList();
        responseList.add(Response);

        // Quản lý việc chỉnh sửa người quản lý của dự án
        Response = manageLeaderEdit();
        responseList.add(Response);

        projectID = null;
        return responseList;
    }

    private Response getProject(String projectId, boolean changeDateFormat){
        Response Response = new Response();

        if(projectId != null && !"".equals(projectId)){
            ProjectService projectService = new ProjectService();
            ProjectModel project = projectService.getProject(Integer.parseInt(projectId),changeDateFormat);
            Response.setStatusCode(200);
            Response.setMessage("Lấy thông tin dự án thành công");
            Response.setData(project);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy thông tin dự án thất bại");
            Response.setData(null);
        }

        return Response;
    }

    private Response manageLeaderEdit(){
        Response Response = new Response();

        if(user.getRole().getId() == AuthList.ADMIN.getValue()){
            Response.setStatusCode(200);
            Response.setMessage("Có quyền thay đổi người quản lý của dự án");
            Response.setData(true);
        } else {
            Response.setStatusCode(200);
            Response.setMessage("Không có quyền thay đổi người quản lý của dự án");
            Response.setData(false);
        }

        return Response;
    }

    private List<Response> doGetOfProjectDetail(HttpServletRequest req){
        List<Response> responseList = new ArrayList<>();

        // Lấy thông tin user đang đăng nhập
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        // Lấy thông tin dự án để hiển thị
        Response = getProject(projectID,true);
        responseList.add(Response);

        // Lấy thông tin thống kê của dự án
        Response = getTaskStatics(projectID);
        responseList.add(Response);

        // Lấy thông tin công việc của dự án
        Response = getAllTaskOfProject(projectID);
        responseList.add(Response);

        projectID = null;
        return responseList;
    }

    private Response getTaskStatics(String projectId){
        Response Response = new Response();
        int[] staticsList = {0,0,0};   //1-unbegun num, 2-doing, 3-finish

        if(projectId != null && !"".equals(projectId)){
            ProjectService projectService = new ProjectService();
            List<Integer> taskStatusList = projectService.getTaskStatics(Integer.parseInt(projectId));
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
            Response.setMessage("Lấy danh sách thống kê công việc của dự án thành công");
            Response.setData(staticsList);
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách thống công việc của dự án thất bại");
            Response.setData(null);
        }

        return Response;
    }

    private Response getAllTaskOfProject(String projectId){
        Response Response = new Response();

        if(projectId != null && !"".equals(projectId)){
            ProjectService projectService = new ProjectService();
            List<TaskModel> taskList = projectService.getTaskListOfProject(Integer.parseInt(projectId));
            Response.setStatusCode(200);
            Response.setMessage("Lấy danh sách công việc của dự án thành công");
            Response.setData(arrangeTaskByMember(taskList));
        } else {
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách công việc của dự án thất bại");
            Response.setData(null);
        }
        return Response;
    }

    private List<MemberTaskModel> arrangeTaskByMember(List<TaskModel> inputList){
        List<MemberTaskModel> resultList = new ArrayList<>();

        if(inputList.size() > 0){
            for(int i=0;i<inputList.size();i++){
                boolean isDuplicate = false;
                if(i != 0){
                    for(int k=0;k<resultList.size();k++){
                        if(inputList.get(i).getUser().getId() == resultList.get(k).getUser().getId()){
                            resultList.get(k).getTaskList().add(inputList.get(i));
                            isDuplicate = true;
                            break;
                        }
                    }
                }
                if(!isDuplicate){
                    MemberTaskModel taskByMember = new MemberTaskModel();

                    UserModel user = inputList.get(i).getUser();
                    taskByMember.setUser(user);

                    List<TaskModel> taskList = new ArrayList<>();
                    taskList.add(inputList.get(i));
                    taskByMember.setTaskList(taskList);

                    resultList.add(taskByMember);
                }
            }
        } else {
            return null;
        }

        return resultList;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response Response = new Response();

        String servletPath = req.getServletPath();
        String function = req.getParameter("function");;
        switch (servletPath){
            case "/api/project":
                Response = doPostOfProject(req,resp,function);
                break;
            case "/api/project-add":
                Response = doPostOfAddProject(req,resp,function);
                break;
            case "/api/project-edit":
                Response = doPostOfEditProject(req,resp,function);
                break;
            case "/api/project-detail":
                Response = doPostOfProjectDetail(req,resp,function);
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

    private Response doPostOfProject(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();

        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            case "goToAddProject":
                Response = goToAddProject();
                break;
            case "deleteProject":
                int projectId = Integer.parseInt(req.getParameter("projectID"));
                Response = deleteProject(projectId);
                break;
            case "goToEditProject":
                projectID = req.getParameter("projectID");
                Response = goToEditProject(projectID);
                break;
            case "goToProjectDetail":
                projectID = req.getParameter("projectID") ;
                Response = goToProjectDetail(projectID);
                break;
            default:
                break;
        }

        return Response;
    }

    private Response goToAddProject(){
        Response Response = new Response();

        Response.setStatusCode(200);
        Response.setMessage("Truy cập vào trang thêm dự án");
        Response.setData("/project-add");

        return Response;
    }

    private Response deleteProject(int projectId){
        Response Response = new Response();
        ProjectService projectService = new ProjectService();

        if(!projectService.checkExistingOfTaskByProjectId(projectId)){
            if(projectService.deleteProject(projectId)){
                Response.setStatusCode(200);
                Response.setMessage("Xóa dự án thành công");
                Response.setData(1);
            }else {
                Response.setStatusCode(400);
                Response.setMessage("Xóa dự án thất bại");
                Response.setData(0);
            }
        } else {
            Response.setStatusCode(400);
            Response.setMessage("Không thể xóa dự án này");
            Response.setData(-1);
        }

        return Response;
    }

    private Response goToEditProject(String projectId){
        Response Response = new Response();

        if(projectId != null && !"".equals(projectId)){
            Response.setStatusCode(200);
            Response.setMessage("Truy cập vào trang chỉnh sửa dự án");
            Response.setData("/project-edit");
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Không tìm thấy ID của dự án muốn chỉnh sửa");
            Response.setData(null);
        }

        return Response;
    }

    private Response goToProjectDetail(String projectId){
        Response Response = new Response();

        if(projectId != null && !"".equals(projectId)){
            Response.setStatusCode(200);
            Response.setMessage("Truy cập vào trang chi tiết dự án");
            Response.setData("/project-detail");
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Không tìm thấy ID của dự án muốn xem");
            Response.setData(null);
        }
        return Response;
    }
    private Response doPostOfAddProject(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();
        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            case "addProject":
                Response = addProject(req);
                break;
            default:
                break;
        }
        return Response;
    }
    private Response addProject(HttpServletRequest req){
        Response Response = new Response();

        ProjectModel newProject = new ProjectModel();
        newProject.setName(req.getParameter("name"));
        String leaderId = req.getParameter("leaderId");
        newProject.setStart_date(req.getParameter("start-date"));
        newProject.setEnd_date(req.getParameter("end-date"));

        if("".equals(newProject.getName()) || "".equals(newProject.getStart_date()) || "".equals(newProject.getEnd_date())
                || "".equals(leaderId) || leaderId == null) {
            Response.setStatusCode(400);
            Response.setMessage("Dữ liệu chưa được nhập đủ");
            Response.setData(-1);
            return Response;
        }

        UserModel user = new UserModel();
        user.setId(Integer.parseInt(leaderId));
        newProject.setLeader(user);

        ProjectService projectService = new ProjectService();
        if(projectService.addProject(newProject)){
            Response.setStatusCode(200);
            Response.setMessage("Thêm dự án thành công");
            Response.setData(1);
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Thêm dự án thất bại");
            Response.setData(0);
        }
        return Response;
    }
    private Response doPostOfEditProject(HttpServletRequest req, HttpServletResponse resp, String function){
        Response Response = new Response();
        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            case "editProject":
                Response = editProject(req);
                break;
            default:
                break;
        }
        return Response;
    }

    private Response editProject(HttpServletRequest req){
        Response Response = new Response();

        String projectId = req.getParameter("id");
        if("0".equals(projectId)){
            Response.setStatusCode(400);
            Response.setMessage("Không tìm thấy dữ liệu dự án");
            Response.setData(-2);

            return Response;
        }

        ProjectModel project = new ProjectModel();
        project.setId(Integer.parseInt(req.getParameter("id")));
        project.setName(req.getParameter("name"));
        project.setStart_date(req.getParameter("start-date"));
        project.setEnd_date(req.getParameter("end-date"));
        String leaderId = req.getParameter("leaderId");

        if("".equals(project.getName()) || "".equals(project.getStart_date())
                || "".equals(project.getEnd_date()) || "".equals(leaderId) || leaderId == null) {
            Response.setStatusCode(400);
            Response.setMessage("Chưa nhập đủ dữ liệu");
            Response.setData(-1);

            return Response;
        }
        UserModel user = new UserModel();
        user.setId(Integer.parseInt(leaderId));
        project.setLeader(user);

        ProjectService projectService = new ProjectService();
        if(projectService.editProject(project)){
            Response.setStatusCode(200);
            Response.setMessage("Chỉnh sửa thông tin dự án thành công");
            Response.setData(1);
        }else {
            Response.setStatusCode(400);
            Response.setMessage("Chỉnh sửa thông tin dự án thất bại");
            Response.setData(0);
        }

        return Response;
    }
    private Response doPostOfProjectDetail(HttpServletRequest req, HttpServletResponse resp, String function){
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
