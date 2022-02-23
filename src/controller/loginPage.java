package controller;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.*;
import helper.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.ZoneId;
import java.io.*;

public class loginPage implements Initializable {
    @FXML
    private Label passwordLabel;
    @FXML
    private Button loginButton;
    @FXML
    private TextField UsernameTextField;
    @FXML
    private PasswordField PasswordTextField;
    @FXML
    private Button exitButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label currentLocationLabel;
    private DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private ObservableList<appointment> appointmentReminder = FXCollections.observableArrayList();
    //Uncomment to check Europe/ Paris Timezone change. MUST ALSO COMMENT THE OTHER VAR
    //private ZoneId localZoneID = ZoneId.of("Europe/Paris");
    private ZoneId localZoneID = ZoneId.systemDefault();

    @Override
    /**initialize.
     * This sets up our Locale to the system default and sets all of our fields to use the ResourceBundle */
    public void initialize(URL url, ResourceBundle rb) {

        System.out.println("Before rb");
        try {
            rb = ResourceBundle.getBundle("language_files/Nat", Locale.getDefault());
            titleLabel.setText(rb.getString("Title"));
            usernameLabel.setText(rb.getString("Username"));
            UsernameTextField.setPromptText(rb.getString("Username"));
            passwordLabel.setText(rb.getString("Password"));
            PasswordTextField.setPromptText(rb.getString("Password"));
            loginButton.setText(rb.getString("Login"));
            exitButton.setText(rb.getString("ExitTitle"));
            locationLabel.setText(rb.getString("Location") + ":");
            currentLocationLabel.setText(String.valueOf(localZoneID));
        } catch (MissingResourceException e) {
            System.out.println("Missing resource");
        }
    }

    /**appointmentReminderAlert.
     * This sets up appointment reminder alert and will alert the user if they have an appointment within 15 minutes of
     * sign in. It will display a message that they do not have an appointment if they don't have one within 15 minutes
     * of sign in. This uses a Lambda expression to set a filtered list of all the times that meet our conditions. */
    public void appointmentReminderAlert(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime noPlus15 = now.plusMinutes(15);
        System.out.println("Now: " + now);
        System.out.println("Now + 15: " + noPlus15);
        FilteredList<appointment> fd = new FilteredList<>(appointmentReminder);
        /**Lambda*/fd.setPredicate(row -> {
            System.out.println("Row Start Time:" + row.getStartTime());
            LocalDateTime rowDate = LocalDateTime.parse(row.getStartTime(),datetimeDTF);
            System.out.println("Row Date: " + rowDate);
            return rowDate.isAfter(now.minusMinutes(1)) && rowDate.isBefore(noPlus15);}
        );
        if (fd.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Appointment Reminder");
            alert.setHeaderText("You have no Upcoming appointments.");
            alert.showAndWait();
        }
        else {
            String title = fd.get(0).getAppointmentTitle();
            int appID = fd.get(0).getAppointment_ID();
            String start = fd.get(0).getStartTime();
            String end = fd.get(0).getEndTime();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Appointment Reminder");
            alert.setHeaderText("You have an upcoming appointment.");
            alert.setContentText("Appointment ID: "+ appID +"\n" +
                    "Appointment Title: " + title + "\n" +
                    "Appointment Start Time: " + start + "\n"+
                    "Appointment End Time: " + end );
            alert.showAndWait();
                }
    }

