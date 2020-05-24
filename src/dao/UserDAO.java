package dao;

import common.Error;
import model.User;
import common.Jdbc;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

    private static final String SEARCH_BY_COUNTRY = "select * from demo.users where country = ? order by name desc;";

    public UserDAO() {
    }

    protected Connection getConnection() {
        Connection connection = Jdbc.CONNECTION_NULL;
        try {
            Class.forName(Jdbc.JDBC_DRIVER);
            connection = DriverManager.getConnection(Jdbc.DATABASE_URL, Jdbc.USER, Jdbc.PASSWORD);
        } catch (SQLException ex) {
            System.err.println(Error.ERROR_001);
        } catch (ClassNotFoundException ex) {
            System.err.println(Error.ERROR_002);
        }
        return connection;
    }

    @Override
    public void insertUser(User user) {
        Connection connection;
        PreparedStatement preparedStatement;
        System.out.println(Jdbc.INSERT_USERS_SQL);
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Jdbc.INSERT_USERS_SQL);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(Error.ERROR_003);
        }
    }

    @Override
    public User selectUser(int id) {
        User user = null;
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet rs;
        String name;
        String email;
        String country;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Jdbc.SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                name = rs.getString(Jdbc.COLUMN_NAME);
                email = rs.getString(Jdbc.COLUMN_EMAIL);
                country = rs.getString(Jdbc.COLUMN_COUNTRY);
                user = new User(id, name, email, country);
            }
        } catch (SQLException ex) {
            System.err.println(Error.ERROR_005);
        }
        return user;
    }

    @Override
    public List<User> selectAllUsers() {
        List<User> users = null;
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        int id;
        String name;
        String email;
        String country;

        try {
            users = new ArrayList<>();
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Jdbc.SELECT_ALL_USERS);
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getInt(Jdbc.COLUMN_ID);
                name = resultSet.getString(Jdbc.COLUMN_NAME);
                email = resultSet.getString(Jdbc.COLUMN_EMAIL);
                country = resultSet.getString(Jdbc.COLUMN_COUNTRY);
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException ex) {
            System.err.println(Error.ERROR_004);
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) {
        boolean rowDeleted = false;
        Connection connection;
        PreparedStatement preparedStatement;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Jdbc.DELETE_USERS_SQL);
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println(Error.ERROR_006);
        }
        return rowDeleted;
    }

    @Override
    public boolean updateUser(User user) {
        boolean rowUpdated = false;
        Connection connection;
        PreparedStatement preparedStatement;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Jdbc.UPDATE_USERS_SQL);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            preparedStatement.setInt(4, user.getId());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println(Error.ERROR_007);
        }
        return rowUpdated;
    }

    @Override
    public void addUserTransaction(User user, int[] permission) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtAssignment = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(Jdbc.INSERT_USERS_SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getCountry());
            int rowAffected = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            int userId = 0;
            if (rs.next())
                userId = rs.getInt(1);
            if (rowAffected == 1) {
                String sqlPivot = "INSERT INTO permision (user_id,permision_id) " + "VALUES(?,?)";
                pstmtAssignment = conn.prepareStatement(sqlPivot);
                for (int permisionId : permission) {
                    pstmtAssignment.setInt(1, userId);
                    pstmtAssignment.setInt(2, permisionId);
                    pstmtAssignment.executeUpdate();
                }
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (pstmtAssignment != null) pstmtAssignment.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void insertUpdateWithoutTransaction() {
        try (Connection conn = getConnection();
            Statement statement = conn.createStatement();
            PreparedStatement psInsert = conn.prepareStatement(SQL_INSERT);
            PreparedStatement psUpdate = conn.prepareStatement(SQL_UPDATE)) {
            statement.execute(SQL_TABLE_DROP);
            statement.execute(SQL_TABLE_CREATE);
            psInsert.setString(1, "Quynh");
            psInsert.setBigDecimal(2, new BigDecimal(10));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();
            psInsert.setString(1, "Ngan");
            psInsert.setBigDecimal(2, new BigDecimal(20));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();
            psUpdate.setBigDecimal(2, new BigDecimal(999.99));
            psUpdate.setString(2, "Quynh");
            psUpdate.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> searchByCountry(String country) throws SQLException {
        List<User> users = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(SEARCH_BY_COUNTRY);
        ps.setString(1, country);

        System.out.println(ps);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String name = rs.getString("name");
            String email = rs.getString("email");
            String countryRS = rs.getString("country");

            User user = new User(name, email, countryRS);
            users.add(user);
        }

        return users;
    }

    @Override

    public void insertUpdateUseTransaction() {
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             PreparedStatement psInsert = conn.prepareStatement(SQL_INSERT);
             PreparedStatement psUpdate = conn.prepareStatement(SQL_UPDATE)) {
            statement.execute(SQL_TABLE_DROP);
            statement.execute(SQL_TABLE_CREATE);
            conn.setAutoCommit(false);
            psInsert.setString(1, "Quynh");
            psInsert.setBigDecimal(2, new BigDecimal(10));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();
            psInsert.setString(1, "Ngan");
            psInsert.setBigDecimal(2, new BigDecimal(20));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();
            psUpdate.setBigDecimal(2, new BigDecimal(999.99));
            psUpdate.setString(2, "Quynh");
            psUpdate.execute();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static final String SQL_INSERT = "INSERT INTO EMPLOYEE (NAME, SALARY, CREATED_DATE) VALUES (?,?,?)";

    private static final String SQL_UPDATE = "UPDATE EMPLOYEE SET SALARY=? WHERE NAME=?";

    private static final String SQL_TABLE_CREATE = "CREATE TABLE EMPLOYEE"

            + "("

            + " ID serial,"

            + " NAME varchar(100) NOT NULL,"

            + " SALARY numeric(15, 2) NOT NULL,"

            + " CREATED_DATE timestamp,"

            + " PRIMARY KEY (ID)"

            + ")";

    private static final String SQL_TABLE_DROP = "DROP TABLE IF EXISTS EMPLOYEE";

}