package gooble.ui;

import gooble.Gooble;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Provides the JavaFX interface for interacting with Gooble. */
public class GoobleGui extends Application {
    private final Gooble gooble = new Gooble("data/Gooble.txt");
    private final VBox dialogContainer = new VBox(10);
    private ScrollPane scrollPane;
    private TextField userInput;

    @Override
    public void start(Stage stage) {
        scrollPane = new ScrollPane(dialogContainer);
        userInput = new TextField();
        userInput.setPromptText("Enter a command, e.g. list or todo read book");
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> handleInput());
        userInput.setOnAction(event -> handleInput());

        HBox inputBar = new HBox(8, userInput, sendButton);
        inputBar.setPadding(new Insets(10));
        HBox.setHgrow(userInput, Priority.ALWAYS);

        AnchorPane mainLayout = new AnchorPane(scrollPane, inputBar);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 58.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(inputBar, 0.0);
        AnchorPane.setBottomAnchor(inputBar, 0.0);
        AnchorPane.setLeftAnchor(inputBar, 0.0);

        Scene scene = new Scene(mainLayout, 520, 650);
        scene.getStylesheets().add(getClass().getResource("/gooble.css").toExternalForm());
        stage.setTitle("Gooble");
        stage.setMinWidth(420);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();

        appendMessage("Hello! I'm Gooble. What can I do for you?", false);
        userInput.requestFocus();
    }

    private void handleInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }
        appendMessage(input, true);
        userInput.clear();
        boolean isExit = gooble.executeCommand(input, response -> appendMessage(response, false));
        if (isExit) {
            userInput.setDisable(true);
        }
    }

    private void appendMessage(String message, boolean isUserMessage) {
        if (message.isEmpty()) {
            return;
        }
        for (String line : message.stripTrailing().split("\\R")) {
            dialogContainer.getChildren().add(new DialogBox(line, isUserMessage));
        }
        scrollPane.setVvalue(1.0);
    }
}
