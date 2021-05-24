import database.MySqlConn;
import services.ReadingService;

public class Main {
    public static void main(String[] args) {
        ReadingService app = ReadingService.getInstance();
        /*try {
            MySqlConn.getConection();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        app.Run();
    }
}
