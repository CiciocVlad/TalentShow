package talent.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import talent.model.Jurat;
import talent.services.ITalentServices;
import talent.services.TalentException;
import javafx.event.ActionEvent;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ResourceBundle;

public class Login implements Initializable {
    private ITalentServices server;
    private final Stage userPageStage = new Stage();

    @FXML
    private Stage loginStage;
    @FXML
    private TextField usernameLogin;
    @FXML
    private PasswordField passwordLogin;
    @FXML
    private Button button;
    Parent mainTalentParent;
    Jurat loggedJurat;
    private MainWindow mainCtrl;

    public void setServer(ITalentServices talentServices) {
        this.server = talentServices;
    }

    public void setStage(Stage stage) {
        this.loginStage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #1b1717; -fx-text-fill: #810000; -fx-border-radius: 60; -fx-background-radius: 60"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #810000; -fx-text-fill: #1b1717; -fx-border-radius: 60; -fx-font-size: 14; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-background-radius: 50"));
    }

    public void setMainController(MainWindow mainController) {
        this.mainCtrl = mainController;
    }

    public void setParent(Parent p) {
        mainTalentParent = p;
    }

    public void loginMethod(ActionEvent actionEvent) throws Exception {
        String username = usernameLogin.getText();
        String password = passwordLogin.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Log in failed");
            errorAlert.setContentText("Campurile nu au voie sa fie goale");
            errorAlert.showAndWait();
        }
        try {
            Jurat jurat = new Jurat(username, password);
            System.out.println(jurat);
            server.login(jurat, mainCtrl);
            loggedJurat = jurat;
            Stage stage = new Stage();
            stage.setTitle("Talent window for " + loggedJurat.getUsername());
            stage.setScene(new Scene(mainTalentParent));
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.show();
            mainCtrl.setLoggedJurat(loggedJurat);
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }
        catch (TalentException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Log in failed");
            errorAlert.setContentText(e.getLocalizedMessage());
            errorAlert.showAndWait();
        }
    }

    public String hash(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes(), 0, pass.length());
            String z = new BigInteger(1, md.digest()).toString(16);
            System.out.println(z);
            return z;
        }
        catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
}
