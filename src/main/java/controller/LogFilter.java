package controller;

import model.Dao.DaoLog;
import model.Entities.Log;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.*;

@WebFilter("/*")
public class LogFilter implements javax.servlet.Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        DaoLog daoLog = new DaoLog();
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        String ipClient = req.getRemoteAddr();
        String urlServer = req.getRequestURL().toString();
        String commandType = req.getParameter("type");
        Date dateNow = new Date();

        String logString = "Request to URL = " + urlServer +
                (commandType != null ? (" | with command = '" + commandType + "'") : "") +
                " | date = " + dateNow;

        //save to DB
        Log log = new Log(ipClient, logString);
        daoLog.save(log);
        //
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
