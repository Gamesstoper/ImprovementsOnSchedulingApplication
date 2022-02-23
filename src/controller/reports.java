package controller;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class reports implements Initializable {
    public Tab TypeByMonth;
    public TableColumn monthColumn;
    public TableColumn consultationColumn;
    public TableColumn workoutColumn;
    public TableColumn personalColumn;
    public TableColumn customerMeetingColumn;
    public TableView TypesTable;
    public Tab ContactSchedule;
    public TableView appointmentTableView;
    public TableColumn appointmentIDColumn;
    public TableColumn appointmentTitleColumn;
    public TableColumn appointmentDescriptionColumn;
    public TableColumn appointmentTypeColumn;
    public TableColumn startTimeColumn;
    public TableColumn endTimeColumn;
    public Tab CreatedByUser;
    public TableView LastCreatedTable;
    public TableColumn usernameColumn;
    public TableColumn dateColumn;
    public TableColumn appIDColumn;
    public TableColumn contactColumn;
    public Button back;
    public ComboBox contactBox;
    private final ZoneId utcZoneID = ZoneId.of("UTC");
    private final ZoneId localZoneID = ZoneId.systemDefault();
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ObservableList<appointment> appointmentList = FXCollections.observableArrayList();
    private final ObservableList<model.reports> lastList = FXCollections.observableArrayList();
    private final ObservableList<model.reports> monthsList = FXCollections.observableArrayList();
    public TableColumn customerID;
    public TableColumn nutritionMeetingColumn;
    public TableColumn companyMeetingColumn;
    Parent root;
    Stage stage;

    private int monthTypes[][] = new int[][]{
            {0, 0, 0, 0,0,0},
            {0, 0, 0, 0,0,0},
            {0, 0, 0, 0,0,0},
            {0, 0, 0, 0,0,0},
            {0, 0, 0, 0,0,0},
            {0, 0, 0, 0,0,0},
            {0, 0, 0, 0,0,0},
            {0, 0, 0, 0,0,0},
            {0, 0, 0, 0,0,0},
            {0, 0, 0, 0,0,0},
            {0, 0, 0, 0,0,0},
            {0, 0, 0, 0,0,0}
    };


    @Override
    /**initialize.
     * This sets all our combo boxes and tables to start the page. */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        PropertyValueFactory<appointment, Integer> id = new PropertyValueFactory<>("appointment_ID");
        PropertyValueFactory<appointment, String> title = new PropertyValueFactory<>("appointmentTitle");
        PropertyValueFactory<appointment, String> des = new PropertyValueFactory<>("appointmentDes");
        PropertyValueFactory<appointment, String> type = new PropertyValueFactory<>("appointmentType");
        PropertyValueFactory<appointment, String> start = new PropertyValueFactory<>("startTime");
        PropertyValueFactory<appointment, String> end = new PropertyValueFactory<>("endTime");
        PropertyValueFactory<appointment, Integer> cusID = new PropertyValueFactory<>("customerID");

        appointmentIDColumn.setCellValueFactory(id);
        appointmentTitleColumn.setCellValueFactory(title);
        appointmentDescriptionColumn.setCellValueFactory(des);
        appointmentTypeColumn.setCellValueFactory(type);
        startTimeColumn.setCellValueFactory(start);
        endTimeColumn.setCellValueFactory(end);
        customerID.setCellValueFactory(cusID);

        PropertyValueFactory<model.reports, String> months = new PropertyValueFactory<>("months");
        PropertyValueFactory<model.reports, Integer> consultation = new PropertyValueFactory<>("consultation");
        PropertyValueFactory<model.reports, Integer> workout = new PropertyValueFactory<>("workout");
        PropertyValueFactory<model.reports, Integer> personal = new PropertyValueFactory<>("personal");
        PropertyValueFactory<model.reports, Integer> newCustomerMeeting = new PropertyValueFactory<>("newCustomerMeeting");
        PropertyValueFactory<model.reports, Integer> nutritionMeeting = new PropertyValueFactory<>("nutritionMeeting");
        PropertyValueFactory<model.reports, Integer> companyMeeting = new PropertyValueFactory<>("companyMeeting");

        monthColumn.setCellValueFactory(months);
        consultationColumn.setCellValueFactory(consultation);
        workoutColumn.setCellValueFactory(workout);
        personalColumn.setCellValueFactory(personal);
        customerMeetingColumn.setCellValueFactory(newCustomerMeeting);
        nutritionMeetingColumn.setCellValueFactory(nutritionMeeting);
        companyMeetingColumn.setCellValueFactory(companyMeeting);


        PropertyValueFactory<model.reports, String> createBy = new PropertyValueFactory<>("createdBy");
        PropertyValueFactory<model.reports, String> createDate = new PropertyValueFactory<>("createDate");
        PropertyValueFactory<model.reports, Integer> apptID = new PropertyValueFactory<>("apptID");
        PropertyValueFactory<model.reports, String> contact = new PropertyValueFactory<>("contact");


        usernameColumn.setCellValueFactory(createBy);
        dateColumn.setCellValueFactory(createDate);
        appIDColumn.setCellValueFactory(apptID);
        contactColumn.setCellValueFactory(contact);

        try{
            contactBox.setItems(functions.fillContact());;
            contactBox.setVisible(true);
            contactBox.getSelectionModel().select(0);
            setContactScheduleTable();
            setTypeByMonthTable();
            setLastCreatedTable();
            TypesTable.setItems(monthsList);
            appointmentTableView.setItems(appointmentList);
            LastCreatedTable.setItems(lastList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**setTypeByMonthTable.
     * This sets the Table for our Types by Month table */
    private void setTypeByMonthTable () {
        PreparedStatement ps;
        try {
            ps = JDBC.connection.prepareStatement(
                    "SELECT * FROM capstone_db.appointments");
            ResultSet rs = ps.executeQuery();
            monthsList.clear();
            while (rs.next()) {
                String startUTC = rs.getString("Start").substring(0, 19);
                LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
                ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
                String localStartDT = localZoneStart.format(datetimeDTF);

                String monthParse = localStartDT.substring(5, 7);

                int month = Integer.parseInt(monthParse);

                month = month - 1;
                String type = rs.getString("Type");

                if (month == 0) {
                    if (type.equals("Consultation")) {
                        monthTypes[0][0]++;
                    } else if (type.equals("Workout")) {
                        monthTypes[0][1]++;
                    } else if (type.equals("Personal Reason")) {
                        monthTypes[0][2]++;
                    } else if (type.equals("New Customer Meeting")) {
                        monthTypes[0][3]++;
                    }
                    else if (type.equals("Nutrition Meeting")) {
                        monthTypes[0][4]++;
                    }
                    else if (type.equals("Company Meeting")) {
                        monthTypes[0][5]++;
                    }
                } else if (month == 1) {
                    if (type.equals("Consultation")) {
                        monthTypes[1][0]++;
                    } else if (type.equals("Workout")) {
                        monthTypes[1][1]++;
                    } else if (type.equals("Personal Reason")) {
                        monthTypes[1][2]++;
                    } else if (type.equals("New Customer Meeting")) {
                        monthTypes[1][3]++;
                    }
                    else if (type.equals("Nutrition Meeting")) {
                        monthTypes[1][4]++;
                    }
                    else if (type.equals("Company Meeting")) {
                        monthTypes[1][5]++;
                    }
                } else if (month == 2) {
                    if (type.equals("Consultation")) {
                        monthTypes[2][0]++;
                    } else if (type.equals("Workout")) {
                        monthTypes[2][1]++;
                    } else if (type.equals("Personal Reason")) {
                        monthTypes[2][2]++;
                    } else if (type.equals("New Customer Meeting")) {
                        monthTypes[2][3]++;
                    }
                    else if (type.equals("Nutrition Meeting")) {
                        monthTypes[2][4]++;
                    }
                    else if (type.equals("Company Meeting")) {
                        monthTypes[2][5]++;
                    }
                } else if (month == 3) {
                    if (type.equals("Consultation")) {
                        monthTypes[3][0]++;
                    } else if (type.equals("Workout")) {
                        monthTypes[3][1]++;
                    } else if (type.equals("Personal Reason")) {
                        monthTypes[3][2]++;
                    } else if (type.equals("New Customer Meeting")) {
                        monthTypes[3][3]++;
                    }
                    else if (type.equals("Nutrition Meeting")) {
                        monthTypes[3][4]++;
                    }
                    else if (type.equals("Company Meeting")) {
                        monthTypes[3][5]++;
                    }
                } else if (month == 4) {
                    if (type.equals("Consultation")) {
                        monthTypes[4][0]++;
                    } else if (type.equals("Workout")) {
                        monthTypes[4][1]++;
                    } else if (type.equals("Personal Reason")) {
                        monthTypes[4][2]++;
                    } else if (type.equals("New Customer Meeting")) {
                        monthTypes[4][3]++;
                    }
                    else if (type.equals("Nutrition Meeting")) {
                        monthTypes[4][4]++;
                    }
                    else if (type.equals("Company Meeting")) {
                        monthTypes[4][5]++;
                    }
                } else if (month == 5) {
                    if (type.equals("Consultation")) {
                        monthTypes[5][0]++;
                    } else if (type.equals("Workout")) {
                        monthTypes[5][1]++;
                    } else if (type.equals("Personal Reason")) {
                        monthTypes[5][2]++;
                    } else if (type.equals("New Customer Meeting")) {
                        monthTypes[5][3]++;
                    }
                    else if (type.equals("Nutrition Meeting")) {
                        monthTypes[5][4]++;
                    }
                    else if (type.equals("Company Meeting")) {
                        monthTypes[5][5]++;
                    }
                } else if (month == 6) {
                    if (type.equals("Consultation")) {
                        monthTypes[6][0]++;
                    } else if (type.equals("Workout")) {
                        monthTypes[6][1]++;
                    } else if (type.equals("Personal Reason")) {
                        monthTypes[6][2]++;
                    } else if (type.equals("New Customer Meeting")) {
                        monthTypes[6][3]++;
                    }
                    else if (type.equals("Nutrition Meeting")) {
                        monthTypes[6][4]++;
                    }
                    else if (type.equals("Company Meeting")) {
                        monthTypes[6][5]++;
                    }
                } else if (month == 7) {
                    if (type.equals("Consultation")) {
                        monthTypes[7][0]++;
                    } else if (type.equals("Workout")) {
                        monthTypes[7][1]++;
                    } else if (type.equals("Personal Reason")) {
                        monthTypes[7][2]++;
                    } else if (type.equals("New Customer Meeting")) {
                        monthTypes[7][3]++;
                    }
                    else if (type.equals("Nutrition Meeting")) {
                        monthTypes[7][4]++;
                    }
                    else if (type.equals("Company Meeting")) {
                        monthTypes[7][5]++;
                    }
                } else if (month == 8) {
                    if (type.equals("Consultation")) {
                        monthTypes[8][0]++;
                    } else if (type.equals("Workout")) {
                        monthTypes[8][1]++;
                    } else if (type.equals("Personal Reason")) {
                        monthTypes[8][2]++;
                    } else if (type.equals("New Customer Meeting")) {
                        monthTypes[8][3]++;
                    }
                    else if (type.equals("Nutrition Meeting")) {
                        monthTypes[8][4]++;
                    }
                    else if (type.equals("Company Meeting")) {
                        monthTypes[8][5]++;
                    }
                } else if (month == 9) {
                    if (type.equals("Consultation")) {
                        monthTypes[9][0]++;
                    } else if (type.equals("Workout")) {
                        monthTypes[9][1]++;
                    } else if (type.equals("Personal Reason")) {
                        monthTypes[9][2]++;
                    } else if (type.equals("New Customer Meeting")) {
                        monthTypes[9][3]++;
                    }
                    else if (type.equals("Nutrition Meeting")) {
                        monthTypes[9][4]++;
                    }
                    else if (type.equals("Company Meeting")) {
                        monthTypes[9][5]++;
                    }
                } else if (month == 10) {
                    if (type.equals("Consultation")) {
                        monthTypes[10][0]++;
                    } else if (type.equals("Workout")) {
                        monthTypes[10][1]++;
                    } else if (type.equals("Personal Reason")) {
                        monthTypes[10][2]++;
                    } else if (type.equals("New Customer Meeting")) {
                        monthTypes[10][3]++;
                    }
                    else if (type.equals("Nutrition Meeting")) {
                        monthTypes[10][4]++;
                    }
                    else if (type.equals("Company Meeting")) {
                        monthTypes[10][5]++;
                    }
                } else if (month == 11) {
                    if (type.equals("Consultation")) {
                        monthTypes[11][0]++;
                    } else if (type.equals("Workout")) {
                        monthTypes[11][1]++;
                    } else if (type.equals("Personal Reason")) {
                        monthTypes[11][2]++;
                    } else if (type.equals("New Customer Meeting")) {
                        monthTypes[11][3]++;
                    }
                    else if (type.equals("Nutrition Meeting")) {
                        monthTypes[11][4]++;
                    }
                    else if (type.equals("Company Meeting")) {
                        monthTypes[11][5]++;
                    }
                }
            }
        }
        catch (SQLException sqe) {
        }
        for (int i = 0; i < 12; i++) {

            int consultation = monthTypes[i][0];
            int workout = monthTypes[i][1];
            int personal = monthTypes[i][2];
            int newCustomerMeeting = monthTypes[i][3];
            int nutritionMeeting = monthTypes[i][4];
            int companyMeeting = monthTypes[i][5];
            monthsList.add(new model.reports(getAbbreviatedMonth(i), consultation, workout, personal, newCustomerMeeting,nutritionMeeting, companyMeeting));
        }
    }

    /**setContactScheduleTable.
     * @throws SQLException
     * This sets the Table for our Contacts Schedules table. It creates the table based on the selected contact in the contact combo box. */
    private void setContactScheduleTable() throws SQLException {
        int id = functions.getContactID(contactBox.getValue().toString());
        PreparedStatement ps;
        try {
            ps = JDBC.connection.prepareStatement("SELECT * FROM capstone_db.appointments \n" +
                            "where Contact_ID = ?;");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            appointmentList.clear();

            while (rs.next()) {
                appointment a = new appointment();
                a.setAppointment_ID(rs.getInt("Appointment_ID"));
                a.setAppointmentTitle(rs.getString("Title"));
                a.setAppointmentDes(rs.getString("Description"));
                a.setAppointmentType(rs.getString("Type"));
                a.setCustomerID(rs.getInt("Customer_ID"));
                a.setUserID(rs.getInt("User_ID"));
                String startUTC = rs.getString("Start");
                String endUTC = rs.getString("End");

                LocalDateTime utcStartlocal = LocalDateTime.parse(startUTC, datetimeDTF);
                LocalDateTime utcEndlocal = LocalDateTime.parse(endUTC, datetimeDTF);

                functions.utcToLocal(utcStartlocal);
                functions.utcToLocal(utcEndlocal);

                String localStart = functions.utcToLocal(utcStartlocal);
                String localEnd = functions.utcToLocal(utcEndlocal);

                a.setStartTime(localStart);
                a.setEndTime(localEnd);
                appointmentList.addAll(a);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**getAbbreviatedMonth.
     * @param month
     * This method is used to convert our month int used in our Array to the correct month.  */
    private String getAbbreviatedMonth(int month) {

        String abbreviatedMonth ="";
        if (month == 0) {
            abbreviatedMonth = "January";
        }
        if (month == 1) {
            abbreviatedMonth = "February";
        }
        if (month == 2) {
            abbreviatedMonth = "March";
        }
        if (month == 3) {
            abbreviatedMonth = "April";
        }
        if (month == 4) {
            abbreviatedMonth = "May";
        }
        if (month == 5) {
            abbreviatedMonth = "June";
        }
        if (month == 6) {
            abbreviatedMonth = "July";
        }
        if (month == 7) {
            abbreviatedMonth = "August";
        }
        if (month == 8) {
            abbreviatedMonth = "September";
        }
        if (month == 9) {
            abbreviatedMonth = "October";
        }
        if (month == 10) {
            abbreviatedMonth = "November";
        }
        if (month == 11) {
            abbreviatedMonth = "December";
        }
        return abbreviatedMonth;

    }

    /**setLastCreatedTable.
     * This is used to set our Last created table for our customer report.  */
    private void setLastCreatedTable() {
        PreparedStatement ps;
        try {
            ps = JDBC.connection.prepareStatement(
                    "select Created_By, Create_Date, Appointment_ID, Contact_ID from capstone_db.appointments " +
                            "order by Create_Date desc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String createdBy = rs.getString("Created_By");

                String createdDate = rs.getString("Create_Date");

                LocalDateTime utcStartlocal = LocalDateTime.parse(createdDate, datetimeDTF);

                ZonedDateTime localZoneStart = utcStartlocal.atZone(utcZoneID).withZoneSameInstant(localZoneID);

                String localStart = localZoneStart.format(datetimeDTF);

                int appID = rs.getInt("Appointment_ID");
                String contact = functions.getContact(rs.getInt("Contact_ID"));

                System.out.println(localStart);

                lastList.add(new model.reports(createdBy, localStart, appID, contact));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**TypeByMonthHandler.
     * @param event
     * This is used to check to see if the contact is visible, if it is it will make it not visible.  */
        public void TypeByMonthHandler(Event event){
        if (contactBox.isVisible() == true) {
            contactBox.setVisible(false);
        }
    }

    /**TypeByMonthHandler.
     * @param event
     * @throws SQLException
     * This is used to check to see if the contact is visible, if it isn't it will make it visible. Then set the table again. */
    public void ContactScheduleHandler(Event event) throws SQLException {
        if (contactBox.isVisible() == false) {
            contactBox.setVisible(true);
        }

        setContactScheduleTable();
    }

    /**CreatedByUserHandler.
     * @param event
     * This is used to check to see if the contact is visible, if it is it will make it not visible.  */
    public void CreatedByUserHandler (Event event) {
        if (contactBox.isVisible() == true) {
            contactBox.setVisible(false);
        }
    }

    /**backHandler.
     * @param event
     * @throws IOException
     * This goes back to the mainPage*/
    public void back (ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/mainPage.fxml"));
        stage = (Stage) back.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    /**contactBoxHandler.
     * @param event
     * This will set the Contact Schedule after the user selects a contact in the combo box.  */
    public void contactBoxHandler (ActionEvent event) throws SQLException {
        setContactScheduleTable();
    }

}
