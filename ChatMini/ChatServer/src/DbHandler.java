import org.sqlite.JDBC;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sergii Bugaienko
 */

public class DbHandler {
    private static final String separator = File.separator;
//    private static final String mainPath = separator + "JavaProCourse" + separator + "src" + separator + "homework30" + separator;
    private static final String mainPath ="src" + separator;
    private static final String CON_STR = "jdbc:sqlite:" +  mainPath + "db1.db";
    private static DbHandler instance = null;
    private Connection connection;
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
            System.out.println("Connection done");
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("add User to BD Error");
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

    public boolean changePass(int id, String newPass) {
        String query = "UPDATE users SET pass = ? WHERE id = ?";
        try(PreparedStatement statement = this.connection.prepareStatement(
                query
        )) {
            statement.setString(1, newPass);
            statement.setInt(2, id);
            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Cant pass Update");
        }
        return false;
    }
}