    /**reminderList.
     * This creates the reminder list based on the user that signs in.  */
    private void reminderList(){
        try {
            int userID = user.getUserID();
            PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Appointment_ID, Start, End, Title, Customer_ID from capstone_db.appointments \n" +
                    "where User_ID =?; ");
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String Start = rs.getString("start");
                String End = rs.getString("end");

                LocalDateTime utcStartlocal = LocalDateTime.parse(Start, datetimeDTF);
                LocalDateTime utcEndlocal = LocalDateTime.parse(End, datetimeDTF);

                String startTime = functions.utcToLocal(utcStartlocal);
                String endTime = functions.utcToLocal(utcEndlocal);

                System.out.println("Appointment List Start: " + startTime);
                System.out.println("Appointment List End: " + endTime);

                int appointmentID= rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                int cusID = rs.getInt("Customer_ID");

                appointmentReminder.add(new appointment(appointmentID, startTime, endTime, title,cusID));
                System.out.println(appointmentReminder);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**exitHandler.
     * @param actionEvent
     * This exits the program*/
    public void exitAction(ActionEvent actionEvent) {
        //Uncomment the below to test the French
        //Locale.setDefault(new Locale("fr"));
        ResourceBundle rb = ResourceBundle.getBundle("language_files/Nat", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("ExitTitle"));
        alert.setContentText(rb.getString("Exit"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**loginAction.
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     * This gets the username and password and sends it to another function to make sure that it matches the database.
     * If it does then it will bring you to the main menu. This will also record the login attempts in login_activity.txt. */
    public void loginAction(ActionEvent actionEvent) throws SQLException, IOException {
        //Uncomment the below to test the French
        //Locale.setDefault(new Locale("fr"));
        ResourceBundle rb = ResourceBundle.getBundle("language_files/Nat", Locale.getDefault());
        String username = UsernameTextField.getText();
        String password = PasswordTextField.getText();
        Stage stage;
        int userID = getUserID(username);
        int specialCount = 1;
        int numCount = 1;
        int capCount = 1;

        for (int i = 0; i <password.length(); i++){
            if(!Character.isDigit(password.charAt(i)) && !Character.isLetter(password.charAt(i)) && !Character.isWhitespace(password.charAt(i))){
                specialCount--;
                System.out.println(specialCount);
                System.out.println(password);
                System.out.println(password.charAt(i));
            }
        }

        if (specialCount > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("ErrorTitle"));
            alert.setContentText(rb.getString("Special"));
            alert.showAndWait();
            return;
        }
        for (int i = 0; i <password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                capCount--;

            }
        }

        if (capCount > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("ErrorTitle"));
            alert.setContentText(rb.getString("Caps"));
            alert.showAndWait();
            return;
        }
        for (int i = 0; i <password.length(); i++){
            if(Character.isDigit(password.charAt(i))){
                numCount--;
            }
        }

        if (numCount > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("ErrorTitle"));
            alert.setContentText(rb.getString("Number"));
            alert.showAndWait();
            return;
        }

        if(username.isBlank() || password.isBlank()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("ErrorTitle"));
            alert.setContentText(rb.getString("PasswordBlank"));
            alert.showAndWait();
            return;
        }

        else {
            if (isValidPassword(userID, password)) {
                user.setUserID(userID);
                user.setUsername(username);
                loginLogSuccess(username);
                reminderList();
                appointmentReminderAlert();

                Parent root = FXMLLoader.load(getClass().getResource("/view/mainPage.fxml"));
                stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Main Menu");
                stage.setScene(scene);
                stage.show();
                System.out.println("Success");

            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("ErrorTitle"));
                alert.setContentText(rb.getString("Invalid"));
                alert.showAndWait();
                return;
            }
        }
    }

    /**isValidPassword.
     * @param userID
     * @param password
     * @throws SQLException
     * @return
     * This compares the user's entry to what is in the database. It will return true or false depending on the result of the search. */
    private boolean isValidPassword(int userID, String password) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Password FROM `capstone_db`.`users` where User_ID = "+ userID);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            if (result.getString("Password").equals(password)) {
                return true;
            }
        }
        loginLogFail();
        return false;
    }

    /**getUserID.
     * @param username
     * @throws SQLException
     * @return
     * This pulls the user ID from the username. */
    private int getUserID(String username) throws SQLException {
        int userID = -1;
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT User_ID FROM `capstone_db`.`users` where User_Name = "+ "'" + username +"'");
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            userID = result.getInt("User_ID");
        }
        return userID;
    }

    /**loginLogSuccess.
     * @param user
     * This writes to login_activity.txt that there was a successful login */
    public void loginLogSuccess(String user) {
        try {
            String fileName = "login_activity.txt";
            String text = "User: " + user + " logged in at: " + dateTime.getTimeStamp() + " UTC\n";
            FileWriter fileWriter = new FileWriter(fileName, true);
            PrintWriter writer = new PrintWriter(fileWriter);
            writer.println(text);
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**loginLogFail.
     * This writes to login_activity.txt that there was a failed login */
        public void loginLogFail (){
            try {
                String fileName = "login_activity.txt";
                String text = "User: " + UsernameTextField.getText() + " typed in incorrect username or password at: " + dateTime.getTimeStamp() + " UTC\n";
                FileWriter fileWriter = new FileWriter(fileName, true);
                PrintWriter writer = new PrintWriter(fileWriter);
                writer.println(text);
                writer.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

    /**UserNameTextFieldHandler.
     * @param actionEvent
     * This is here for any future development or improvements but currently not being used */
    public void UserNameTextFieldHandler (ActionEvent actionEvent){
            }

    /**PasswordTextFieldHandler.
     * @param actionEvent
     * This is here for any future development or improvements but currently not being used */
    public void PasswordTextFieldHandler (ActionEvent actionEvent){
            }

    }
