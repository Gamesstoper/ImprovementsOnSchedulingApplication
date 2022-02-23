package controller;
import helper.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class appointmentMain implements Initializable {
    public RadioButton radioWeek;
    public RadioButton radioMonth;
    public Button addAppointment;
    public Button deleteAppointment;
    public Button updateAppointment;
    public Button back;
    public TableColumn <appointment, Integer> appointmentIDColumn;
    public TableColumn <appointment, String> appointmentTitleColumn;
    public TableColumn <appointment, String> appointmentDescriptionColumn;
    public TableColumn <appointment, String> appointmentLocationColumn;
    public TableColumn <appointment, String> appointmentContactColumn;
    public TableColumn <appointment, String> appointmentTypeColumn;
    public TableColumn <appointment, String> startTimeColumn;
    public TableColumn <appointment, String> endTimeColumn;
    public TableColumn <appointment, String> customerNameColumn;
    public TableColumn <appointment, String> userNameColumn;
    public Button exit;
    public TableView <appointment> appointmentTableView;
    public RadioButton showAll;
    private ToggleGroup RadioButtonToggleGroup;
    private boolean isWeekly;
    ObservableList<appointment> appointmentList = FXCollections.observableArrayList();
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Parent root;
    Stage stage;

    @Override
    /**initialize.
     * This sets our table, radio button defaults */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PropertyValueFactory<appointment, Integer> id = new PropertyValueFactory<>("appointment_ID");
        PropertyValueFactory<appointment, String> title = new PropertyValueFactory<>("appointmentTitle");
        PropertyValueFactory<appointment, String> des = new PropertyValueFactory<>("appointmentDes");
        PropertyValueFactory<appointment, String> location = new PropertyValueFactory<>("appointmentLoc");
        PropertyValueFactory<appointment, String> contact = new PropertyValueFactory<>("contactName");
        PropertyValueFactory<appointment, String> type = new PropertyValueFactory<>("appointmentType");
        PropertyValueFactory<appointment, String> start = new PropertyValueFactory<>("startTime");
        PropertyValueFactory<appointment, String> end = new PropertyValueFactory<>("endTime");
        PropertyValueFactory<appointment, String> customer = new PropertyValueFactory<>("customerName");
        PropertyValueFactory<appointment, String> user = new PropertyValueFactory<>("UserName");

        appointmentIDColumn.setCellValueFactory(id);
        appointmentTitleColumn.setCellValueFactory(title);
        appointmentDescriptionColumn.setCellValueFactory(des);
        appointmentLocationColumn.setCellValueFactory(location);
        appointmentContactColumn.setCellValueFactory(contact);
        appointmentTypeColumn.setCellValueFactory(type);
        startTimeColumn.setCellValueFactory(start);
        endTimeColumn.setCellValueFactory(end);
        customerNameColumn.setCellValueFactory(customer);
        userNameColumn.setCellValueFactory(user);

        RadioButtonToggleGroup = new ToggleGroup();
        showAll.setToggleGroup(RadioButtonToggleGroup);
        radioWeek.setToggleGroup(RadioButtonToggleGroup);
        radioMonth.setToggleGroup(RadioButtonToggleGroup);
        radioWeek.setSelected(true);
        radioMonth.setSelected(false);
        showAll.setSelected(false);
        isWeekly = true;
        try{
            setAppointmentTable();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**setAppointmentTable.
     * @throws SQLException
     * This sets our table with information. It will add all data into a list, then set it to another method for filtering. */
    public void setAppointmentTable() throws SQLException {
        PreparedStatement ps;
        try{
            ps = JDBC.connection.prepareStatement("Select * from capstone_db.appointments");
            ResultSet rs =ps.executeQuery();
            appointmentList.clear();;

            while(rs.next()) {
                appointment a = new appointment();
                a.setAppointment_ID(rs.getInt("Appointment_ID"));
                a.setAppointmentTitle(rs.getString("Title"));
                a.setAppointmentDes(rs.getString("Description"));
                a.setAppointmentLoc(rs.getString("Location"));

                a.setAppointmentType(rs.getString("Type"));
                a.setCustomerName(functions.getCustomer(rs.getInt("Customer_ID")));
                a.setUserName(functions.getUser(rs.getInt("User_ID")));
                a.setContactName(functions.getContact(rs.getInt("Contact_ID")));

                String startUTC = rs.getString("Start");
                String endUTC = rs.getString("End");

                LocalDateTime utcStartlocal = LocalDateTime.parse(startUTC, datetimeDTF);
                LocalDateTime utcEndlocal = LocalDateTime.parse(endUTC, datetimeDTF);

                String start = functions.utcToLocal(utcStartlocal);
                String end = functions.utcToLocal(utcEndlocal);

                a.setStartTime(start);
                a.setEndTime(end);

                appointmentList.addAll(a);
                    if (isWeekly) {
                        filterAppointmentsByWeek(appointmentList);
                    } else {
                        filterAppointmentsByMonth(appointmentList);
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**radioWeekHandler.
     * @param event
     * @throws SQLException
     * This sets our Radio button for weekly sorting and then sets the table. */
    public void radioWeekHandler(ActionEvent event) throws SQLException {
        isWeekly = true;
        setAppointmentTable();
    }
    /**radioMonthHandler.
     * @param event
     * @throws SQLException
     * This sets our Radio button for monthly sorting and then sets the table. */
    public void radioMonthHandler(ActionEvent event) throws SQLException {
        isWeekly = false;
        setAppointmentTable();
    }
    /**addAppointmentHandler.
     * @param event
     * This is the button that takes you to add a new appointment. */
    public void addAppointmentHandler(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/appointmentAdd.fxml"));
        stage = (Stage) addAppointment.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
    }
    /**deleteAppointmentHandler.
     * @param event
     * @throws SQLException
     * This is the button that first verifies that you have selected an appointment from the table, deletes the appointment you have
     * chosen, presents you a message advising you of the appointment you deleted, then resets the table.*/
    public void deleteAppointmentHandler(ActionEvent event) throws SQLException {
        appointment sa = appointmentTableView.getSelectionModel().getSelectedItem();
        if (sa == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Appointment Selected");
            alert.setContentText("Please select an appointment and try again.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete All Data");
            alert.setContentText("Are you sure you want to delete this appointment?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                appointment selected = appointmentTableView.getSelectionModel().getSelectedItem();
                int appointmentID = selected.getAppointment_ID();
                String type = selected.getAppointmentType();
                System.out.println(appointmentID);

                PreparedStatement ps = JDBC.connection.prepareStatement("DELETE FROM capstone_db.appointments WHERE Appointment_ID=" + appointmentID);
                ps.executeUpdate();

                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Delete Complete");
                alert1.setContentText("Appointment ID: " + appointmentID + " and Type: " + type +" has been deleted.");
                alert1.showAndWait();

                appointmentList.clear();
                radioWeek.setSelected(true);
                radioMonth.setSelected(false);
                showAll.setSelected(false);
                setAppointmentTable();

            }
        }
    }

    /**updateAppointmentHandler.
     * @param event
     * This is the button that takes you to update a selected appointment. It will call the method to send the data from the
     * appointment you selected and if you fail to select an appointment before clicking it will give you an error.*/
    public void updateAppointmentHandler(ActionEvent event) {
        appointment sc = appointmentTableView.getSelectionModel().getSelectedItem();
        if (sc == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Appointment Selected");
            alert.setContentText("Please select an appointment and try again.");
            alert.showAndWait();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/appointmentUpdate.fxml"));
                root = loader.load();
                appointmentUpdate au = loader.getController();
                au.sendInfo(appointmentTableView.getSelectionModel().getSelectedItem());
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 800, 600);
                stage.setTitle("Update Appointment");
                stage.setScene(scene);
                stage.show();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**back.
     * @param event
     * @throws IOException
     * This will take you back to the main page of menus.*/
    public void back(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/mainPage.fxml"));
        stage = (Stage) back.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }
    /**exitHandler.
     * @param event
     * This will exit the program */
    public void exitHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure you want to exit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
    /**filterAppointmentsByWeek.
     * @param appointmentList
     * This will Filter you appointments by week. I used a Lambda expression to organize my from my list into a filtered list
     * this made it much more efficient than running an entire loop to find the current information. It will then set the table
     * with the proper information.*/
    public void filterAppointmentsByWeek(ObservableList appointmentList) {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus1Week = now.plusWeeks(1);

        /**Lambda*/
        FilteredList<appointment> filteredData = new FilteredList<>(appointmentList);
        filteredData.setPredicate(row -> {

            LocalDate rowDate = LocalDate.parse(row.getStartTime(), datetimeDTF);

            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlus1Week);
        });

        appointmentTableView.setItems(filteredData);
    }
    /**filterAppointmentsByMonth.
     * @param appointmentList
     * This will Filter you appointments by week. I used a Lambda expression to organize my from my list into a filtered list
     * this made it much more efficient than running an entire loop to find the current information. It will then set the table
     * with the proper information. */
    public void filterAppointmentsByMonth(ObservableList appointmentList){
        LocalDate now = LocalDate.now();
        LocalDate nowPlus1Month = now.plusMonths(1);

        /**Lambda*/
        FilteredList<appointment> filteredData = new FilteredList<>(appointmentList);
        filteredData.setPredicate(row -> {

            LocalDate rowDate = LocalDate.parse(row.getStartTime(), datetimeDTF);

            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlus1Month);
        });

        appointmentTableView.setItems(filteredData);
    }


    /**showAll.
     * @param event
     * This will show all appointments when you click on the button no matter the time. */
    public void showAll(ActionEvent event) {
        appointmentTableView.setItems(appointmentList);
    }
}
