package services;

import data.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance = null;
    final private Writer writer;
    private DateTimeFormatter formater = null;

    private AuditService(){
        writer = Writer.getInstance();
        formater = DateTimeFormatter.ofPattern("dd.MM.yyyy, kk:mm:ss");
    }

    public static AuditService getInstance(){
        if (instance == null)
            instance = new AuditService();
        return instance;
    }

    public void writeAction(String actionName) {
        LocalDateTime dateTime = LocalDateTime.now();
        String line = "\n" + actionName + "," + dateTime.format(formater);
        writer.writeLine("audit", line);
    }
}
