package com.steamsworld.flappy;

import com.steamsworld.flappy.objects.AudioEffect;
import com.steamsworld.flappy.objects.Pipe;
import com.steamsworld.flappy.sprites.BirdSprite;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Steamworks (Steamworks#1127)
 * Monday 04 2021 (6:34 PM)
 * flappy (com.steamsworld.flappy)
 */
public class FlappyBird extends Application {

    private final int height = 700, width = 400;
    private int score = 0;
    private long spaceClick;
    private double motion, elapsed;
    private boolean clicked, started, hitPipe, gameOver;
    private JustFeelsTight nanoTime;
    private Sprite first, second, birdSprite;
    private BirdSprite bird;
    private Text label;
    private GraphicsContext context, birdContext;
    private AnimationTimer timer;
    private List<Pipe> pipes;
    private AudioEffect coin, hit, wing, swoosh, die;
    private ImageView gameOverImage, startGame;
    private Group root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Flappy Bird");
        primaryStage.setResizable(false);

        Parent root = this.getContent();
        Scene mainScene = new Scene(root, width, height);

        setFunctions(mainScene);

        primaryStage.setScene(mainScene);
        primaryStage.show();

        startGame();
    }

    private void setFunctions(Scene scene) {
        scene.setOnKeyPressed((e) -> {
            if(e.getCode() == KeyCode.SPACE)
                jump();
        });

        scene.setOnMousePressed((e) -> jump());
    }

    private void jump() {
        if(!hitPipe) {
            clicked = true;
            if(!started) {
                root.getChildren().remove(startGame);
                swoosh.play();
                started = true;
            } else {
                wing.play();
                spaceClick = System.currentTimeMillis();
                birdSprite.setVelocity(0, -250);
            }
        }

        if(gameOver)
            startNewGame();
    }

    private Parent getContent() {
        root = new Group();

        Canvas canvas = new Canvas(width, height);
        Canvas birdCanvas = new Canvas(width, height);

        context = canvas.getGraphicsContext2D();
        birdContext = birdCanvas.getGraphicsContext2D();

        ImageView background = setBackground();

        setFloor();

        pipes = new ArrayList<>();
        setPipes();
        setBird();
        setLabels();
        setSounds();

        root.getChildren().addAll(background, canvas, birdCanvas, label, startGame);
        return root;
    }
    private ImageView setBackground() {
        ImageView view = new ImageView(new Image(this.getClass().getResource("/boards/" + (new Random().nextInt(2) > 0 ? "day" : "night") + ".png").toExternalForm()));
        view.setFitWidth(width);
        view.setFitHeight(height);
        return view;
    }

    private void setLabels() {
        label = new Text("0");
        label.setFont(Font.font("Courier", FontWeight.EXTRA_BOLD, 50));
        label.setStroke(Color.BLACK);
        label.setFill(Color.WHITE);
        label.setLayoutX(20);
        label.setLayoutY(40);

        gameOverImage = new ImageView(new Image(this.getClass().getResource("/game_over.png").toExternalForm()));
        gameOverImage.setFitWidth(178);
        gameOverImage.setFitHeight(50);
        gameOverImage.setLayoutX(110);
        gameOverImage.setLayoutY(100);

        startGame = new ImageView(new Image(this.getClass().getResource("/get_ready.png").toExternalForm()));
        startGame.setFitWidth(178);
        startGame.setFitHeight(50);
        startGame.setLayoutX(100);
        startGame.setLayoutY(100);
    }

    private void setSounds() {
        coin = new AudioEffect("/audio/score.mp3");
        hit = new AudioEffect("/audio/hit.mp3");
        wing = new AudioEffect("/audio/wing.mp3");
        swoosh = new AudioEffect("/audio/swoosh.mp3");
        die = new AudioEffect("/audio/die.mp3");
    }

    private void setBird() {
        bird = new BirdSprite();
        birdSprite = bird.getBird();
        birdSprite.render(context);
    }

    private void setFloor() {
        first = new Sprite();
        first.resize("/floor.png", 400, 140);
        first.setPosition(0, height - 100);
        first.setVelocity(-.4, 0);
        first.render(birdContext);

        second = new Sprite();
        second.resize("/floor.png", 400, 140);
        second.setPosition(first.getWidth(), height - 100);
        second.setVelocity(-.4, 0);
        second.render(context);
    }

    private void startGame() {
        nanoTime = new JustFeelsTight(System.nanoTime());

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                elapsed = (now - nanoTime.value) / 1e10;
                nanoTime.value = now;

                context.clearRect(0, 0, width, height);
                birdContext.clearRect(0, 0, width, height);
                move();
                check();

                if(started) {
                    renderPipes();
                    checkScroll();
                    updateScore();

                    if(hitPipes()) {
                        root.getChildren().add(gameOverImage);
                        stopScroll();
                        playHitSound();
                        motion += .18;
                        if(motion > .5) {
                            birdSprite.addVelocity(-200, 400);
                            birdSprite.render(context);
                            birdSprite.update(elapsed);
                            motion = 0;
                        }
                    }

                    if(hitFloor()) {
                        if(!root.getChildren().contains(gameOverImage)) {
                            root.getChildren().add(gameOverImage);
                            playHitSound();
                            showHitEffect();
                        }
                        timer.stop();
                        gameOver = true;
                        die.play();
                    }
                }
            }
        };
        timer.start();
    }

    private void startNewGame() {
        root.getChildren().remove(gameOverImage);
        root.getChildren().add(startGame);

        pipes.clear();
        setFloor();
        setPipes();
        setBird();

        reset();
        startGame();
    }

    private void reset() {
        updateLabel(0);

        score = 0;

        hitPipe = false;
        clicked = false;
        gameOver = false;
        started = false;
    }

    private void check() {
        long difference = (System.currentTimeMillis() - spaceClick) / 300;
        if(difference >= .001 && clicked) {
            clicked = false;
            birdSprite.addVelocity(0, 800);
            birdSprite.render(birdContext);
            birdSprite.update(elapsed);
        } else animate();
    }

    private void updateScore() {
        if(!hitPipe) {
            for(Pipe pipe : pipes) {
                if(pipe.getSprite().getPosX() == birdSprite.getPosX()) {
                    updateLabel(++score);
                    coin.play();
                    break;
                }
            }
        }
    }

    private void updateLabel(int score) {
        label.setText(score + "");
    }

    private void move() {
       first.render(context);
       second.render(context);

       first.update(5);
       second.update(5);

       Sprite sprite = (first.getPosX() <= -width) ? first : second;
       sprite.setPosition(sprite.getPosX() + sprite.getWidth(), height - 100);
    }

    private void animate() {
        birdSprite.render(birdContext);
        birdSprite.update(elapsed);

        motion += .18;
        if(motion > 0.5 && clicked) {
            Sprite temp = birdSprite;
            birdSprite = bird.animate();
            birdSprite.setPosition(temp.getPosX(), temp.getPosY());
            birdSprite.setVelocity(temp.getVelX(), temp.getVelY());
            motion = 0;
        }
    }

    private boolean hitPipes() {
        for(Pipe pipe : pipes) {
            if(!hitPipe && birdSprite.doesIntersect(pipe.getSprite())) {
                hitPipe = true;
                showHitEffect();
                return true;
            }
        }
        return false;
    }

    private void showHitEffect() {
        ParallelTransition transition = new ParallelTransition();
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(.1), root);
        fadeTransition.setToValue(0);
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);
        transition.getChildren().add(fadeTransition);
        transition.play();
    }

    private void playHitSound() {
        hit.play();
    }

    private boolean hitFloor() {
        return birdSprite.doesIntersect(first) || birdSprite.doesIntersect(second) || birdSprite.getPosX() < 0;
    }

    private void stopScroll() {
        for(Pipe pipe : pipes)
            pipe.getSprite().setVelocity(0, 0);
        first.setVelocity(0, 0);
        second.setVelocity(0, 0);
    }

    private void checkScroll() {
        if(pipes.size() > 0) {
            Sprite pipe = pipes.get(pipes.size() - 1).getSprite();
            if(pipe.getPosX() == (double) (width / 2) - 80)
                setPipes();
            else if(pipe.getPosX() <= -pipe.getWidth()) {
                pipes.remove(0);
                pipes.remove(0);
            }
        }
    }

    private void setPipes() {
        int height = getRandomPipeHeight();

        Pipe pipe = new Pipe(true, height);
        Pipe down = new Pipe(false, 425 - height);

        pipe.getSprite().setVelocity(-.4, 0);
        down.getSprite().setVelocity(-.4, 0);

        pipe.getSprite().render(context);
        down.getSprite().render(context);

        pipes.addAll(Arrays.asList(pipe, down));
    }

    private int getRandomPipeHeight() {
        return (int) (Math.random() * (410 - 25)) + 25;
    }

    private void renderPipes() {
        for(Pipe pipe : pipes) {
            Sprite sprite = pipe.getSprite();
            sprite.render(context);
            sprite.update(5);
        }
    }

    @AllArgsConstructor
    class JustFeelsTight {
        long value;
    }
}
