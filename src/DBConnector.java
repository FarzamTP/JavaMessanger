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

        String query = "CREATE TABLE Users (id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY, port INT(10), username VARCHAR (128), password VARCHAR (128), status VARCHAR (32), busy BOOLEAN);";
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

    public void insertUserToDB(int port, String username, String password, String status, boolean busy) throws SQLException {
        DBConnector connector = new DBConnector();

        String query = "INSERT INTO Users (port, username, password, status, busy) VALUES (" + port + ", '" + username + "', '" + password + "', '" + status + "', " + busy + ");";
        Statement st = connector.setStatement();
        st.executeUpdate(query);
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
            String status = resultSet.getString("status");
            boolean isBusy = resultSet.getBoolean("busy");
            System.out.println("Port: " + port + ", Username: " + username + ", Status: " + status + ", isBusy: " + isBusy);
        }
    }

    public boolean userExists(String targetUsername) throws SQLException {
        boolean userExistsFlag = false;
        ResultSet resultSet = this.fetchRecords("Users");
        while(resultSet.next()) {
            String username = resultSet.getString("username");
            if (username.equals(targetUsername)){
                userExistsFlag = true;
                break;
            }
        }
        return userExistsFlag;
    }

    public String getUserPassword(String targetUsername) throws SQLException {
        String password = null;
        ResultSet resultSet = fetchRecords("Users");
        while(resultSet.next()) {
            String username = resultSet.getString("username");
            password = resultSet.getString("password");
            if (targetUsername.equals(username)){
                break;
            }
        }
        return password;
    }

    public String getUserStatus(String targetUsername) throws SQLException {
        String status = null;
        ResultSet resultSet = fetchRecords("Users");
        while(resultSet.next()) {
            String username = resultSet.getString("username");
            status = resultSet.getString("status");
            if (targetUsername.equals(username)){
                break;
            }
        }
        return status;
    }

    public String getUserName(int port) throws SQLException {
        String username = null;
        ResultSet resultSet = fetchRecords("Users");
        while(resultSet.next()) {
            int userPort = resultSet.getInt("port");
            username = resultSet.getString("username");
            if (port == userPort){
                break;
            }
        }
        return username;
    }

    public boolean getUserBusy(String targetUsername) throws SQLException {
        boolean isBusy = false;
        ResultSet resultSet = fetchRecords("Users");
        while(resultSet.next()) {
            String username = resultSet.getString("username");
            isBusy = resultSet.getBoolean("busy");
            if (targetUsername.equals(username)){
                break;
            }
        }
        return isBusy;
    }

    public boolean authenticateUser(String username, String password) throws SQLException {
        if (userExists(username)){
            return password.equals(getUserPassword(username));
        } else {
            return false;
        }
    }

    public void alterUserStatus(String targetUsername, String newStatus) throws SQLException {
        DBConnector connector = new DBConnector();

        String query = "UPDATE Users SET status = '" + newStatus + "' WHERE username = '" + targetUsername + "';";
        ResultSet resultSet = fetchRecords("Users");

        while(resultSet.next()) {
            String username = resultSet.getString("username");
            if (username.equals(targetUsername)){
                Statement st = connector.setStatement();
                st.executeUpdate(query);
            }
        }
    }

    public void alterUserBusy(String targetUsername, boolean newBusy) throws SQLException {
        DBConnector connector = new DBConnector();

        String query = "UPDATE Users SET busy = " + newBusy + " WHERE username = '" + targetUsername + "';";
        ResultSet resultSet = fetchRecords("Users");

        while(resultSet.next()) {
            String username = resultSet.getString("username");
            if (username.equals(targetUsername)){
                Statement st = connector.setStatement();
                st.executeUpdate(query);
            }
        }
    }

}

