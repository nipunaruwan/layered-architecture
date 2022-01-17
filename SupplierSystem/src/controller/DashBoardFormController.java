package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.ValidationUtil;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class DashBoardFormController {
    public Label lblDate;
    public Label lblTime;
    public AnchorPane rootContext;
    public Label lblMenu;
    public Label lblDescription;
    public ImageView imgAdmin;
    public ImageView imgCashier;
    public ImageView imgIcon;
    public JFXTextField Username;
    public JFXTextField password;
    public JFXButton btnLogin;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    Pattern titleRegEx = Pattern.compile("^[A-z]{1,5}$");
    Pattern postalCodeRegEx = Pattern.compile("^[0-9]{4,9}$");

    public void initialize() {
        loadDateAndTime();
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), rootContext);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        btnLogin.setDisable(true);
        storeValidate();
    }

    private void storeValidate() {
        map.put(Username, titleRegEx);
        map.put(password, postalCodeRegEx);


    }

    private void loadDateAndTime() {
        //load date
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        //load time
        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(
                    currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    /*public void playMouseEnterAnimation(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();

            switch (icon.getId()) {
                case "imgAdmin":
                    lblMenu.setText("Admin Form");
                    lblDescription.setText("Add Items and Manage Reports.");
                    break;

                case "imgCashier":
                    lblMenu.setText("Cashier's Form");
                    lblDescription.setText("Add Customers and Order Placing.");
                    break;
            }

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }*/

    public void playMouseExitAnimation(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
            lblMenu.setText("Welcome To SuperMarket System");
           // lblDescription.setText("Please select one of above main operations to proceed");
        }
    }

    /*public void navigate(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();

            Parent root = null;

            switch (icon.getId()) {
                case "imgAdmin":
                    root = FXMLLoader.load(getClass().getResource("/views/AdminViewForm.fxml"));
                    break;

                case "imgCashier":
                    root = FXMLLoader.load(getClass().getResource("/views/CashierForm.fxml"));
                    break;
            }

            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.rootContext.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();

            }
        }
    }
*/
    public void aboutUsOnAction(MouseEvent mouseEvent) throws IOException {
        URL resource = getClass().getResource("../views/AboutUsForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setFullScreen(false);
        stage.setTitle("About Us | Supermarket System v0.1.0 | MINDARTLK");
        stage.setResizable(false);
        stage.show();
    }

    public void exitOnAction(MouseEvent mouseEvent) {
        ButtonType buttonType = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonType1 = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Did you exit the system?", buttonType, buttonType1);
        alert.setTitle("Confirmation");
        Optional<ButtonType> close = alert.showAndWait();
        if (close.orElse(buttonType1) == buttonType) {
            Platform.exit();
            System.exit(0);
        }
    }

    public void btnLogin(ActionEvent actionEvent) throws IOException {
        LoginFormManager();
    }

    private void LoginFormManager() throws IOException {
        if (Username.getText().equals("admin") && password.getText().equals("1234")) {
            Stage window = (Stage) rootContext.getScene().getWindow();
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../views/AdminViewForm.fxml"))));

// manager
        } else if (Username.getText().equals("c") && password.getText().equals("1234")) {
            Stage window = (Stage) rootContext.getScene().getWindow();
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../views/CashierForm.fxml"))));
// cashier
        } else {
            new Alert(Alert.AlertType.WARNING, "Please Try Again...").show();
        }
    }

    public void KeyRelease(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(map, btnLogin);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                // new Alert(Alert.AlertType.INFORMATION, "Added").showAndWait();
            }
        }

    }
}