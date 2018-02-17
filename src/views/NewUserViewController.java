
package views;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import models.Volunteer;

/**
 * FXML Controller class
 *
 * @author gilemerson
 */
public class NewUserViewController implements Initializable {
    
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField phoneTextField;
    @FXML private DatePicker birthday;
    @FXML private Label errMsgLabel;
    @FXML private ImageView imageView;
    
    private File imageFile;
    private boolean imageFileChanged;
    
    
    /**
     * When this button is pushed, a FileChooser object is launched to allow the user to browse for a new image file. When that is complete it will update the view with a new image.
     */
    public void chooseImageButtonPushed(ActionEvent event) {
        
        //get the stage to open a new window (or stage in JavaFX)
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        //instantiate the FileChooser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        
        //filter for jpg, png files
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);
        
        //set to users picture directory or user directory if not avalible
        String userDirectoryString = System.getProperty("user.home")+"//pictures";
        File userDirectory = new File(userDirectoryString);
        
        //if you cannot navigate to the pictures directory, go to the users home
        if(!userDirectory.canRead())
            userDirectory = new File(System.getProperty("user.home"));
        
        fileChooser.setInitialDirectory(userDirectory);
        
        //open the file dialog window
        File tmpImageFile = fileChooser.showOpenDialog(stage);
        if(tmpImageFile != null)
        {
                
                imageFile = tmpImageFile;

            //update the image view with the new image
            if(imageFile.isFile())
            {
                try
                {
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    Image img = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageView.setImage(img); 
                    imageFileChanged = true;
                }
                catch(IOException e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }
        


        }

        /**
         * This method will read from the scene and try to create a new instance of a volunteer. if a volunteer was successfully created, it is uploaded to the database
         */
        public void saveVolunteerButtonPushed(ActionEvent event){

            try 
            {
                Volunteer volunteer;
                        
                if(imageFileChanged)
                {
                   volunteer = new Volunteer(firstNameTextField.getText(), lastNameTextField.getText(), phoneTextField.getText(), birthday.getValue(), imageFile);
                }
                else
                {
                   volunteer = new Volunteer(firstNameTextField.getText(), lastNameTextField.getText(), phoneTextField.getText(), birthday.getValue());
                }
              
                 errMsgLabel.setText("");//do not show errors if creating volunteer was successful

                 volunteer.insertIntoDB();
            }
            catch(Exception e)
            {
                errMsgLabel.setText(e.getMessage());
            }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        birthday.setValue(LocalDate.now().minusYears(19));
        
        imageFileChanged = false; //initially the image has not changed, use the default
        
        errMsgLabel.setText(""); //set the error message to be empty to start
        
        //load the default image for the avatar
        try 
        {
            imageFile = new File("./src/images/defaultPerson.png");
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);
        }
        catch(IOException e)
        {
            System.err.println(e.getMessage());
        }
    }    
    
}
