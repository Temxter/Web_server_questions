package controller;

import model.Dao.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/singleServlet", name = "singleServlet")
public class Servlet extends HttpServlet {

    /**
     * Processing both GET and POST HTML methods.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandTypeString = req.getParameter("type"); // "login" or "register" or ...
        String page = "/errors/notfound.jsp";
        if (commandTypeString != null) {
            try {
                CommandType commandType = CommandType.valueOf(commandTypeString);
                Command command = new Command(this, commandType, req, resp);
                log("Command type = " + commandTypeString);
                page = command.execute();
            } catch (IllegalArgumentException e) {
                log(String.format("Command \"%s\" had not defined: %s ", commandTypeString, e.getMessage()));
            }
        }
        dispatch(req, resp, page);
    }

    // in other class
    protected void getTypeCommand(){

    }

    protected void dispatch(HttpServletRequest req, HttpServletResponse resp, String page)
            throws  javax.servlet.ServletException,
            java.io.IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(page);
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
