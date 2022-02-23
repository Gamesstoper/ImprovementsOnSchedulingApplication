package main;
import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;
import java.util.ResourceBundle;

    public class main extends Application {
        @Override
        /** start. This is the starts the application.*/
        public void start(Stage primarystage) throws Exception {
            //Uncomment to Check the French Translation
            //Locale.setDefault(new Locale("fr"));
            ResourceBundle rb = ResourceBundle.getBundle("language_files/Nat", Locale.getDefault());
            Parent root = FXMLLoader.load(getClass().getResource("/view/loginPage.fxml"));
            Scene scene = new Scene(root);
            primarystage.setTitle(rb.getString("TitlePage"));
            primarystage.setScene(scene);
            primarystage.show();
        }

        /**main. This is used to set the test data into the program. */
    public static void main(String[] args){

        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}