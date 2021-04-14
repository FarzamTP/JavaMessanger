import java.sql.*;

class DBConnector {
    private static final DBConnector connector = new DBConnector();

    public Statement setStatement() throws SQLException {
        String user = "user";
        String password = "user123456";
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/javaProject", user, password);
        return con.createStatement();
    }

    public void createTableUsers() throws SQLException {
        String query = "CREATE TABLE Users (id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY, port INT(10), username VARCHAR (128), password VARCHAR (128), status VARCHAR (32), busy BOOLEAN, chat VARCHAR (128));";
        Statement st = connector.setStatement();
        st.executeUpdate(query);
        System.out.println("Table Users has been created.");
    }

    public void dropTable(String tableName) throws SQLException {

        String query = "DROP TABLE IF EXISTS " + tableName + ";";
        Statement st = connector.setStatement();
        st.executeUpdate(query);
        System.out.println("Table " + tableName + " has been dropped.");
    }

    public void insertUser(int port, String username, String password, String status, String chatName) throws SQLException {

        String query = "INSERT INTO Users (port, username, password, status, busy, chat) VALUES (" + port + ", '" + username + "', '" + password + "', '" + status + "', false, '" + chatName + "');";
        System.out.println(query);
        Statement st = connector.setStatement();
        st.executeUpdate(query);
    }

    public ResultSet fetchRecords(String tableName) throws SQLException {
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
            String chatName = resultSet.getString("chat");
            System.out.println("Port: " + port + ", Username: " + username + ", Status: " + status + ", isBusy: " + isBusy + " Chat: " + chatName);
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

    public String getChatName(String targetUsername) throws SQLException {
        String chatName = null;
        ResultSet resultSet = fetchRecords("Users");
        while(resultSet.next()) {
            String username = resultSet.getString("username");
            chatName = resultSet.getString("chat");
            if (targetUsername.equals(username)){
                break;
            }
        }
        return chatName;
    }

    public boolean authenticateUser(String username, String password) throws SQLException {
        if (userExists(username)){
            return password.equals(getUserPassword(username));
        } else {
            return false;
        }
    }

    public void alterUserStatus(String targetUsername, String newStatus) throws SQLException {
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

    public void alterUserChat(String targetUsername, String newChat) throws SQLException {
        String query = "UPDATE Users SET chat = '" + newChat + "' WHERE username = '" + targetUsername + "';";
        ResultSet resultSet = fetchRecords("Users");

        while(resultSet.next()) {
            String username = resultSet.getString("username");
            if (username.equals(targetUsername)){
                Statement st = connector.setStatement();
                st.executeUpdate(query);
            }
        }
    }

    public void createTableChats() throws SQLException {
        String query = "CREATE TABLE Chats (id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY, type VARCHAR(32), name VARCHAR (128), owner VARCHAR (128), attendances VARCHAR (1028));";
        Statement st = connector.setStatement();
        st.executeUpdate(query);
        System.out.println("Table Chats has been created.");
    }

    public void insertChat(String chatType, String chatName, String chatOwnerUsername, String attendances) throws SQLException {
        String query = "INSERT INTO Chats (type, name, owner, attendances) VALUES ('" + chatType + "', '" + chatName + "', '" + chatOwnerUsername + "', '" + attendances + "');";
        Statement st = connector.setStatement();
        st.executeUpdate(query);
    }

    public String getChatAttendances(String chatName) throws SQLException {
        String chatAttendances = null;
        ResultSet resultSet = fetchRecords("Chats");
        while(resultSet.next()) {
            String name = resultSet.getString("name");
            chatAttendances = resultSet.getString("attendances");
            if (chatName.equalsIgnoreCase(name)){
                break;
            }
        }
        return chatAttendances;
    }

    public String getChatType(String chatName) throws SQLException {
        String chatType = null;
        ResultSet resultSet = fetchRecords("Chats");
        while(resultSet.next()) {
            String name = resultSet.getString("name");
            chatType = resultSet.getString("type");
            if (chatName.equalsIgnoreCase(name)){
                break;
            }
        }
        return chatType;
    }

    public String getChatOwner(String chatName) throws SQLException {
        String chatOwner = null;
        ResultSet resultSet = fetchRecords("Chats");
        while(resultSet.next()) {
            String name = resultSet.getString("name");
            chatOwner = resultSet.getString("owner");
            if (chatName.equalsIgnoreCase(name)){
                break;
            }
        }
        return chatOwner;
    }

    public static void main(String[] args) throws SQLException {
        DBConnector db = new DBConnector();
        boolean a = db.getUserBusy("Test1");
        System.out.println(a);
    }
}

