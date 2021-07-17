package io.muzoo.ooc.webapp.basic.security;


import io.muzoo.ooc.webapp.basic.model.User;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User service is used in too many places and we only need one instance of it so we will make it singleton
 */
public class UserService {

    private static final String INSERT_USER_SQL = "INSERT INTO tbl_user (username, password, display_name) VALUES(?,?,?);";
    private static final String SELECT_USER_SQL = "SELECT * FROM tbl_user WHERE username = ?;";
    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM tbl_user;";
    private static final String DELETE_USER_SQL = "DELETE FROM tbl_user WHERE username = ?;";
    private static final String UPDATE_USER_SQL = "UPDATE tbl_user SET display_name = ? WHERE username = ?;";
    private static final String UPDATE_USER_PASSWORD_SQL = "UPDATE tbl_user SET password = ? WHERE username = ?;";

    private static DatabaseConnectionService database = new DatabaseConnectionService();

    private static UserService service = new UserService();

    public UserService() {

    }

    public static UserService getInstance() {
        if (service == null) {
            service = new UserService();
            service.setDatabaseConnectionService(DatabaseConnectionService.getInstance());
        }
        return service;
    }

    public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService) {
        this.database = databaseConnectionService;
    }

    // create new user
    public void createUser(String username, String password, String displayName) throws UserServiceException {

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (
                Connection connection = database.getConnection();
                PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL);
        ) {
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, displayName);
            ps.executeUpdate();
            //need to be manually commit the change
            connection.setAutoCommit(false);
            connection.commit();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UsernameNotUniqueException(String.format("Username %s has already been taken.", username));
        } catch (SQLException e) {
            throw new UserServiceException(e.getMessage());
        }
    }

    //find user by username
    public User findByUsername(String username) {
        try (
                Connection connection = database.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_USER_SQL);

        ) {
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return new User(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"), // hashed password
                    resultSet.getString("display_Name")
            );

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }


    /**
     * list all users in the database
     *
     * @return list of users, never return null
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (
                Connection connection = database.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_ALL_USERS_SQL);
        ) {
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                users.add(
                        new User(
                                resultSet.getLong("id"),
                                resultSet.getString("username"),
                                resultSet.getString("password"),
                                resultSet.getString("display_Name")));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    /**
     * Delete user by username.
     * @param username
     * @return true if successful
     */
    public boolean deleteUserByUsername(String username) {
        try (
                Connection connection = database.getConnection();
                PreparedStatement ps = connection.prepareStatement(DELETE_USER_SQL);
        ) {
            ps.setString(1, username);
            int deleteCount = ps.executeUpdate();
            connection.commit();
            return deleteCount > 0;
        } catch (SQLException throwables) {
            return false;
        }
    }
    // update user by user id

    /**
     * User can only change their display name when updating profile
     *
     * @param username
     * @param display_name
     */
    public void updateUserByUsername(String username, String display_name) throws UserServiceException {
        try {
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(UPDATE_USER_SQL);

            ps.setString(1, display_name);
            ps.setString(2, username);

            ps.executeUpdate();

            connection.setAutoCommit(false);
            connection.commit();
        } catch (SQLException throwables) {
            throw new UserServiceException(throwables.getMessage());
        }
    }

    /**
     * Change password method is separated from update user method because it normally
     * never change password and update profile at the same time.
     *
     * @param newPassword
     */
    public void changePassword(String username, String newPassword) throws UserServiceException {
        try {
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(UPDATE_USER_PASSWORD_SQL);

            ps.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            ps.setString(2, username);

            ps.executeUpdate();

            connection.setAutoCommit(false);
            connection.commit();
        } catch (SQLException throwables) {
            throw new UserServiceException(throwables.getMessage());
        }
    }


    public static void main(String[] args) throws UserServiceException {
        UserService userService = new UserService();
        userService.setDatabaseConnectionService(new DatabaseConnectionService());
        List<User> users = userService.findAll();
        for (User user : users) {
            System.out.println(user.getUsername());
        }
    }

}
