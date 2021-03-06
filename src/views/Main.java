
package views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author gilemerson
 */
public class Main extends Application{
    
    public static void main(String[] args){
        
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("VolunteerTableView.fxml"));
        
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("New Volunteer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
