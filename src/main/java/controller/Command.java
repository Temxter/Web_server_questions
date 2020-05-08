package controller;

import model.Dao.*;
import model.Entities.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Command {
    private CommandType commandType;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Servlet servlet;
    //private int priority;


    //attrMessage - on jsp pages print message
    final private String attrMessage = "message";
    final private String attrUser = "user";
    final private String attrCurrentTest = "currentTest";
    final private String attrQuestionNumber = "questionNumber";
    final private String attrCurrentQuestion = "currentQuestion";
    final private String attrTests = "tests";
    final private String attrStudents = "students";
    final private String attrMarks = "marks";

    final private String cookieVisitCounter = "visitCounter";
    final private String cookieLastVisit = "lastVisitDate";

    final private String pageHome = "";
    final private String pageError = "/errors/notfound.jsp";
    final private String pageLogin = "/auth/login.jsp";
    final private String pageRegister = "/auth/register.jsp";
    final private String pageStudent = "/student/student.jsp";
    final private String pageTest = "/student/test.jsp";
    final private String pageTeacher = "/teacher/teacher.jsp";

    public Command(Servlet servlet, CommandType commandType, HttpServletRequest request, HttpServletResponse response) {
        this.servlet = servlet;
        this.commandType = commandType;
        this.request = request;
        this.response = response;
    }

    public String execute() throws ServletException, IOException {
        String url = null;
        switch (commandType) {
            case login:
                url = login();
                break;
            case register:
                url = register();
                break;
            case logout:
                url = logout();
                break;
            case student:
                url = student();
                break;
            case test:
                url = test();
                break;
            case teacher:
                url = teacher();
            case assignTest:
                url = assignTest();
//                url = insertDataFromFile();
        }
        return url;
    }

    private String teacher() {
        checkLogin();

        //TODO check User is teacher. (in filter)

        List<Test> tests = new DaoTest().getAll();
        List<Student> students = new DaoStudent().getAll();
        List<Mark> marks = new DaoMark().getAll();

        request.setAttribute(attrStudents, students);
        request.setAttribute(attrTests, tests);
        request.setAttribute(attrMarks, marks);
        return pageTeacher;
    }

    private String assignTest() {
        int studentId = Integer.parseInt(request.getParameter("student"));
        int testId = Integer.parseInt(request.getParameter("test"));

        servlet.log("StudentId = " + studentId + " + testId = " + testId);

        Student student = new DaoStudent().get(studentId);
        Test test = new DaoTest().get(testId);
        Mark newAppoint = new Mark(Mark.NEW_TEST, test, student, new Date());
        new DaoMark().save(newAppoint);

        setMessage(test.getName() + " appoint to " + student.getName() + ".");
        return teacher();
    }

    //temp solve (using only in test())
    private static int testResult = 0;

    private String test() {
        DaoTest daoTest = new DaoTest();
        String testIdString = request.getParameter("testId");
        Test test = null;
        int questionNum = 1;

        //user start test
        if (testIdString != null)
            try {
                testResult = 0;
                int testIdInt = Integer.parseInt(testIdString);
                test = daoTest.get(testIdInt);
                setSessionAttribute(attrCurrentTest, test);
                setSessionAttribute(attrQuestionNumber, questionNum);
            } catch (Exception e) {
                e.printStackTrace();
                servlet.log("Error testIdString " + testIdString + " from user " + getLoggedUser(request.getSession(false)));
                return pageError;
            }
            //user continue test
        else {
            questionNum = (Integer) getSessionAttribute(attrQuestionNumber);
            questionNum += 1;
            setSessionAttribute(attrQuestionNumber, questionNum);
            test = (Test) getSessionAttribute(attrCurrentTest);
            Question prevQuestion = test.getQuestion(questionNum - 2);
            String answer = request.getParameter("answer");

            servlet.log("User " + getLoggedUser(request.getSession(false)) + " answered " + answer);

            if (answer != null) {
                // 1, 2, 3...
                int answerInt = Integer.parseInt(answer);
                //write that result is right
                if (prevQuestion.getAnswers().get(answerInt - 1).isRight()) {
                    testResult++;
                }
            }
        }

        Question question = test.getQuestion(questionNum - 1);


        if (question == null) {

            //todo write result
            Mark mark = new Mark(testResult, test,
                    getLoggedUser(request.getSession(false)).getStudent(), new Date());
            new DaoMark().save(mark);
            //need to delete passed test (delete mark or fix mark from -1 to well mark)
            // message finish test
            setMessage("You are finished test: " + test.getName());
            return pageStudent;
        } else
            setSessionAttribute(attrCurrentQuestion, question);

        return pageTest;
    }

    private String student() throws ServletException, IOException {
        checkLogin();

        DaoMark daoMark = new DaoMark();
        DaoStudent daoStudent = new DaoStudent();

        // getSession(false) - not create new session if session not exist
        User user = (User) getLoggedUser(request.getSession(false));

        if (user == null)
            return pageHome;

        servlet.log("[student()]: user = " + user);

        //todo isCorrect to getId from student? - yes, correct.
        Student student = daoStudent.get(user.getStudent().getId());

        List<Mark> markAll = student.getMarks();
        List<Mark> unfilledMarks = markAll.stream().filter(mark -> !mark.isPassed())
                .collect(Collectors.toList());
        List<Mark> filledMarks = markAll.stream().filter(mark -> mark.isPassed())
                .collect(Collectors.toList());

        request.setAttribute("unfilledMarks", unfilledMarks);
        request.setAttribute("filledMarks", filledMarks);
        //todo do request?
        return pageStudent;
    }

    private boolean checkLogin() {
        HttpSession session = request.getSession(false); //false - if session hadn't created, then return null
        if (session == null) {
            //todo do request to login page?
            return false;
        }
        User user = (User) session.getAttribute(attrUser);
        if (user == null)
            return false;
        return true;
    }

    private String login() throws ServletException, IOException {

        DaoUser daoUser = new DaoUser();

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        User user = daoUser.authorizationUser(login, password);

        if (user == null) {
            String message = "Invalidate login/password!";
            request.setAttribute("message", message);
            return pageLogin;
        }

        setSession(request, user);

        servlet.log("Login to system: " + user);

        if (UserType.Student.equals(user.getUserType()))
            return student();
        else
            return teacher();
    }

    private String register() throws ServletException, IOException {
        //todo not very good solution with such architecture
        DaoUser daoUser = new DaoUser();
        DaoStudent daoStudent = new DaoStudent();

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeat-password");

        // 1. if login already exist
        User existUser = daoUser.getUserByLogin(login);
        if (existUser != null) {
            String message = "The user with login " + existUser.getLogin() + " exist! Please, choose another username.";
            setMessage(message);
            return pageRegister;
        }
        if (!password.equals(repeatPassword)) {
            String message = "Password and repeat password not match!";
            setMessage(message);
            return pageRegister;
        }
        // default registration for student only
        Student newStudent = new Student(login);
        User newUser = new User(login, password, UserType.Student, newStudent);
        // write to DB
        daoStudent.save(newStudent);
        daoUser.save(newUser);

        servlet.log("New user \'" + newUser.getLogin() + "\' saved  to database.");
        setSession(request, newUser);

        // page to student (auto login)
        return pageLogin;
    }

    private String logout() throws ServletException, IOException {
        HttpSession session = request.getSession(false); //false - if session hadn't created, then return null
        if (session != null) {
            session.removeAttribute(attrUser);
            //todo remove cookie
        }
        return pageHome;
    }

    private void setSession(HttpServletRequest req, User user) {
        HttpSession session = req.getSession();
        session.setAttribute(attrUser, user);
        user.updateVisit();
        new DaoUser().update(user);
        //add cookies
        Cookie cookieDate = new Cookie(this.cookieLastVisit, user.getLastVisitDate().toString());
        Cookie cookieVisitCounter = new Cookie(this.cookieVisitCounter, user.getVisitCounter().toString());
        //TODO not working on Jetty
        //response.addCookie(cookieDate);
        //response.addCookie(cookieVisitCounter);
    }

    private void setSessionAttribute(String attribute, Object o) {
        HttpSession session = request.getSession();
        session.setAttribute(attribute, o);
    }

    private Object getSessionAttribute(String attribute) {
        HttpSession session = request.getSession();
        return session.getAttribute(attribute);
    }


    private User getLoggedUser(HttpSession session) {
        //todo work with cookie
        if (session == null)
            return null;
        return (User) session.getAttribute(attrUser);
    }

    private void setMessage(String msg) {
        request.setAttribute(attrMessage, msg);
    }

//    private String insertDataFromFile() {
//        try {
//            BufferedReader bf = new BufferedReader(new FileReader("insert_data.sql"));
//            EntityManagerExecutor eme = servlet.getEntityManagerExecutor();
//
//            String line;
//            while ((line = bf.readLine()) != null)
//                eme.execute(line);
//
//        } catch (FileNotFoundException e) {
//            servlet.log("File to insert data not founded: " + e.getMessage());
//        } catch (Exception e) {
//            servlet.log("insertDataFromFile() exception: " + e.getMessage());
//        }
//        setMessage("Data inserted successfully! " + new Date());
//        return pageHome;
//    }

}
