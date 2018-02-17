/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package volunteerapp;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import models.Volunteer;

/**
 *
 * @author gilemerson
 */
public class VolunteerApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException {
        //Create a new volunteer  *(This is an instance of our class)*instanciate the model class
        Volunteer volunteer = new Volunteer("Wilma","Flinestone","651-555-1234", LocalDate.of(2002, Month.MARCH, 12),
            new File("./src/images/Fred_Flintstone.png"));
        
        System.out.printf("Our Volunteer: %s%n", volunteer);
        
        volunteer.insertIntoDB();
        
    }
}
