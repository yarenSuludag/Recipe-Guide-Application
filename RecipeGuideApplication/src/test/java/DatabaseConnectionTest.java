import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/tarifrehberi?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false&allowPublicKeyRetrieval=true"; // Veritabanı bağlantı URL'inizi yazın
        String username = "root"; // Veritabanı kullanıcı adınızı yazın
        String password = "fb123456"; // Veritabanı şifrenizi yazın

        try {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            if (connection != null) {
                System.out.println("Veritabanına başarılı bir şekilde bağlandı!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Veritabanı bağlantısında bir hata oluştu.");
        }
    }
}
