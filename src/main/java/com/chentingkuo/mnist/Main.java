package com.chentingkuo.mnist;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas; // draw digit (0-9)
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox; // vertical layout
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Insets;
import javafx.scene.text.Font;

import javafx.embed.swing.SwingFXUtils; // JavaFX ↔ BufferedImage
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;

import okhttp3.*; // OkHttp HTTP client
import org.json.JSONObject;

// inheritance Application, JavaFx base class for entry
public class Main extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private Label resultLabel;
    private Label instructionLabel;

    // Override the start method in JavaFx, entry
    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(280, 280);
        gc = canvas.getGraphicsContext2D();
        clearCanvas(); // BLACK background
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(10); // 10 pixel pen(white) to draw the digit

        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);

        // reminder
        instructionLabel = new Label("Please draw a digit (0-9)");
        instructionLabel.setTextFill(Color.BLUE);
        instructionLabel.setFont(new Font("Arial", 16));

        // Predict button, call predictDigit method
        Button predictButton = new Button("Predict");
        predictButton.setFont(new Font("Arial", 14));
        predictButton.setOnAction(event -> predictDigit());

        // Clear button
        Button clearButton = new Button("Clear");
        clearButton.setFont(new Font("Arial", 14));
        clearButton.setOnAction(event -> {
            clearCanvas();
            resultLabel.setText("Result: "); // reset result
        });

        // Result button
        resultLabel = new Label("Result: ");
        resultLabel.setFont(new Font("Arial", 16));

        // Display all the components vertically with 15px interval space
        VBox root = new VBox(15, instructionLabel, resultLabel, canvas, predictButton, clearButton);// top, right, bottom, left
        root.setPadding(new Insets(5, 20, 20, 20)); // top, right, bottom, left

        Scene scene = new Scene(root);
        primaryStage.setTitle("Digit Recognition");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private double lastX, lastY;
    // save the coordinates that user press the mouse, and save to (lastX, lastY)
    private void handleMousePressed(MouseEvent event) {
        lastX = event.getX();
        lastY = event.getY();
    }
    private void handleMouseDragged(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        // draw line from (lastX, lastY) to (x, y)
        gc.strokeLine(lastX, lastY, x, y);
        // update the (lastX, lastY) to current (x, y), for draw the new line
        lastX = x;
        lastY = y;
    }

    private void predictDigit() {
        try {
            // JavaFX WritableImage -> AWT BufferedImage
            BufferedImage fullImage = SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null);
            // check is the canvas empty
            if (isCanvasEmpty(fullImage)) {
                resultLabel.setText("Please draw a digit before predicting");
                return;
            }

            // cropped the smallest bounding box contain handwriting
            BufferedImage gray = toGray(fullImage);
            int[] box = findBoundingBox(gray); // v > 128 pixel, return [minX, minY, width, height]
            BufferedImage cropped = gray.getSubimage(box[0], box[1], box[2], box[3]);

            // resize the cropped handwritten region to 20x20 pixels using Nearest‐Neighbor
            // since the mnist image size is 28x28, and the digits are about 20x20 in the center
            BufferedImage resized20 = new BufferedImage(20, 20, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g20 = resized20.createGraphics();
            g20.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g20.drawImage(cropped, 0, 0, 20, 20, null);
            g20.dispose();

            // create a new 28x28 gray image and put cropped(20x20) in the center, padding 4
            // imitate the mnist dataset
            BufferedImage final28 = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g28 = final28.createGraphics();
            g28.setColor(java.awt.Color.BLACK);
            g28.fillRect(0, 0, 28, 28);
            g28.drawImage(resized20, 4, 4, null);
            g28.dispose();

            // enhance contrast (threshold = 128)
            // if v > 128, set this pixel to 255. Otherwise, bw = 0
            for (int x = 0; x < 28; x++) {
                for (int y = 0; y < 28; y++) {
                    int v = final28.getRaster().getSample(x, y, 0);
                    int bw = (v > 128 ? 255 : 0);
                    // bw -> 0 or 255
                    final28.getRaster().setSample(x, y, 0, bw);
                }
            }

            File outputFile = new File("temp_images/drawn_image.png");
            outputFile.getParentFile().mkdirs();
            ImageIO.write(final28, "png", outputFile);

            // client is an instance object of OkHttpClient
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", outputFile.getName(),
                            RequestBody.create(outputFile, MediaType.parse("image/png")))
                    .build();
            Request request = new Request.Builder()
                    .url("http://localhost:5050/predict")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();

            JSONObject json = new JSONObject(result);
            int digit = json.getInt("digit");
            double confidence = json.getDouble("confidence");

            // Show result, and check the confidence if it's too low
            if (confidence < 0.5) {
                resultLabel.setText("This might not be a digit (0-9). Please try again");
            } else {
                resultLabel.setText("Result: Digit = " + digit + ", Confidence = " + String.format("%.2f%%", confidence * 100));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Error: " + e.getMessage());
        }
    }

    // toGray
    private BufferedImage toGray(BufferedImage src) {
        BufferedImage gray = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = gray.createGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return gray;
    }

    private int[] findBoundingBox(BufferedImage img) {
        int w = img.getWidth(), h = img.getHeight();
        int minX=w, minY=h, maxX=0, maxY=0;
        for (int x=0; x<w; x++) {
            for (int y=0; y<h; y++) {
                int v = img.getRaster().getSample(x, y, 0);
                if (v > 128) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }
        if (maxX < minX) {
            return new int[]{0,0,w,h};
        }
        return new int[]{minX, minY, maxX-minX+1, maxY-minY+1};
    }

    // check canvas if empty
    private boolean isCanvasEmpty(BufferedImage image) {
        BufferedImage gray = toGray(image);
        for (int x = 0; x < gray.getWidth(); x++) {
            for (int y = 0; y < gray.getHeight(); y++) {
                if (gray.getRaster().getSample(x, y, 0) > 16) {
                    return false;
                }
            }
        }
        return true;
    }

    private void clearCanvas() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public static void main(String[] args){
        launch(args);
    }
}
