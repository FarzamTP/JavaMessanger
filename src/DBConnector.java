import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class DBConnector {

    private static String user ="user";
    private static String password = "user123456";

    public static void main(String args[])throws Exception {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/javaProject", user, password);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select * from users");
        while(rs.next()) {
            System.out.println(rs.getString("port"));
        }
    }
}

