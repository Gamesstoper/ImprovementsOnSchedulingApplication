package controller;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class mainPage implements Initializable {

    @FXML
    private AnchorPane MainScreenLabel;
    @FXML
    private Button MainCustomersButton;
    @FXML
    private Button MainAppointmentsButton;
    @FXML
    private Button MainReportsButton;
    @FXML
    private Button MainExitButton;

    Parent root;
    Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    /**MainCustomersButtonHandler.
     * This takes you to the main screen for your customer window */
    private void MainCustomersButtonHandler(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/customerMain.fxml"));
        stage = (Stage)MainCustomersButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    /**MainAppointmentsButtonHandler.
     * This takes you to the main screen for your appointments window */
    private void MainAppointmentsButtonHandler(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/appointmentMain.fxml"));
        stage = (Stage)MainCustomersButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    /**MainReportsButtonHandler.
     * This takes you to the main screen for your reports window */
    private void MainReportsButtonHandler(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/reports.fxml"));
        stage = (Stage)MainCustomersButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    /**MainExitButtonHandler.
     * This takes you out of the program */
    private void MainExitButtonHandler(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure you want to exit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

}
