package model;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class business extends appointment{
    private static final ObservableList<String> contactIdList = FXCollections.observableArrayList();
    private static final ObservableList<String> customerList = FXCollections.observableArrayList();

    /**fillBusinessType.
     * @return
     * This will create a list of all appointment types for a business appointment. */
    public static ObservableList<String> fillBusinessType() {
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.clear();
        typeList.addAll("Consultation", "Workout", "New Customer Meeting", "Nutrition Meeting", "Company Meeting");
        return typeList;
    }
    /**fillContact.
     * @return
     * This will create a list of all Contact Names*/
    public static ObservableList<String> fillBusinessContact() throws SQLException {
        contactIdList.clear();
        String contactName = "";
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "Select * from capstone_db.contacts";
        ResultSet rs = statement.executeQuery(sqlStatement);
        while (rs.next()) {
            contactName = rs.getString("Contact_Name");
            contactIdList.add(contactName);
        }
        return contactIdList;
    }
    /**fillBusinessCustomerName.
     * @throws SQLException
     * @return
     * This will create a list of all business customer names. */
        public static ObservableList<String> fillBusinessCustomerName() throws SQLException {
        customerList.clear();
        String nextCustomer = "";
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "Select Customer_Name from capstone_db.customers WHERE Customer_ID>1";

        ResultSet rs = statement.executeQuery(sqlStatement);
        while (rs.next()) {
            nextCustomer = rs.getString("Customer_Name");
            customerList.add(nextCustomer);
        }
        return customerList;
    }
}
