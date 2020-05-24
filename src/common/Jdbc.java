package common;

import java.sql.Connection;

public class Jdbc {
    //dao
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/demo";
    public static final String USER = "root";
    public static final String PASSWORD = "admin123";

    public static final String INSERT_USERS_SQL = "INSERT INTO user" + "  (name, email, country) VALUES " + " (?, ?, ?);";
    public static final String SELECT_USER_BY_ID = "select id,name,email,country from user where id =?";
    public static final String SELECT_ALL_USERS = "select * from user";
    public static final String DELETE_USERS_SQL = "delete from user where id = ?;";
    public static final String UPDATE_USERS_SQL = "update user set name = ?,email= ?, country =? where id = ?;";

    public static final String BLANK = "";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_COUNTRY = "country";
    public static final Connection CONNECTION_NULL = null;

    //controller
    public static final String CREATE = "create";
    public static final String EDIT = "edit";
    public static final String DELETE = "delete";
}