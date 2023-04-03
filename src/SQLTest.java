import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class SQLTest {
    

    public static void main(String[] args) {
        System.out.println("init");
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/files?" +
            "user=root&password=password");
            String statement = "SELECT * FROM files";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(statement);
            System.out.println(rs.getMetaData());


        } catch (SQLException e){
            System.out.println(e.getMessage());
        }


    }


}
