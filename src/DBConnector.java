import java.sql.*;

class DBConnector {
    public Statement setStatement() throws SQLException {
        String user = "user";
        String password = "user123456";
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/javaProject", user, password);
        return con.createStatement();
    }

    public void insertUserToDB(int port, String username) throws SQLException {
        System.out.println("Connecting to DB...");
        DBConnector connector = new DBConnector();
        System.out.println("Running query...");

        String query = "INSERT INTO Users (port, username) VALUES (" + port + ", '" + username + "');";
        System.out.println(query);
        Statement st = connector.setStatement();
        st.executeUpdate(query);
        System.out.println("User '" + username + "' with port " + port + " has been inserted into table Users.");
    }

    public void fetchRecords(String tableName) throws SQLException {
        System.out.println("Connecting to DB...");
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

