package api;

import com.google.gson.Gson;
import filter.AuthenHandling;
import model.StatusList;
import model.UserModel;
import payload.Response;
import service.HomeService;
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

@WebServlet(name = "HomeApi",urlPatterns = {"/api/home"})
public class HomeApi extends HttpServlet {
    UserModel user = new UserModel();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Response> responseList = new ArrayList<>();
        AuthenHandling auth = new AuthenHandling();
        Response Response = auth.getUserInfo(req);
        responseList.add(Response);
        user = (UserModel) Response.getData();

        Response = getTaskStatus();
        responseList.add(Response);

        Gson gson = new Gson();
        String dataJson = gson.toJson(responseList);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        PrintWriter printWriter = resp.getWriter();
        printWriter.print(dataJson);
        printWriter.flush();
        printWriter.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response Response = new Response();
        String function = req.getParameter("function");
        switch (function){
            case "logout":
                AuthenHandling AuthenHandling = new AuthenHandling();
                Response = AuthenHandling.logOut(req,resp);
                break;
            default:
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

    private Response getTaskStatus(){
        Response Response = new Response();

        int[] statusList = {0,0,0,0};   // 0-task qty sum, 1-unbegun qty, 2-doing qty, 3-finish qty

        if(user != null){
            HomeService homeService = new HomeService();
            List<Integer> taskStatusList = homeService.getTaskStatus(user.getId());
            if(taskStatusList.size()>0){
                statusList[0] = taskStatusList.size();
                for (int i: taskStatusList) {
                    if(i == StatusList.UNBEGUN.getValue()){
                        statusList[1]++;
                    } else if(i == StatusList.DOING.getValue()){
                        statusList[2]++;
                    } else if(i == StatusList.FINISH.getValue()){
                        statusList[3]++;
                    }
                }
                Response.setStatusCode(200);
                Response.setMessage("Lấy danh sách thống kê công việc của người dùng thành công");
                Response.setData(statusList);
            }
        } else{
            Response.setStatusCode(404);
            Response.setMessage("Lấy danh sách thống kê công việc của người dùng thất bại");
            Response.setData(null);
        }
        return Response;
    }
}
