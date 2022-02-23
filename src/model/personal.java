package model;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class personal extends appointment {
    private static final ObservableList<String> contactIdList = FXCollections.observableArrayList();
    private static final ObservableList<String> customerList = FXCollections.observableArrayList();

    /**fillBusinessType.
     * @return
     * This will create a list of all appointment types for a personal appointment. */
    public static ObservableList<String> fillPersonalType() {
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.clear();
        typeList.addAll("Personal Reason");
        return typeList;
    }
    /**fillContact.
     * @return
     * This will create a list of all Contact Names*/
    public static ObservableList<String> fillPersonalContact() throws SQLException {
        contactIdList.clear();
        String contactName = "";
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "Select * from capstone_db.contacts where contact_ID = 1";
        ResultSet rs = statement.executeQuery(sqlStatement);
        while (rs.next()) {
            contactName = rs.getString("Contact_Name");
            contactIdList.add(contactName);
        }
        return contactIdList;
    }
    /**fillPersonalCustomerName.
     * @throws SQLException
     * @return
     * This will create a list of all customer ids. */
    public static ObservableList<String> fillPersonalCustomerName() throws SQLException {
        customerList.clear();
        String nextCustomer = "";
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "Select Customer_Name from capstone_db.customers";

        ResultSet rs = statement.executeQuery(sqlStatement);
        while (rs.next()) {
            nextCustomer = rs.getString("Customer_Name");
            customerList.add(nextCustomer);
        }
        return customerList;
    }
}
