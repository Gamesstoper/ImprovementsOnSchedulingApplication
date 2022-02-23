package model;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

public class customer {
    private int customerID;
    private String customerName;
    private String address;
    private String zip;
    private String phone;
    private String division;
    private String country;
    private static ObservableList<customer> allCustomers = FXCollections.observableArrayList();

    /**customer.
     * This sets all of our values to default settings. */
    public customer(){
        customerID=0;
        customerName="";
        address="";
        zip="";
        phone="";
        division="";
        country="";
    }

    /**customer.
     * @param customerID
     * @param address
     * @param country
     * @param customerName
     * @param division
     * @param phone
     * @param zip
     * This creates our main constructor for formatting and setting up our customer objects */
    public customer(int customerID,String customerName,String address, String zip, String phone,String division,String country){
        this.customerID= customerID;
        this.customerName = customerName;
        this.address= address;
        this.zip = zip;
        this.phone = phone;
        this.division=division;
        this.country=country;

    }
    /**setFields.
     * @param customerName
     * @param customerID
     * @param c
     * @param countryBox
     * @param customerAddress
     * @param customerPhone
     * @param customerZip
     * @param divisionBox
     * This is used to set our fields from our main customer table to our update customer screen. This sets all our text fields and combo boxes.*/
    public static void setFields(customer c, TextField customerID, TextField customerName, TextField customerAddress, TextField customerPhone, TextField customerZip, ComboBox countryBox,ComboBox divisionBox) {
        customerID.setText(String.valueOf(Integer.valueOf(c.getCustomerID())));
        customerName.setText((c.getCustomerName()));
        customerAddress.setText(String.valueOf(c.getAddress()));
        customerPhone.setText(String.valueOf(c.getPhone()));
        customerZip.setText((String.valueOf(c.getZip())));
        countryBox.setValue(c.getCountry());
        divisionBox.setValue(c.getDivision());
    }

    /**getAllCustomers.
     * @throws SQLException\
     * @return
     * This gets a list of all customers and information into a customers list */
    public static ObservableList<customer> getAllCustomers() throws SQLException {
        allCustomers.clear();
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT * FROM capstone_db.customers";
        ResultSet result = statement.executeQuery(sqlStatement);
        while (result.next()) {
            customer cus = new customer();
            cus.setCustomerID(result.getInt("Customer_ID"));
            cus.setCustomerName(result.getString("Customer_Name"));
            cus.setAddress(result.getString("Address"));
            cus.setZip(result.getString("Postal_Code"));
            cus.setPhone(result.getString("Phone"));
            String div = result.getString("Division_ID");
            cus.setDivision(getDivisionName(div));
            int code = getCountryCode(div);
            cus.setCountry(getCountryName(code));
            allCustomers.addAll(cus);
        }
        return allCustomers;
    }

    /**getDivisionName.
     * @param div
     * @throws SQLException
     * @return
     * This gets the Division Names from a division id. */
    public static String getDivisionName(String div) throws SQLException {
        String divName = "";
        Integer.parseInt(div);
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT * FROM capstone_db.first_level_divisions where Division_ID =" + div;
        ResultSet result = statement.executeQuery(sqlStatement);
        while (result.next()) {
            divName = result.getString("Division");
            System.out.println(divName);
        }

        return divName;
    }

    /**getCountryCode.
     * @param div
     * @throws SQLException
     * @return
     * This gets the Country Code from a division id. */
    public static int getCountryCode(String div) throws SQLException {
        Integer.parseInt(div);
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT Country_ID FROM capstone_db.first_level_divisions where Division_ID =" +
                div;
        ResultSet result = statement.executeQuery(sqlStatement);
        int couCode = 0;
        while (result.next()) {
            couCode = result.getInt("Country_ID");

        }
        return couCode;
    }

    /**getCountryName.
     * @param code
     * @throws SQLException
     * @return
     * This gets the Country Name from a Country code */
    public static String getCountryName(int code) throws SQLException {

        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT * FROM capstone_db.countries where Country_ID =" + code;
        ResultSet result = statement.executeQuery(sqlStatement);
        String couName = "";
        while (result.next()) {
            couName = result.getString("Country");
        }
        return couName;
    }

    /**getDivisionID.
     * @param division
     * @throws SQLException
     * @return
     * This gets the division id from a division name */
    public static int getDivisionID(String division) throws SQLException {
        int divisionid = 0;
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT Division_ID FROM capstone_db.first_level_divisions where Division =" + "'" + division +"'";
        ResultSet result = statement.executeQuery(sqlStatement);
        while (result.next()) {
            divisionid = result.getInt("Division_ID");
        }
        return divisionid;

    }
    /**lookupCustomer.
     * @param customer
     * @throws SQLException
     * @return
     * This searches for strings through the texfield for the search and does full and partial matches. If not found it searches for id next.*/
    public static ObservableList<customer> lookupCustomer (String customer) throws SQLException {
        ObservableList<customer> customers = FXCollections.observableArrayList();
        customer = customer.trim().toLowerCase();
        for (model.customer c : getAllCustomers()){
            if ( c.getCustomerName().toLowerCase().contains(customer)){
                customers.add(c);
            }
            if (customers.isEmpty()){
                try {
                    int id = Integer.parseInt(customer);
                    System.out.println(id);
                    model.customer cu = lookupCustomer(id);
                    if(cu != null)
                        customers.add(cu);
                    } catch (NumberFormatException e){}
                }
            }
        return customers;
        }
    /**lookupCustomer.
     * @param customerID
     * @throws SQLException
     * @return
     * This searches for strings through the texfield for the search and find matches to the customer ID.*/
    public static customer lookupCustomer(int customerID) throws SQLException {
        for (customer c : allCustomers) {
            if (c.getCustomerID() == customerID) {
                System.out.println(c);
                return c;
            }
        }
        return null;
    }

    /**getCustomerID.
     * @return
     * Standard getter*/
    public  int getCustomerID(){return customerID;}

    /**getCustomerName.
     * @return
     * Standard getter*/
    public String getCustomerName(){return customerName;}

    /**getAddress.
     * @return
     * Standard getter*/
    public String getAddress(){return address;}

    /**getZip.
     * @return
     * Standard getter*/
    public String getZip(){return zip;}

    /**getPhone.
     * @return
     * Standard getter*/
    public String getPhone(){return phone;}

    /**getDivision.
     * @return
     * Standard getter*/
    public String getDivision(){return division;}

    /**getCountry.
     * @return
     * Standard getter*/
    public String getCountry(){return country;}

    /**setCustomerID.
     * Standard setter*/
    public void setCustomerID(int customerID){this.customerID =customerID;}

    /**setCustomerName.
     * Standard setter*/
    public void setCustomerName(String customerName){this.customerName =customerName;}

    /**setAddress.
     * Standard setter*/
    public void setAddress(String address){this.address =address;}

    /**setZip.
     * Standard setter*/
    public void setZip(String zip){this.zip =zip;}

    /**setPhone.
     * Standard setter*/
    public void setPhone(String phone){this.phone =phone;}

    /**setDivision.
     * Standard setter*/
    public void setDivision(String division){this.division =division;}

    /**setCountry.
     * Standard setter*/
    public void setCountry(String country){this.country =country;}
}
