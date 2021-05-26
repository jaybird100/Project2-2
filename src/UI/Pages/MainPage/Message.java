package UI.Pages.MainPage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    public final String message;
    public final String id;
    public final LocalDateTime time;

    public Message(String m, String id, LocalDateTime t){
        this.message = m;
        this.id = id;
        this.time = t;
    }

    public Message(String m, String id){
        this(m, id, LocalDateTime.now());
    }

    public String toString(){
        return message+"\n"+id+", "+dtf.format(time);
    }
}
