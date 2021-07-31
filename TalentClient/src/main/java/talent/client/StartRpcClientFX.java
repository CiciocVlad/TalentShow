package talent.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import talent.client.controller.Login;
import talent.client.controller.MainWindow;
import talent.services.ITalentServices;

public class StartRpcClientFX extends Application {
    public void start(Stage primaryStage) throws Exception {
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
        ITalentServices server = (ITalentServices) factory.getBean("talentService");

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
        Parent root = loader.load();

        Login ctrl = loader.getController();
        ctrl.setServer(server);

        FXMLLoader cloader = new FXMLLoader(getClass().getClassLoader().getResource("mainWindow.fxml"));
        Parent croot = cloader.load();

        MainWindow mainCtrl = cloader.getController();

        mainCtrl.setServer(server);

        ctrl.setMainController(mainCtrl);
        ctrl.setParent(croot);
        mainCtrl.setChild(root);
        primaryStage.setTitle("Talent caritabil");
        primaryStage.setScene(new Scene(root, 294, 357));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
