package gooble.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/** Displays one compact message in the Gooble conversation. */
public class DialogBox extends HBox {
    /** Creates a message styled as either a user or Gooble response. */
    public DialogBox(String message, boolean isUserMessage) {
        this(message, isUserMessage, false);
    }

    /** Creates a message with optional error styling. */
    public DialogBox(String message, boolean isUserMessage, boolean isError) {
        this(message, isUserMessage, isError, false);
    }

    /** Creates a message with optional error or help styling. */
    public DialogBox(String message, boolean isUserMessage, boolean isError, boolean isHelp) {
        String displayMessage = isUserMessage ? message
                : isError ? "⚠ " + message : isHelp ? message : "Gooble: " + message;
        Label text = new Label(displayMessage);
        text.setWrapText(true);
        text.setMaxWidth(680);
        text.getStyleClass().add(isError ? "error-message" : isHelp ? "help-message"
                : isUserMessage ? "command-line" : "response-line");
        setSpacing(8);
        setMaxWidth(Double.MAX_VALUE);
        setAlignment(isUserMessage ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        getChildren().add(text);
        HBox.setHgrow(text, Priority.NEVER);
        getStyleClass().add(isError ? "error-dialog" : isHelp ? "help-dialog"
                : isUserMessage ? "user-dialog" : "gooble-dialog");
    }
}
