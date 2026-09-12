package gooble.ui;

import gooble.Gooble;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Provides the JavaFX interface for interacting with Gooble. */
public class GoobleGui extends Application {
    private final Gooble gooble = new Gooble("data/Gooble.txt");
    private final VBox dialogContainer = new VBox(12);
    private ScrollPane scrollPane;
    private TextField userInput;

    @Override
    public void start(Stage stage) {
        scrollPane = new ScrollPane(dialogContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        // Scroll to the latest message whenever the conversation grows.
        dialogContainer.heightProperty().addListener(
                observable -> scrollPane.setVvalue(1.0));
        userInput = new TextField();
        userInput.setPromptText("Type a command...");
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> handleInput());
        userInput.setOnAction(event -> handleInput());

        HBox inputBar = new HBox(12, userInput, sendButton);
        inputBar.setPadding(new Insets(12, 16, 14, 16));
        inputBar.getStyleClass().add("input-bar");
        sendButton.setPrefWidth(82);
        HBox.setHgrow(userInput, Priority.ALWAYS);

        Label title = new Label("Gooble");
        title.getStyleClass().add("app-title");
        Label subtitle = new Label("Your task assistant");
        subtitle.getStyleClass().add("app-subtitle");
        VBox titleBlock = new VBox(2, title, subtitle);
        titleBlock.getStyleClass().add("title-block");
        Label menu = new Label("⋯");
        menu.getStyleClass().add("menu-button");
        HBox header = new HBox(titleBlock, menu);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        HBox.setHgrow(titleBlock, Priority.ALWAYS);
        header.getStyleClass().add("app-header");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(header);
        mainLayout.setCenter(scrollPane);
        mainLayout.setBottom(inputBar);

        Scene scene = new Scene(mainLayout, 720, 650);
        scene.getStylesheets().add(getClass().getResource("/gooble.css").toExternalForm());
        stage.setTitle("Gooble");
        stage.setMinWidth(420);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.show();

        appendMessage("Hello! I'm Gooble. What can I do for you?", false, false);
        userInput.requestFocus();
    }

    private void handleInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }
        appendMessage(input, true, false);
        userInput.clear();
        StringBuilder response = new StringBuilder();
        boolean isExit = gooble.executeCommand(input, message -> appendResponse(response, message));
        String responseText = response.toString();
        appendMessage(responseText, false, isErrorResponse(responseText));
        if (isExit) {
            userInput.setDisable(true);
        }
    }

    private void appendResponse(StringBuilder response, String message) {
        if (message.isEmpty()) {
            return;
        }
        if (response.length() > 0) {
            response.append(System.lineSeparator());
        }
        response.append(message.stripTrailing());
    }

    private boolean isErrorResponse(String message) {
        return message.startsWith("Invalid command")
                || message.startsWith("Please ")
                || message.endsWith("does not exist.");
    }

    private void appendMessage(String message, boolean isUserMessage, boolean isError) {
        if (message.isEmpty()) {
            return;
        }
        dialogContainer.getChildren().add(new DialogBox(message.stripTrailing(), isUserMessage, isError));
        scrollPane.setVvalue(1.0);
    }
}
