import java.sql.SQLException;

public class Chat {
    private String chatType;
    private String chatName;
    private String chatOwnerUsername;
    private String chatAttendances;

    public Chat (String chatType, String chatName, String chatOwnerUsername, String chatAttendances){
        this.chatType = chatType;
        this.chatName = chatName;
        this.chatOwnerUsername = chatOwnerUsername;
        this.chatAttendances = chatAttendances;
    }

    public void save() throws SQLException {
        DBConnector dbHandler = new DBConnector();
        dbHandler.insertChat(this.chatType, this.chatName, this.chatOwnerUsername, this.chatAttendances);
    }

}
