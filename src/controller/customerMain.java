package controller;
import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import static model.customer.getAllCustomers;

public class customerMain implements Initializable {
    public TableColumn<customer, Integer> customerIDCol;
    public TableColumn<customer, String> customerNameCol;
    public TableColumn<customer, String> customerPhoneCol;
    public TableColumn<customer, String> customerAddressCol;
    public TableColumn<customer, String> customerZipCol;
    public TableColumn<customer, String> customerDivisionCol;
    public TableColumn<customer, String> customerCountryCol;
    public Button update;
    public Button add;
    public Button back;
    public Button exit;
    public TableView<customer> customerTable;
    public Button delete;
    public TextField search;
    Parent root;
    Stage stage;
    public int code;


    @Override
    /**initialize.
     * This sets all our combo boxes and sets the tables to start the page. */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PropertyValueFactory<customer, String> name = new PropertyValueFactory<>("customerName");
        PropertyValueFactory<customer, String> phone = new PropertyValueFactory<>("phone");
        PropertyValueFactory<customer, String> address = new PropertyValueFactory<>("address");
        PropertyValueFactory<customer, String> zip = new PropertyValueFactory<>("zip");
        PropertyValueFactory<customer, String> division = new PropertyValueFactory<>("division");
        PropertyValueFactory<customer, String> country = new PropertyValueFactory<>("country");
        PropertyValueFactory<customer, Integer> id = new PropertyValueFactory<>("customerID");
        customerIDCol.setCellValueFactory(id);
        customerNameCol.setCellValueFactory(name);
        customerPhoneCol.setCellValueFactory(phone);
        customerAddressCol.setCellValueFactory(address);
        customerZipCol.setCellValueFactory(zip);
        customerDivisionCol.setCellValueFactory(division);
        customerCountryCol.setCellValueFactory(country);

        try {
            customerTable.setItems(getAllCustomers());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**updateHandler.
     * @param actionEvent
     * This grabs the selected customer and sends the data to the update form. This will error if no customer is selected */
    public void updateHandler(ActionEvent actionEvent){
        customer sc = customerTable.getSelectionModel().getSelectedItem();

        if (sc.getCustomerID() == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Personal Customer Selected");
            alert.setContentText("Please select a different customer. This is saved for personal appointments.");
            alert.showAndWait();
            return;
        }
        if (sc == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Customer Selected");
            alert.setContentText("Please select a customer and try again.");
            alert.showAndWait();
            return;
        }
        else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customerUpdate.fxml"));
                root = loader.load();
                customerUpdate cu = loader.getController();
                cu.sendInfo(customerTable.getSelectionModel().getSelectedItem());
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 800, 600);
                stage.setTitle("Update Customer");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**addHandler.
     * @param actionEvent
     * This opens the addCustomer page */
    public void addHandler(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/customerAdd.fxml"));
        stage = (Stage) add.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
    }

    /**backHandler.
     * @param actionEvent
     * @throws IOException
     * This goes back to the mainPage*/
    public void backHandler(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/mainPage.fxml"));
        stage = (Stage) back.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    /**exitHandler.
     * @param event
     * This exits the program*/
    public void exitHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure you want to exit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**deleteHandler.
     * @param event
     * @throws SQLException
     * This deletes the selected customer. It deletes all appointments associated with the customer then the customer information.
     * It will present an error if the user does not select a customer first.*/
    public void deleteHandler(ActionEvent event) throws SQLException {
        customer sc = customerTable.getSelectionModel().getSelectedItem();
        if (sc.getCustomerID() == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Personal Customer Selected");
            alert.setContentText("Please select a different customer. This is saved for personal appointments.");
            alert.showAndWait();
            return;
        }
        if (sc == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Customer Selected");
            alert.setContentText("Please select a customer and try again.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete All Data");
            alert.setContentText("Are you sure you want to delete this customer and all data, This includes all appointments");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                customer selected = customerTable.getSelectionModel().getSelectedItem();
                int cusID = selected.getCustomerID();
                System.out.println(cusID);
                String cusName = selected.getCustomerName();

                PreparedStatement ps = JDBC.connection.prepareStatement("DELETE FROM capstone_db.appointments WHERE Customer_ID=" + cusID);
                ps.executeUpdate();

                PreparedStatement ts = JDBC.connection.prepareStatement("DELETE FROM capstone_db.customers WHERE Customer_ID=" + cusID);
                ts.executeUpdate();

                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Delete Complete");
                alert1.setContentText("Customer: " + cusName +" has been deleted.");
                alert1.showAndWait();

                customerTable.setItems(getAllCustomers());

            }
        }
    }
    /**searchButton.
     * @param event
     * @throws SQLException
     * This searches through the Customer table and updates the table with what it finds. It searches for customer name and ID
     * it can be partial or full. */
    public void searchButton(ActionEvent event) throws SQLException {
        String customer = search.getText();
        customerTable.setItems(model.customer.lookupCustomer(customer));
        ObservableList<customer> c = customerTable.getItems();
        if (c.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer Not Found");
            alert.setContentText("Unable to find customer name or ID, please try another search.");
            alert.showAndWait();
        }
    }
}
