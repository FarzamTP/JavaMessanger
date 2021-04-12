import java.sql.*;

class DBConnector {

    private ResultSet runQuery(String query) throws SQLException {
        String user = "user";
        String password = "user123456";
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/javaProject", user, password);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        return rs;
    }
}

