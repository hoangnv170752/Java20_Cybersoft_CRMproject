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
        Response basicResponse = new Response();
        CookieHandling cookieHandling = new CookieHandling();
        Cookie cookie = cookieHandling.getCookie(req);

        if(cookie != null){
            AuthService authService = new AuthService();
            UserModel user = authService.getUser(cookie.getValue());
            if(user != null){
                basicResponse.setStatusCode(200);
                basicResponse.setMessage("Lấy thông tin user thành công");
                basicResponse.setData(user);

                return basicResponse;
            } else {
                basicResponse.setMessage("Không tìm thấy user");
            }
        } else {
            basicResponse.setMessage("Không tìm thấy cookie");
        }
        basicResponse.setStatusCode(404);
        basicResponse.setData(null);

        return basicResponse;
    }
    public Response logOut(HttpServletRequest req, HttpServletResponse resp){
        Response basicResponse = new Response();
        CookieHandling cookieHandling = new CookieHandling();
        Cookie cookie = cookieHandling.getCookie(req);

        if(cookie != null){
            basicResponse.setStatusCode(200);
            cookieHandling.deleteCookie(resp,cookie);
            basicResponse.setMessage("Đăng xuất thành công");
            basicResponse.setData(true);
        } else{
            basicResponse.setStatusCode(400);
            basicResponse.setMessage("Đăng xuất thất bại");
            basicResponse.setData(false);
        }
        return basicResponse;
    }
    public Response verifyLoginAccount(HttpServletResponse resp, String email, String password){
        Response basicResponse = new Response();
        LoginService loginService = new LoginService();
        if(loginService.checkLogin(email,password)){
            CookieHandling cookieHandling = new CookieHandling();
            cookieHandling.addCookie(resp,email);
            basicResponse.setStatusCode(200);
            basicResponse.setMessage("Đăng nhập thành công");
            basicResponse.setData(true);
        } else{
            basicResponse.setStatusCode(200);
            basicResponse.setMessage("Đăng nhập thất bại");
            basicResponse.setData(false);
        }
        return basicResponse;
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
