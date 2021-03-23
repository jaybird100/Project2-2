package UI.Pages.MainPage;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class MessagingBoard extends ScrollPane {
    public int keep = Integer.MAX_VALUE;
    public MessagingBoard(){
        super();
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.ALWAYS);
        setFitToHeight(true);
        setFitToWidth(true);
        setContent(new VBox(10));
        vvalueProperty().bind(getVBox().heightProperty());
        getVBox().setAlignment(Pos.BOTTOM_CENTER);
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
        if(children.size()>=keep) {
            while (children.size() >= keep) {
                children.remove(0);
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
}