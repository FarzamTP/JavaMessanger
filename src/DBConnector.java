import java.sql.*;

class DBConnector {
    public ResultSet runQuery(String query) throws SQLException, ClassNotFoundException {
        String user = "user";
        String password = "user123456";
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/javaProject", user, password);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        return rs;
    }
}

