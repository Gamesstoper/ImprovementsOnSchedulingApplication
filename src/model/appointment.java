package model;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class appointment {
    private int appointment_ID;
    private String appointmentTitle;
    private String appointmentDes;
    private String appointmentLoc;
    private String appointmentType;
    private String startTime;
    private String endTime;
    private Date createDate;
    private String createBy;
    private String lastUpdate;
    private String lastUpdateBy;
    private int customerID;
    private int userID;
    private String contactName;
    private String customerName;
    private String userName;

    /**appointment.
     * This sets all of our values to default settings. */
    public appointment() {
        appointment_ID=0;
        appointmentTitle="";
        appointmentDes="";
        appointmentLoc="";
        appointmentType="";
        startTime="";
        endTime="";
        createBy="";
        lastUpdate="";
        lastUpdateBy="";
        customerID=0;
        userID=0;
        customerName ="";
        userName="";
    }
    /**appointment.
     * @param appointmentID
     * @param contact
     * @param customerID
     * @param description
     * @param endTime
     * @param location
     * @param startTime
     * @param title
     * @param type
     * @param userID
     * This creates our main constructor for formatting and setting up our appointment objects */
        public appointment(int appointmentID, int customerID, int userID, String title, String description, String location, String contact, String type, String startTime,
                       String endTime) throws SQLException {

        setAppointment_ID(appointmentID);
        setCustomerID(customerID);
        setUserID(userID);
        setAppointmentTitle(title);
        setAppointmentDes(description);
        setAppointmentLoc(location);
        setContactName(contact);
        setAppointmentType(type);
        setStartTime(startTime);
        setEndTime(endTime);
    }
    /**appointment.
     * @param appointmentID
     * @param customerID
     * @param end
     * @param start
     * @param title
     * This creates our another constructor for formatting and setting up our appointment objects */
    public appointment(int appointmentID, String start, String end, String title, int customerID){
        setAppointment_ID(appointmentID);
        setStartTime(start);
        setEndTime(end);
        setAppointmentTitle(title);
        setCustomerID(customerID);
    }

    /**setFields.
     * @param appointmentID
     * @param customerName
     * @param a
     * @param appointmentContact
     * @param appointmentDate
     * @param userName
     * @param startTime
     * @param endTime
     * @param appointmentDescription
     * @param appointmentLocation
     * @param appointmentTitle
     * @param typebox
     * This is used to set our fields from our main appointment table to our update appointment screen. This sets all our text fields and combo boxes.*/
    public static void setFields(appointment a, TextArea appointmentDescription, TextField appointmentID, TextField appointmentTitle, TextField appointmentLocation, ComboBox appointmentContact, DatePicker appointmentDate, ComboBox customerName, ComboBox userName,
                                 ComboBox typebox, ComboBox startTime, ComboBox endTime) {

        appointmentDescription.setText(a.getAppointmentDes());
        appointmentID.setText(String.valueOf(Integer.valueOf(a.getAppointment_ID())));
        appointmentTitle.setText(a.getAppointmentTitle());
        appointmentLocation.setText(a.getAppointmentLoc());
        appointmentContact.setValue(a.getContactName());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");

        String startLocal = a.getStartTime();
        String endLocal = a.getEndTime();


        LocalDateTime localDateTimeStart = LocalDateTime.parse(startLocal, dtf);
        LocalDateTime localDateTimeEnd = LocalDateTime.parse(endLocal, dtf);

        LocalDate localDate = localDateTimeStart.toLocalDate();

        appointmentDate.setValue(localDate);

        customerName.setValue(a.getCustomerName());
        userName.setValue(a.getUserName());
        typebox.getSelectionModel().select(a.getAppointmentType());
        startTime.getSelectionModel().select(localDateTimeStart.format(time));
        endTime.getSelectionModel().select(localDateTimeEnd.format(time));
    }

    /**getAppointment_ID.
     * @return
     * Standard getter*/
    public int getAppointment_ID() {
        return appointment_ID;
    }

    /**setAppointment_ID.
     * Standard setter*/
    public void setAppointment_ID(int appointment_ID) {
        this.appointment_ID = appointment_ID;
    }

    /**getAppointmentTitle.
     * @return
     * Standard getter*/
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /**setAppointmentTitle.
     * Standard setter*/
    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    /**getAppointmentDes.
     * @return
     * Standard getter*/
    public String getAppointmentDes() {
        return appointmentDes;
    }

    /**setAppointmentDes.
     * Standard setter*/
    public void setAppointmentDes(String appointmentDes) {
        this.appointmentDes = appointmentDes;
    }

    /**getAppointmentLoc.
     * @return
     * Standard getter*/
    public String getAppointmentLoc() {
        return appointmentLoc;
    }

    /**setAppointmentLoc.
     * Standard setter*/
    public void setAppointmentLoc(String appointmentLoc) {
        this.appointmentLoc = appointmentLoc;
    }

    /**getAppointmentType.
     * @return
     * Standard getter*/
    public String getAppointmentType() {
        return appointmentType;
    }

    /**setAppointmentType.
     * Standard setter*/
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    /**getStartTime.
     * @return
     * Standard getter*/
    public String getStartTime() {
        return startTime;
    }

    /**setStartTime.
     * Standard setter*/
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**getEndTime.
     * @return
     * Standard getter*/
    public String getEndTime() {
        return endTime;
    }

    /**setEndTime.
     * Standard setter*/
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**getCreateBy.
     * @return
     * Standard getter*/
    public String getCreateBy() {
        return createBy;
    }

    /**setCreateBy.
     * Standard setter*/
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**getLastUpdate.
     * @return
     * Standard getter*/
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**setLastUpdate.
     * Standard setter*/
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**getLastUpdateBy.
     * @return
     * Standard getter*/
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /**setLastUpdateBy.
     * @param lastUpdateBy
     * Standard setter*/
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    /**getCustomerID.
     * @return
     * Standard getter*/
    public int getCustomerID() {
        return customerID;
    }

    /**setCustomerID.
     * Standard setter*/
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**getUserID.
     * @return
     * Standard getter*/
    public int getUserID() {
        return userID;
    }

    /**setUserID.
     * Standard setter*/
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**getCreateDate.
     * @return
     * Standard getter*/
    public Date getCreateDate() {
        return createDate;
    }

    /**setCreateDate.
     * Standard setter*/
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**getContactName.
     * @return
     * Standard getter*/
    public String getContactName() {
        return contactName;
    }

    /**setContactName.
     * Standard setter*/
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**getCustomerName.
     * @return
     * Standard getter*/
    public String getCustomerName() {
        return customerName;
    }

    /**setCustomerName.
     * Standard setter*/
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**getUserName.
     * @return
     * Standard getter*/
    public String getUserName() {
        return userName;
    }

    /**setUserName.
     * Standard setter*/
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
