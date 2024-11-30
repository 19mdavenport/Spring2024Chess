package dataaccess.mysql;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class MySqlUserDAO extends MySqlDAO implements UserDAO {
    public MySqlUserDAO() throws DataAccessException {
    }

    @Override
    public void clear() throws DataAccessException {
        executeUpdate("TRUNCATE TABLE user;");
    }

    @Override
    public void insertUser(UserData user) throws DataAccessException {
        String statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?);";
        executeUpdate(statement, user.username(), user.password(), user.email());
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return executeQuery("SELECT * FROM user WHERE username=?", (rs) -> rs.next() ?
                new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email")) :
                null, username);
    }

    @Override
    protected String[] getCreateStatements() {
        return new String[]{"""
            CREATE TABLE IF NOT EXISTS `user` (
                `username` VARCHAR(64) NOT NULL PRIMARY KEY,
                `password` VARCHAR(64) NOT NULL,
                `email` VARCHAR(64) NOT NULL
            )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            
            """};
    }
}
