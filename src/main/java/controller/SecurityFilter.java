package controller;

import model.Entities.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = {"/singleServlet"})
public class SecurityFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String commandTypeString = req.getParameter("type");
        if (commandTypeString != null) {
            try {
                CommandType commandType = CommandType.valueOf(commandTypeString);
                User user = (User) req.getSession().getAttribute("user");
                //add commandType for not logined users
                if (user == null &&
                        !(commandType.equals(CommandType.login)
                        || commandType.equals(CommandType.register)
                        || commandType.equals(CommandType.logout))) {
                    req.getServletContext().log("SecurityFilter: guest have not access to logined users functions!");
                    req.setAttribute("message", "Guest have not access to logined users functions!");
                    return;
                }
            } catch (Exception e) {
                req.getServletContext().log("SecurityFilter exception! Possibly not correct command = '" +
                        commandTypeString + "'. Exception message: " + e.getMessage());
                e.printStackTrace();
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
        // if command == null
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    //for security filter
//    private HashSet<CommandType> studentCommands = new HashSet<>(Arrays.asList(CommandType.student, CommandType.test));
//    private HashSet<CommandType> teacherCommands = new HashSet<>(Arrays.asList(CommandType.teacher, CommandType.createTest,
//            CommandType.assignTest));
//    private HashSet<CommandType> simpleUserCommands = new HashSet<>(Arrays.asList(CommandType.logout));
//
//    private String errorPage = "/errors/notfound.jsp";
}
