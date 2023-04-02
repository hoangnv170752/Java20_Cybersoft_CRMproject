package controller;

import filter.AuthList;
import filter.AuthenHandling;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RoleController",urlPatterns = {"/role","/role-add","/role-edit"})
public class RoleController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthenHandling AuthenHandling = new AuthenHandling();
        int roleId = AuthenHandling.getRoleOfUser(req);

        if(roleId != AuthList.ADMIN.getValue()){
            req.getRequestDispatcher("403.jsp").forward(req,resp);
        } else {
            String servletPath = req.getServletPath();
            switch (servletPath){
                case "/role":
                    req.getRequestDispatcher("role.jsp").forward(req,resp);
                    break;
                case "/role-add":
                    req.getRequestDispatcher("role-add.jsp").forward(req,resp);
                    break;
                case "/role-edit":
                    req.getRequestDispatcher("role-edit.jsp").forward(req,resp);
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
