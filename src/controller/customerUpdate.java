package controller;
import helper.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class customerUpdate implements Initializable {
    public TextField customerID;
    public TextField customerName;
    public TextField customerAddress;
    public TextField customerZip;
    public TextField customerPhone;
    public Label country;
    public ComboBox countryBox;
    public ComboBox divisionBox;
    public Label division;
    public Button save;
    public Button cancel;
    public ObservableList<String> divisionOptions = FXCollections.observableArrayList();
    Parent root;
    Stage stage;

    @Override
    /**initialize.
     * This sets all our combo boxes to start the page. */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            countryBox.setItems(functions.FillCountryCombo());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**sendInfo.
     * @param customer
     * This takes all the information from the prior table and gets it to this page. The function setFields will set all
     * content on the page and fill the combo boxes and text fields from the customer's information. */
    public void sendInfo(customer customer) {
        model.customer.setFields(customer, customerID, customerName, customerAddress, customerPhone, customerZip, countryBox, divisionBox);

    }

    /**FindCountryID.
     * @param selectedItem
     * @return
     * This will take the selected country from the country box and get the Country ID from the Name.*/
    public int FindCountryID(Object selectedItem) throws SQLException {
        selectedItem = countryBox.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Country Selected");
            alert.setContentText("Please select a country and try again.");
            alert.showAndWait();
        } else {
            try {
                int couID = 0;
                Object country = countryBox.getSelectionModel().getSelectedItem();
                String cou = String.valueOf(country);
                Statement statement = JDBC.connection.createStatement();

                String sqlStatement = "SELECT Country_ID FROM capstone_db.countries Where Country =" + "'" + cou + "'";

                ResultSet rs = statement.executeQuery(sqlStatement);
                while (rs.next()) {
                    couID = rs.getInt("Country_ID");

                }
                return couID;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**FindCountryID.
     * @param couID
     * This will take the country ID and fill the Division combo box based on the possible options based on the country.*/
    public void FillDivisionCombo(int couID) throws SQLException {
        Object selectedItem = countryBox.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Country Selected");
            alert.setContentText("Please select a country and try again.");
            alert.showAndWait();
        } else {
            Statement statement = JDBC.connection.createStatement();

            String sqlStatement = "SELECT Division FROM capstone_db.first_level_divisions Where Country_ID =" + couID;

            ResultSet rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                customer cus = new customer();
                cus.setDivision(rs.getString("Division"));
                divisionOptions.add(cus.getDivision());
                divisionBox.setItems(divisionOptions);
            }
        }
    }

    /**saveHandler.
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     * This takes all information from our combo boxes and text fields and inputs the information into our database. It
     * will convert all the times from Local Date Time to UTC for storage. */
    public void saveHandler(ActionEvent actionEvent) throws SQLException, IOException {
        int cusID = Integer.parseInt(customerID.getText());
        String name = customerName.getText();
        String ad = customerAddress.getText();
        String pos = customerZip.getText();
        String phone = customerPhone.getText();
        String city = (String) divisionBox.getValue();
        String cou = (String) countryBox.getValue();
        String division = String.valueOf(divisionBox.getSelectionModel().getSelectedItem());
        System.out.println(customer.getDivisionID(division));
        System.out.println(user.getUsername());
        System.out.println(cusID);

        Timestamp nowTS = dateTime.getTimeStamp();
        String errorMessage = "";

        if (name == null || name.length() == 0) {
            errorMessage += "Please enter Customer Name.\n";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Customer");
            alert.setContentText(errorMessage);
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }
        if (ad == null || ad.length() == 0) {
            errorMessage += "Please enter an address.\n";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Customer");
            alert.setContentText(errorMessage);
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        if (cou == null) {
            errorMessage += "Please Select a Country.\n";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Customer");
            alert.setContentText(errorMessage);
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }
        if (city == null) {
            errorMessage += "Please Select a City.\n";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Customer");
            alert.setContentText(errorMessage);
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        if (pos == null || pos.length() == 0) {
            errorMessage += "Please enter a valid Postal Code.\n";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Customer");
            alert.setContentText(errorMessage);
            Optional<ButtonType> result = alert.showAndWait();
            return;
        } else if (pos.length() > 10 || pos.length() < 5) {
            errorMessage += "Postal Code must be between 5 and 10 characters.\n";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Customer");
            alert.setContentText(errorMessage);
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }
        if (phone == null || phone.length() == 0) {
            errorMessage += "Please enter a Phone Number (including Area Code).";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Customer");
            alert.setContentText(errorMessage);
            Optional<ButtonType> result = alert.showAndWait();
            return;
        } else if (phone.length() != 12) {
            errorMessage += "Please enter a valid phone number (including Area Code).\n" +
                    "Please format numbers as XXX-XXX-XXXX\n";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Customer");
            alert.setContentText(errorMessage);
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }
        for (int i = 0; i <phone.length(); i++){
            if(Character.isLetter(phone.charAt(i)) || Character.isWhitespace(phone.charAt(i))){
                errorMessage += "Please only input numbers for the phone number\n";
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Invalid Phone Number");
                alert.setContentText(errorMessage);
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
        }
        if (Character.isDigit(phone.charAt(3) )|| Character.isDigit(phone.charAt(7))) {
            errorMessage += "Please format numbers as XXX-XXX-XXXX\n";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Phone Number");
            alert.setContentText(errorMessage);
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }
        else {
            PreparedStatement ps = JDBC.connection.prepareStatement("update capstone_db.customers " +
                    "set Customer_Name = '" + name + "'" + " , Address = '" + ad + "'" + " , Postal_Code ='" + pos + "'" + " ,Phone= '" + phone + "'" + " ,Division_ID = "
                    + customer.getDivisionID(division) + " ,Last_Update= '"+ nowTS + "', Last_Updated_By= '" + user.getUsername() + "'" +
                    "where Customer_ID = " + cusID);
            ps.executeUpdate();

            root = FXMLLoader.load(getClass().getResource("/view/customerMain.fxml"));
            stage = (Stage) cancel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Update Complete");
            alert1.setContentText("Customer: " + name + " has been updated.");
            alert1.showAndWait();

        }
    }

    /**cancel.
     * @param event
     * @throws IOException
     * This will cancel all changes and takes you back to the main customer page. */
    public void cancelHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setContentText("Are you sure you want to cancel this update? Nothing will be saved and you will return to the Customers screen");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            root = FXMLLoader.load(getClass().getResource("/view/customerMain.fxml"));
            stage = (Stage) cancel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Customers");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**MouseClick.
     * @param mouseEvent
     * This is used to set our DivisionBox on click. Since we have rules in place the user can't click this box until they
     * select a country meaning that this will fill once you try and set this box.*/
    public void MouseClick(MouseEvent mouseEvent) {
        try {
            FillDivisionCombo(FindCountryID(countryBox.getSelectionModel().getSelectedItem()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**MouseRelease.
     * @param mouseEvent
     * This is used to clear our list for the division options. This is to prevent the list populating the wrong options or
     * multiple of the same options. */
    public void MouseRelease(MouseEvent mouseEvent) {
        divisionOptions.clear();
    }

    /**CountryMouseClick.
     * @param mouseEvent
     * This is used to clear our division box if the user clicks on the country box. This will reset our division box to
     * ensure they have to pick a new division after they select a country. */
    public void CountryMouseClick(MouseEvent mouseEvent) {
        divisionBox.valueProperty().set(null);
    }

    /**customerIDHandler.
     * @param actionEvent
     * This is here for any future development or improvements but currently not being used */
    public void customerIDHandler(ActionEvent actionEvent) {
    }

    /**customerNameHandler.
     * @param actionEvent
     * This is here for any future development or improvements but currently not being used */
    public void customerNameHandler(ActionEvent actionEvent) {
    }

    /**customerAddressHandler.
     * @param actionEvent
     * This is here for any future development or improvements but currently not being used */
    public void customerAddressHandler(ActionEvent actionEvent) {
    }

    /**customerZipHandler.
     * @param actionEvent
     * This is here for any future development or improvements but currently not being used */
    public void customerZipHandler(ActionEvent actionEvent) {
    }

    /**customerPhoneHandler.
     * @param actionEvent
     * This is here for any future development or improvements but currently not being used */
    public void customerPhoneHandler(ActionEvent actionEvent) {
    }

    /**countryBoxHandler.
     * @param actionEvent
     * This is here for any future development or improvements but currently not being used */
    public void countryBoxHandler(ActionEvent actionEvent) {
    }

    /**divisionBoxHandler.
     * @param actionEvent
     * This is here for any future development or improvements but currently not being used */
    public void divisionBoxHandler(ActionEvent actionEvent) {
    }
}