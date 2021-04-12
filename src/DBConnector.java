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
        System.out.println("Running query...");

        String query = "CREATE TABLE Users (id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY, port INT(10), username VARCHAR (128));";
        Statement st = connector.setStatement();
        st.executeUpdate(query);
        System.out.println("Table Users has been created.");
    }

    public void dropTable(String tableName) throws SQLException {
        DBConnector connector = new DBConnector();
        System.out.println("Running query...");

        String query = "DROP TABLE IF EXISTS " + tableName + ";";
        Statement st = connector.setStatement();
        st.executeUpdate(query);
        System.out.println("Table " + tableName + " has been dropped.");
    }

    public void insertUserToDB(int port, String username) throws SQLException {
        DBConnector connector = new DBConnector();
        System.out.println("Running query...");

        String query = "INSERT INTO Users (port, username) VALUES (" + port + ", '" + username + "');";
        Statement st = connector.setStatement();
        st.executeUpdate(query);
        System.out.println("User '" + username + "' with port " + port + " has been inserted into table Users.");
    }

    public void fetchRecords(String tableName) throws SQLException {
        DBConnector connector = new DBConnector();
        System.out.println("Running query...");
        String query = "SELECT * FROM " + tableName + ";";
        Statement st = connector.setStatement();
        ResultSet resultSet = st.executeQuery(query);

        while(resultSet.next()) {
            int port = resultSet.getInt("port");
            String username = resultSet.getString("username");
            System.out.println("Port: " + port + " --> Username: " + username);
        }
    }
}

