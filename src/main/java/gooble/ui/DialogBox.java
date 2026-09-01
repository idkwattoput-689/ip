package gooble.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** Displays one message in the Gooble conversation. */
public class DialogBox extends HBox {
    /** Creates a message styled as either a user or Gooble response. */
    public DialogBox(String message, boolean isUserMessage) {
        Label text = new Label(message);
        Label avatar = new Label(isUserMessage ? "YOU" : "GOOBLE");
        text.setWrapText(true);
        text.setMaxWidth(320);
        avatar.setMinWidth(55);
        avatar.setAlignment(Pos.CENTER);
        avatar.getStyleClass().add(isUserMessage ? "user-avatar" : "gooble-avatar");
        setSpacing(10);
        setAlignment(isUserMessage ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        getChildren().addAll(isUserMessage ? avatar : text, isUserMessage ? text : avatar);
        getStyleClass().add(isUserMessage ? "user-dialog" : "gooble-dialog");
    }
}
