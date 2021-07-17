package io.muzoo.ooc.webapp.basic.servlets;

import io.muzoo.ooc.webapp.basic.model.User;
import io.muzoo.ooc.webapp.basic.security.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class HomeServlet extends AbstractRoutableHttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        if (securityService.isAuthorized(request)) {
            String username = securityService.getCurrentUsername(request);
            UserService userService = UserService.getInstance();


            request.setAttribute("currentUser", userService.findByUsername(username));
            request.setAttribute("users", userService.findAll());

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/home.jsp");
            rd.include(request, response);

            request.getSession().removeAttribute("hasError");
            request.getSession().removeAttribute("message");

        } else {
            // just add some extra precaution to delete these two attributes
            request.removeAttribute("hasError");
            request.removeAttribute("message");
            response.sendRedirect("/login");
        }

    }
    // user service is used in too many places and we only need one instance of it so we will make it singleton
    @Override
    public String getMapping() {
        return "/index.jsp";
    }
}
