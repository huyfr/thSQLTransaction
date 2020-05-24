package controller;

import common.Jdbc;
import dao.UserDAO;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends HttpServlet {

    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = Jdbc.BLANK;
        }
        switch (action) {
            case Jdbc.CREATE:
                createUser(request, response);
                break;
            case Jdbc.EDIT:
                editUser(request, response);
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case Jdbc.CREATE:
                showCreateForm(request, response);
                break;
            case Jdbc.EDIT:
                showEditForm(request, response);
                break;
            case Jdbc.DELETE:
                deleteUser(request, response);
                break;
            case "permission":
                addUserPermission(request, response);
            case "test-without-tran":
                testWithoutTran(request, response);
                break;
            case "test-use-tran":
                testUseTran(request, response);
                break;
            default:
                listUser(request, response);
                break;
        }
    }

    private void testUseTran(HttpServletRequest request, HttpServletResponse response) {
        userDAO.insertUpdateUseTransaction();
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response) {
        List<User> listUser;
        RequestDispatcher requestDispatcher;
        try {
            listUser = userDAO.selectAllUsers();
            request.setAttribute("listUser", listUser);
            requestDispatcher = request.getRequestDispatcher("user/listUser.jsp");
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) {
        RequestDispatcher requestDispatcher;
        try {
            requestDispatcher = request.getRequestDispatcher("user/create.jsp");
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) {
        String name;
        String email;
        String country;
        User user;
        RequestDispatcher requestDispatcher;
        try {
            name = request.getParameter(Jdbc.COLUMN_NAME);
            email = request.getParameter(Jdbc.COLUMN_EMAIL);
            country = request.getParameter(Jdbc.COLUMN_COUNTRY);
            user = new User(name, email, country);
            userDAO.insertUser(user);
            requestDispatcher = request.getRequestDispatcher("user/create.jsp");
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) {
        int id;
        User existingUser;
        RequestDispatcher requestDispatcher;
        try {
            id = Integer.parseInt(request.getParameter(Jdbc.COLUMN_ID));
            existingUser = userDAO.selectUser(id);
            requestDispatcher = request.getRequestDispatcher("user/edit.jsp");
            request.setAttribute("user", existingUser);
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response) {
        int id;
        String name;
        String email;
        String country;
        User user;
        RequestDispatcher requestDispatcher;

        try {
            id = Integer.parseInt(request.getParameter(Jdbc.COLUMN_ID));
            name = request.getParameter(Jdbc.COLUMN_NAME);
            email = request.getParameter(Jdbc.COLUMN_EMAIL);
            country = request.getParameter(Jdbc.COLUMN_COUNTRY);
            user = new User(id, name, email, country);

            userDAO.updateUser(user);
            requestDispatcher = request.getRequestDispatcher("user/edit.jsp");
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        int id;
        try {
            id = Integer.parseInt(request.getParameter(Jdbc.COLUMN_ID));
            userDAO.deleteUser(id);
            listUser(request, response);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void searchByCountry(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String country = request.getParameter("search");
        List<User> userList = userDAO.searchByCountry(country);

        request.setAttribute("userList", userList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/search.jsp");
        try {
            dispatcher.forward(request,response);
        } catch (ServletException | IOException ex) {
            ex.printStackTrace();
        }
    }

    private void addUserPermission(HttpServletRequest request, HttpServletResponse response) {
        User user = new User("kien", "kienhoang@gmail.com", "vn");
        int[] permission = {1, 2, 4};
        userDAO.addUserTransaction(user, permission);
    }

    private void testWithoutTran(HttpServletRequest request, HttpServletResponse response) {
        userDAO.insertUpdateWithoutTransaction();
    }
}