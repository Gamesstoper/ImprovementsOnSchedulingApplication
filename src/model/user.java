package model;

public class user {
    private static int userID;
    private static String username;
    private static String password;

    /**user.
     * This sets all of our values to default settings. */
    public user() {
        userID = 0;
        username = null;
        password = null;
    }

    /**user.
     * @param userID
     * @param username
     * @param password 
     * This creates our main constructor for formatting and setting up our user objects */
    public user(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    /**getUserID.
     * @return
     * Standard getter*/
    public static int getUserID() {
        return userID;
    }

    /**getUsername.
     * @return
     * Standard getter*/
    public static String  getUsername() {
        return username;
    }

    /**getPassword.
     * @return
     * Standard getter*/
    public String getPassword() {
        return this.password;
    }

    /**setUserID.
     * @param userID
     * Standard setter*/
    public static void setUserID(int userID) {
        user.userID = userID;
    }

    /**setUsername.
     * @param username
     * Standard setter*/
    public static void setUsername(String username) {
        user.username = username;
    }

    /**setPassword.
     * @param password
     * Standard setter*/
    public void setPassword(String password) {
        user.password = password;
    }
}

