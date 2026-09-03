package gooble.ui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;

/** Displays one message in the Gooble conversation. */
public class DialogBox extends HBox {
    /** Creates a message styled as either a user or Gooble response. */
    public DialogBox(String message, boolean isUserMessage) {
        Label text = new Label(message);
        Node avatar = createAvatar(isUserMessage);
        text.setWrapText(true);
        text.setMaxWidth(560);
        text.getStyleClass().add(isUserMessage ? "user-bubble" : "gooble-bubble");
        setSpacing(16);
        setMaxWidth(Double.MAX_VALUE);
        setAlignment(isUserMessage ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        getChildren().addAll(isUserMessage ? text : avatar, isUserMessage ? avatar : text);
        HBox.setHgrow(text, Priority.NEVER);
        getStyleClass().add(isUserMessage ? "user-dialog" : "gooble-dialog");
    }

    private Node createAvatar(boolean isUserMessage) {
        if (isUserMessage) {
            ImageView avatar = new ImageView(new Image(
                    getClass().getResourceAsStream("/user-avatar.png")));
            avatar.setFitWidth(60);
            avatar.setFitHeight(60);
            avatar.setPreserveRatio(true);
            avatar.setClip(new Circle(30, 30, 30));
            avatar.getStyleClass().add("user-avatar");
            return avatar;
        }

        ImageView avatar = new ImageView(new Image(
                getClass().getResourceAsStream("/gooble-avatar.png")));
        avatar.setFitWidth(60);
        avatar.setFitHeight(60);
        avatar.setPreserveRatio(true);
        avatar.setClip(new Circle(30, 30, 30));
        avatar.getStyleClass().add("gooble-avatar");
        return avatar;
    }
}
