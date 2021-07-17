package io.muzoo.ooc.webapp.basic.servlets;

import io.muzoo.ooc.webapp.basic.model.User;
import io.muzoo.ooc.webapp.basic.security.UserService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteUserServlet extends AbstractRoutableHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (securityService.isAuthorized(request)) {
            String username = (String) request.getSession().getAttribute("username");
            UserService userService = UserService.getInstance();
            request.getSession().setAttribute("flashSessionRead",false);
            try{
                User currentUser = userService.findByUsername(username);
                // we will delete user by username, so we need to get requested username from parameter
                User deletingUser = userService.findByUsername(request.getParameter("username"));
                // let's prevent deleting own account. User cannot do it from ui but still can send request directly to server

                if (StringUtils.equals(currentUser.getUsername(), deletingUser.getUsername())) {
                    request.getSession().setAttribute("hasError", true);
                    request.getSession().setAttribute("message", "You cannot delete your own account.");
                }
                else {
                    if (userService.deleteUserByUsername(deletingUser.getUsername())) {
                        // go to user list page with successful delete message
                        // we will put message in the session
                        // these attributes are added to session so they will persist unless remove from session
                        // we need to ensure that they are deleted when they are read next time
                        // since in all cases it will be redirected to homepage so we will remove them in home servlet
                        request.getSession().setAttribute("hasError", false);
                        request.getSession().setAttribute("message", String.format("User %s is susccesfully deleted", deletingUser.getUsername()));
                    }
                    else {
                        // go to user list page with error delete message
                        request.getSession().setAttribute("hasError", true);
                        request.getSession().setAttribute("message", String.format("Unable to delete user %s", deletingUser.getUsername()));
                    }
                }

            } catch (Exception e) {
                // go to user list page with error delete message
                request.getSession().setAttribute("hasError", true);
                request.getSession().setAttribute("message", String.format("unable to delete user %s", request.getParameter("username")));
            }

            response.sendRedirect("/");
        } else {
            response.sendRedirect("/login");
        }
    }

    @Override
    public String getMapping() {
        return "/user/delete";
    }
}
