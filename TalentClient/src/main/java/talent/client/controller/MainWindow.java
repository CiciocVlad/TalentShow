package talent.client.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Node;
import talent.model.Concurent;
import talent.model.Jurat;
import talent.model.Result;
import talent.services.ITalentObserver;
import talent.services.ITalentServices;
import talent.services.TalentException;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainWindow extends UnicastRemoteObject implements ITalentObserver, Serializable {
    public Button addResult;
    @FXML
    public Button logOutBtn;

    private ITalentServices server;
    private Jurat loggedJurat;
    @FXML
    Stage mainPageStage;
    private Stage stage = new Stage();
    private final Stage login = new Stage();
    private Parent child;

    @FXML
    private TableView<Concurent> concurentiTableView;
    @FXML
    private TableColumn<Concurent, String> columnConcurent;
    @FXML
    private TableColumn<Result, String> columnResult;

    ObservableList<Concurent> modelConcurent = FXCollections.observableArrayList();
    ObservableList<Result> modelResult = FXCollections.observableArrayList();

    public MainWindow() throws RemoteException { }

    public void setChild(Parent child) {
        this.child = child;
    }

    public void setLoggedJurat(Jurat jurat) {
        this.loggedJurat = jurat;

        List<Concurent> list;
        List<Result> listResult;
        try {
            list = StreamSupport.stream(server.getAllConcurenti().spliterator(), false).collect(Collectors.toList());
            modelConcurent.setAll(list);

            listResult = StreamSupport.stream(server.getAllResults().spliterator(), false).collect(Collectors.toList());
            modelResult.setAll(listResult);

            initialize();
        }
        catch (TalentException e) {
            System.out.println("Eroare get all concurenti initial");
        }
    }

    public void setServer(ITalentServices talentServices) {
        this.server = talentServices;
    }

    @FXML
    public void initialize() {
        columnConcurent.setCellValueFactory(new PropertyValueFactory<Concurent, String>("Name"));
        columnResult.setCellValueFactory(new PropertyValueFactory<Result, String>("Result"));
        concurentiTableView.setItems(modelConcurent);
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        server.logout(loggedJurat, this);
        Stage scene = (Stage)logOutBtn.getScene().getWindow();
        scene.close();
    }

    public void addResultYes(ActionEvent actionEvent) {
        Concurent concurent = concurentiTableView.getSelectionModel().getSelectedItem();
        if (concurent == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error 404");
            errorAlert.setContentText("Please select a contestant");
            errorAlert.showAndWait();
        }
        else {
            Result result = new Result(concurent, loggedJurat, "Yes");
            try {
                server.addResult(result, loggedJurat);
            }
            catch (TalentException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Error writing result");
                errorAlert.setContentText(e.getLocalizedMessage());
                errorAlert.showAndWait();
            }
        }
    }

    public void addResultNo(ActionEvent actionEvent) {
        Concurent concurent = concurentiTableView.getSelectionModel().getSelectedItem();
        if (concurent == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error 404");
            errorAlert.setContentText("Please select a contestant");
            errorAlert.showAndWait();
        }
        else {
            Result result = new Result(concurent, loggedJurat, "No");
            try {
                server.addResult(result, loggedJurat);
            }
            catch (TalentException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Error writing result");
                errorAlert.setContentText(e.getLocalizedMessage());
                errorAlert.showAndWait();
            }
        }
    }

    @Override
    public void newResultAdded(Result result) throws TalentException {
        try {
            List<Concurent> list = StreamSupport.stream(server.getAllConcurenti().spliterator(), false).collect(Collectors.toList());
            System.out.println("Concurenti client: ");
            for (Concurent i : list)
                System.out.println(i);
            modelConcurent.setAll(list);
        }
        catch (TalentException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("pusca");
            errorAlert.setContentText(e.getLocalizedMessage());
            errorAlert.showAndWait();
        }
    }
}
