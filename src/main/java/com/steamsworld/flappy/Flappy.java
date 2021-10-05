package com.steamsworld.flappy;

import com.sun.xml.internal.ws.addressing.WsaTubeHelperImpl;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * @author Steamworks (Steamworks#1127)
 * Monday 04 2021 (6:23 PM)
 * flappy (com.steamsworld.flappy)
 */
public class Flappy extends Application {

    public static boolean GAME_SET;

    public static void main(String[] args) {
        launch(args);
    }

    public void startGame() {
        if(!GAME_SET) {
            GAME_SET = true;

            FlappyBird game = new FlappyBird();
            Stage stage = new Stage();

            try {
                game.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(
                this.createContext()
        );

        scene.setOnKeyPressed(e -> startGame());
        scene.setOnMousePressed(e -> startGame());

        primaryStage.getIcons().add(new Image(this.getClass().getResource("/bird/main.png").toExternalForm()));
        primaryStage.setTitle("Flappy Bird");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Parent createContext() {
        Pane pane = new Pane();

        ImageView title = new ImageView(new Image(this.getClass().getResource("/title.png").toExternalForm()));
        ImageView bird = new ImageView(new Image(this.getClass().getResource("/bird/variant.png").toExternalForm()));
        Rectangle background = new Rectangle(600, 300);

        title.setFitWidth(178);
        title.setFitHeight(50);
        title.setLayoutX(200);
        title.setLayoutY(50);

        bird.setFitWidth(50);
        bird.setFitHeight(45);
        bird.setLayoutX(260);
        bird.setLayoutY(120);

        background.setFill(Color.rgb(78, 192, 202));

        Text text = new Text("Use the Space Bar or\nclick to start.");

        text.setFont(Font.font("Courier", FontWeight.EXTRA_BOLD, 25));
        text.setFill(Color.WHITE);
        text.setStroke(Color.BLACK);
        text.setLayoutX(140);
        text.setLayoutY(230);

        pane.getChildren().addAll(background, title, bird, text);
        return pane;
    }
}
