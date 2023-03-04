import org.sqlite.JDBC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sergii Bugaienko
 */

public class DbHandler {
    private static final Logger LOGGER = LogManager.getLogger(DbHandler.class);
    private static final String separator = File.separator;
//    private static final String mainPath = separator + "JavaProCourse" + separator + "src" + separator + "homework30" + separator;
    private static final String mainPath = separator;
    private static final String CON_STR = "jdbc:sqlite:" +  mainPath + "db1.db";
    private static DbHandler instance = null;
    private Connection connection;
    private Statement statement;
    public static synchronized DbHandler getInstance() throws SQLException {
        if (instance == null) {
            instance = new DbHandler();
        }
        return instance;
    }

    private DbHandler() {
        try {
            DriverManager.registerDriver(new JDBC());
            this.connection = DriverManager.getConnection(CON_STR);
            this.statement = this.connection.createStatement();
            createTableUsers(statement);
            LOGGER.info("Connection done");
//            System.out.println("Connection done");
        } catch (SQLException e) {
            LOGGER.fatal(e);
//            e.printStackTrace();
        }
    }

    private void createTableUsers(Statement statement) {
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (\n" +
                    "    id         INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                    "                       UNIQUE\n" +
                    "                       NOT NULL,\n" +
                    "    name       TEXT    NOT NULL,\n" +
                    "    pass       TEXT    NOT NULL,\n" +
                    "    created_ad TEXT    DEFAULT (CURRENT_TIMESTAMP) \n" +
                    ");\n");
            LOGGER.trace("Create table users");
        } catch (SQLException e) {
            LOGGER.fatal("Cant create Table user");
            LOGGER.error(e);
//            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        try(Statement statement = this.connection.createStatement())
        {
            List<User> users = new ArrayList<>();
            ResultSet res = statement.executeQuery(
                    "SELECT id, name, pass FROM users"
            );
            while (res.next()) {
                users.add(
                        new User(
                                res.getInt("id"),
                                res.getString("name"),
                                res.getString("pass")
                        )
                );
            }
            return users;

        } catch (SQLException e) {
            LOGGER.error(e);
//            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void addNewUser (String name, String pass) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO users('name', 'pass') " +
                        "VALUES (?, ?)"
        )) {
            statement.setString(1, name);
            statement.setString(2, pass);

            statement.execute();
            LOGGER.info("Add new user");

        } catch (SQLException e) {
            LOGGER.error("add User to BD Error");
            LOGGER.fatal(e);
//            e.printStackTrace();
//            System.out.println("add User to BD Error");
        }
    }

    public User checkName (String name) {
            List<User> users = getAllUsers();
            if (users.isEmpty()) {
                return null;
            }
            for (User user: users) {
                if (user.getName().equals(name)) {
                    return user;
                }
            }
        return null;
    }

    public boolean checkPass(User user, String pass) {
        return (user.getPassword().equals(pass));
    }

    public boolean changeName(int id, String newName) {
        String query = "UPDATE users SET name = ? WHERE id = ?";
        try(PreparedStatement statement1 = this.connection.prepareStatement(
                query
        ))
        {
            statement1.setString(1, newName);
            statement1.setInt(2, id);
            statement1.executeUpdate();
            LOGGER.trace("User changed name");
            return true;

        } catch (SQLException e) {
            LOGGER.error("Cant change client name");
            LOGGER.error(e);
//            e.printStackTrace();
        }
        return false;
    }
    public boolean changePass(int id, String newPass) {
        String query = "UPDATE users SET pass = ? WHERE id = ?";
        try(PreparedStatement statement = this.connection.prepareStatement(
                query
        )) {
            statement.setString(1, newPass);
            statement.setInt(2, id);
            statement.executeUpdate();
            LOGGER.trace("User changed pass");
            return true;

        } catch (SQLException e) {
            LOGGER.error("Cant pass Update");
            LOGGER.error(e);
//            e.printStackTrace();
//            System.out.println("Cant pass Update");
        }
        return false;
    }
}
