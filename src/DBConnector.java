import java.sql.*;

class DBConnector {
    public Statement setStatement() throws SQLException {
        String user = "user";
        String password = "user123456";
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/javaProject", user, password);
        return con.createStatement();
    }

    public void createTableUsers() throws SQLException {
        DBConnector connector = new DBConnector();

        String query = "CREATE TABLE Users (id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY, port INT(10), username VARCHAR (128), password VARCHAR (128));";
        Statement st = connector.setStatement();
        st.executeUpdate(query);
        System.out.println("Table Users has been created.");
    }

    public void dropTable(String tableName) throws SQLException {
        DBConnector connector = new DBConnector();

        String query = "DROP TABLE IF EXISTS " + tableName + ";";
        Statement st = connector.setStatement();
        st.executeUpdate(query);
        System.out.println("Table " + tableName + " has been dropped.");
    }

    public void insertUserToDB(int port, String username, String password) throws SQLException {
        DBConnector connector = new DBConnector();

        String query = "INSERT INTO Users (port, username, password) VALUES (" + port + ", '" + username + "', '" + password + "');";
        Statement st = connector.setStatement();
        st.executeUpdate(query);
        System.out.println("User '" + username + "' with port " + port + " has been inserted into table Users.");
    }

    public ResultSet fetchRecords(String tableName) throws SQLException {
        DBConnector connector = new DBConnector();

        String query = "SELECT * FROM " + tableName + ";";
        Statement st = connector.setStatement();
        return st.executeQuery(query);
    }

    public void printRecords(ResultSet resultSet) throws SQLException {
        while(resultSet.next()) {
            int port = resultSet.getInt("port");
            String username = resultSet.getString("username");
            System.out.println("Port: " + port + " --> Username: " + username);
        }
    }

    public boolean userExists(String target_username) throws SQLException {
        boolean userExistsFlag = false;
        ResultSet resultSet = this.fetchRecords("Users");
        while(resultSet.next()) {
            String username = resultSet.getString("username");
            if (username.equals(target_username)){
                userExistsFlag = true;
                break;
            }
        }
        return userExistsFlag;
    }

    public String getPassword(String target_username) throws SQLException {
        String password = null;
        ResultSet resultSet = fetchRecords("Users");
        while(resultSet.next()) {
            String username = resultSet.getString("username");
            password = resultSet.getString("password");
            if (target_username.equals(username)){
                break;
            }
        }
        return password;
    }

    public boolean authenticateUser(String username, String password) throws SQLException {
        if (userExists(username)){
            return password.equals(getPassword(username));
        } else {
            return false;
        }
    }
}

