package controller;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Random;

class Circle {
    double x, y, vx, vy, r, id;
    double leftX, rightX, upperY, lowerY;
    Color color;
    final double maxV = .5, minR = 50, maxR = 150;

    Circle(Canvas canvas) {
        leftX = canvas.getTranslateX();
        rightX = canvas.getTranslateX() + canvas.getWidth();
        lowerY = canvas.getTranslateY();
        upperY = canvas.getTranslateY() + canvas.getHeight();

        x = rangedRandom(leftX, rightX);
        y = rangedRandom(lowerY, upperY);
        vx = rangedRandom(-maxV, maxV);
        vy = rangedRandom(-maxV, maxV);
        r = rangedRandom(minR, maxR);

        color = Color.hsb(rangedRandom(190, 230), .57, .63, rangedRandom(.7, 1));
    }

    void move() {
        x += vx;
        y += vy;
        clamp();
    }

    void clamp() {
        if (x >= rightX || x < leftX)
            vx *= -1;
        if (y >= upperY || y < lowerY)
            vy *= -1;
    }

    boolean isInRange(Circle circle) {
        double dx = x - circle.x;
        double dy = y - circle.y;
        double d = Math.sqrt(dx * dx + dy * dy);
        return d < (this.r + circle.r);
    }

    private double rangedRandom(double lower, double upper) {
        return lower + (upper - lower) * new Random().nextDouble();
    }
}

public class UIUtil {

    static void mouseEnteredEffectDark(Button button) {
        button.setStyle("-fx-background-color:  #1e5b7b; -fx-text-fill: white; -fx-border-color: transparent;");
    }

    static void mouseExitedEffectDark(Button button) {
        button.setStyle("-fx-background-color: transparent; -fx-text-fill:  white ; -fx-border-color: transparent;");
    }

    static void mouseEnteredEffectLight(Button button) {
        button.setStyle("-fx-background-color:  #2a909c; -fx-text-fill: white; -fx-border-color: transparent;");
    }

    static void mouseExitedEffectLight(Button button) {
        button.setStyle("-fx-background-color: transparent; -fx-text-fill:  black ; -fx-border-color: transparent;");
    }

    static void mouseEnteredEffectSpecial(Button button) {
        button.setStyle("-fx-background-color:  #3fbad9; -fx-text-fill: white; -fx-border-color: transparent;");
    }

    static void mouseExitedEffectSpecial(Button button) {
        button.setStyle("-fx-background-color:  #2e869e; -fx-text-fill: white; -fx-border-color: transparent;");
    }

    static void highlightButton(Button button) {
        button.setStyle("-fx-background-color:  #285482; -fx-text-fill: white; -fx-border-color: transparent;");
    }


    static TranslateTransition shake(double time, Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(time), node);
        tt.setFromY(node.getTranslateY());
        tt.setToY(node.getTranslateY() + 5);
        tt.setAutoReverse(true);
        tt.setCycleCount(4);
        return tt;
    }

    static void shakeAll(double time, Node... nodes) {
        for (Node node : nodes) {
            shake(time, node).play();
        }
    }

    private static FadeTransition fade(double time, Node node, double from, double to) {
        FadeTransition ft = new FadeTransition(Duration.millis(time), node);
        ft.setFromValue(from);
        ft.setToValue(to);
        return ft;
    }

    static FadeTransition fadeOut(double time, Node node) {
        return fade(time, node, 1, 0);
    }

    static void fadeOutAll(double time, Node... nodes) {
        for (Node node : nodes)
            fade(time, node, 1, 0).play();
    }

    static FadeTransition fadeIn(double time, Node node) {
        return fade(time, node, 0, 1);
    }

    static void fadeInAll(double time, Node... nodes) {
        for (Node node : nodes)
            fade(time, node, 0, 1).play();
    }

    static TranslateTransition move(double time, double fromX, double fromY, double toX, double toY, Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(time), node);
        tt.setFromX(fromX);
        tt.setFromY(fromY);
        tt.setToX(toX);
        tt.setToY(toY);
        tt.setInterpolator(Interpolator.LINEAR);
        return tt;
    }

    static void moveAll(double time, double fromX, double fromY, double toX, double toY, Node... nodes) {
        for (Node node : nodes)
            move(time, fromX, fromY, toX, toY, node).play();
    }

    static AnimationTimer draw(Canvas canvas, int num) {
        Circle[] circles = new Circle[num];
        for (int i = 0; i < num; i++)
            circles[i] = new Circle(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long l) {
                canvas.getGraphicsContext2D().clearRect(canvas.getTranslateX(), canvas.getTranslateY(), canvas.getWidth(), canvas.getHeight());
                gc.setLineWidth(2);

                for (Circle circle : circles) {
                    circle.move();
                }

                for (int i = 0; i < num; i++) {
                    for (int j = i + 1; j < num; j++) {
                        Circle circle1 = circles[i];
                        Circle circle2 = circles[j];
                        gc.setStroke(circle1.color);
                        gc.setFill(circle1.color);
                        if (circle1.isInRange(circle2)) {
                            gc.fillOval(circle1.x - 2, circle1.y - 2, 4, 4);
                            gc.fillOval(circle2.x - 2, circle2.y - 2, 4, 4);
                            gc.setEffect(new GaussianBlur(i * 0.5));
                            gc.strokeLine(circle1.x, circle1.y, circle2.x, circle2.y);
                            gc.setEffect(null);
                        }
                    }
                }
            }
        };
        at.start();
        return at;
    }
}
