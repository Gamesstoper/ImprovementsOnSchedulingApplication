package model;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class functions {
    private static final ObservableList<String> countryOptions = FXCollections.observableArrayList();
    private static final ObservableList<String> contactIdList = FXCollections.observableArrayList();
    private static final ObservableList<String> userList = FXCollections.observableArrayList();
    private static final ObservableList<String> customerList = FXCollections.observableArrayList();
    private static final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId localZoneID = ZoneId.systemDefault();
    private static final ZoneId utcZoneID = ZoneId.of("UTC");


    /**utcToLocal.
     * @param utcTime
     * @return
     * This will convert UTC time to Local time*/
    public static String utcToLocal(LocalDateTime utcTime) {
        ZonedDateTime localTime = utcTime.atZone(utcZoneID).withZoneSameInstant(localZoneID);
        String localTimeString = localTime.format(datetimeDTF);
        return localTimeString;

    }

    /**utcToLocal.
     * @param localTime
     * @return
     * This will convert Local time to UTC time*/
    public static String localToUTC(LocalDateTime localTime) {
        ZonedDateTime utcTime = localTime.atZone(localZoneID).withZoneSameInstant(utcZoneID);
        String utcTimeString = utcTime.format(datetimeDTF);
        return utcTimeString;
    }

    /**fillContact.
     * @return
     * This will create a list of all Contact Names*/
    public static ObservableList<String> fillContact() throws SQLException {
        contactIdList.clear();
        String contactName = "";
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "Select Contact_Name from capstone_db.contacts";
        ResultSet rs = statement.executeQuery(sqlStatement);
        while (rs.next()) {
            contactName = rs.getString("Contact_Name");
            contactIdList.add(contactName);
        }
        return contactIdList;
    }

    /**fillUserName.
     * @throws SQLException
     * @return
     * This will create a list of all user names. */
    public static ObservableList<String> fillUserName() throws SQLException {
        userList.clear();
        String nextUser = "";
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "Select User_Name from capstone_db.users";

        ResultSet rs = statement.executeQuery(sqlStatement);
        while (rs.next()) {
            nextUser = rs.getString("User_Name");
            userList.add(nextUser);
        }
        return userList;
    }

    /**getContactID.
     * @param contactName
     * @throws SQLException
     * @return
     * This will get the contact ID from a contact Name. */
    public static int getContactID(String contactName) throws SQLException {
        int contact = 0;
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "Select Contact_ID from capstone_db.contacts WHERE Contact_Name= '"
                +contactName +"'";
        ResultSet rs = statement.executeQuery(sqlStatement);
        while (rs.next()) {
            contact = rs.getInt("Contact_ID");
        }
        return contact;
    }
    /**getContact.
     * @param contactID
     * @throws SQLException
     * @return
     * This will get the contact Name from a contact ID. */
    public static String getContact(int contactID) throws SQLException {
        String contactName="";
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "Select Contact_Name FROM capstone_db.contacts WHERE Contact_ID =" + contactID;
        ResultSet result = statement.executeQuery(sqlStatement);
        while (result.next()){
            contactName = result.getString("Contact_Name");
        }

        return contactName;
    }

    /**getCustomer.
     * @param customerID
     * @throws SQLException
     * @return
     * This will get the Customer Name from a Customer ID. */
    public static String getCustomer(int customerID) throws SQLException {
        String customerName="";
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "Select Customer_Name FROM capstone_db.customers WHERE Customer_ID =" + customerID;
        ResultSet result = statement.executeQuery(sqlStatement);
        while (result.next()){
            customerName = result.getString("Customer_Name");
        }

        return customerName;
    }
    /**getCustomerID.
     * @param customerName
     * @throws SQLException
     * @return
     * This will get the contact ID from a contact Name. */
    public static int getCustomerID(String customerName) throws SQLException {
        int customer = 0;
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "Select Customer_ID from capstone_db.customers WHERE Customer_Name= '"
                +customerName +"'";
        ResultSet rs = statement.executeQuery(sqlStatement);
        while (rs.next()) {
            customer = rs.getInt("Customer_ID");
        }
        return customer;
    }

    /**getUser.
     * @param userID
     * @throws SQLException
     * @return
     * This will get the User Name from a User ID. */
    public static String getUser(int userID) throws SQLException {
        String contactName="";
        PreparedStatement statement = JDBC.connection.prepareStatement("Select User_Name FROM capstone_db.users WHERE User_ID =" + userID);
        ResultSet result = statement.executeQuery();
        while (result.next()){
            contactName = result.getString("User_Name");
        }

        return contactName;
    }
    /**getUserId.
     * @param userName
     * @throws SQLException
     * @return
     * This will get the user ID from a user name. */
    public static int getUserId(String userName) throws SQLException {
        int user = 0;
        PreparedStatement statement = JDBC.connection.prepareStatement("Select User_ID from capstone_db.users WHERE User_Name= '"
                +userName +"'");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            user = rs.getInt("User_ID");
        }
        return user;
    }

    /**FillCountryCombo.
     * @throws SQLException
     * @return
     * This will create a list of all customer ids. */
    public static ObservableList<String> FillCountryCombo() throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "Select Country from capstone_db.countries";
        ResultSet rs = statement.executeQuery(sqlStatement);
        while (rs.next()) {
            customer cus = new customer();
            cus.setCountry(rs.getString("Country"));
            countryOptions.add(cus.getCountry());
        }
        return countryOptions;
    }
}