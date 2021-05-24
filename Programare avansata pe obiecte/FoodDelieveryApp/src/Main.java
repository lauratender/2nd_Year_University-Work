import database.MySqlConn;
import services.ReadingService;

public class Main {
    public static void main(String[] args) {
        ReadingService app = ReadingService.getInstance();
        app.Run();
    }
}
