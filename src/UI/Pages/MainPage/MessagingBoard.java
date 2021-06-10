package UI.Pages.MainPage;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class MessagingBoard extends ScrollPane {
    public int keep = Integer.MAX_VALUE;
    public List<Message> messageList;
    public MessagingBoard(){
        super();
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.ALWAYS);
        setFitToHeight(true);
        setFitToWidth(true);
        setContent(new VBox(10));
        vvalueProperty().bind(getVBox().heightProperty());
        getVBox().setAlignment(Pos.BOTTOM_CENTER);
        messageList = new ArrayList<>();
    }
    public VBox getVBox(){
        return (VBox)getContent();
    }
    public void addMessage(Message message, boolean user){
        Text t = new Text(message.toString());
        t.getStyleClass().add("log-label");
        t.setTextAlignment(user? TextAlignment.RIGHT:TextAlignment.LEFT);
        ObservableList<Node> children = getVBox().getChildren();
        children.add(t);
        messageList.add(0,message);
        if(children.size()>=keep) {
            while (children.size() >= keep) {
                children.remove(0);
                messageList.remove(0);
            }
        }
    }
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        for (Node child : getVBox().getChildren()) {
            ((Text)child).setWrappingWidth(getWidth()-20);
        }
    }

    public String getPrevious(int i, String by){
        int c = 0;
        while(c<messageList.size()-1){
            if(messageList.get(c).id.equals(by)){
                i--;
                if(i==0){
                    return messageList.get(c).message;
                }
            }
            c++;
        }
        return messageList.size()==0? "":messageList.get(c).message;
    }
}