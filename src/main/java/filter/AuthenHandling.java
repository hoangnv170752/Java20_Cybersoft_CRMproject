package filter;

import model.UserModel;
import payload.Response;
import service.AuthService;
import service.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenHandling {
    final String COOKIE_NAME = "email";
    public Response getUserInfo(HttpServletRequest req) {
        Response Response = new Response();
        CookieHandling cookieHandling = new CookieHandling();
        Cookie cookie = cookieHandling.getCookie(req);

        if(cookie != null){
            AuthService authService = new AuthService();
            UserModel user = authService.getUser(cookie.getValue());
            if(user != null){
                Response.setStatusCode(200);
                Response.setMessage("Lấy thông tin user thành công");
                Response.setData(user);

                return Response;
            } else {
                Response.setMessage("Không tìm thấy user");
            }
        } else {
            Response.setMessage("Không tìm thấy cookie");
        }
        Response.setStatusCode(404);
        Response.setData(null);

        return Response;
    }
    public Response logOut(HttpServletRequest req, HttpServletResponse resp){
        Response Response = new Response();
        CookieHandling cookieHandling = new CookieHandling();
        Cookie cookie = cookieHandling.getCookie(req);

        if(cookie != null){
            Response.setStatusCode(200);
            cookieHandling.deleteCookie(resp,cookie);
            Response.setMessage("Đăng xuất thành công");
            Response.setData(true);
        } else{
            Response.setStatusCode(400);
            Response.setMessage("Đăng xuất thất bại");
            Response.setData(false);
        }
        return Response;
    }
    public Response verifyLoginAccount(HttpServletResponse resp, String email, String password){
        Response Response = new Response();
        LoginService loginService = new LoginService();
        if(loginService.checkLogin(email,password)){
            CookieHandling cookieHandling = new CookieHandling();
            cookieHandling.addCookie(resp,email);
            Response.setStatusCode(200);
            Response.setMessage("Đăng nhập thành công");
            Response.setData(true);
        } else{
            Response.setStatusCode(200);
            Response.setMessage("Đăng nhập thất bại");
            Response.setData(false);
        }
        return Response;
    }
    public boolean isLoggedIn(Cookie[] cookies){
        boolean isLoggedin = false;
        for (Cookie ck: cookies) {
            if(COOKIE_NAME.equals(ck.getName()) && !("".equals(ck.getValue()))){
                isLoggedin = true;
                break;
            }
        }
        return isLoggedin;
    }
    public int getRoleOfUser(HttpServletRequest req){
        CookieHandling cookieHandling = new CookieHandling();
        Cookie cookie = cookieHandling.getCookie(req);
        String email = cookie.getValue();
        AuthService authService = new AuthService();
        return authService.getRoleByEmail(email);
    }
}
