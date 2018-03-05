/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.




SAVE TO GITHUB AND ASK Jaret to take a look

 */
package models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author gilemerson
 */
public class Volunteer {
    
    private String firstName, lastName, phoneNumber;
    private LocalDate birthday;
    private File imageFile;
    private int volunteerID;

    public Volunteer(String firstName, String lastName, String phoneNumber, LocalDate birthday) {
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setBirthday(birthday);
        setImageFile(new File("./src/images/defaultPerson.png"));
    }

    public Volunteer(String firstName, String lastName, String phoneNumber, LocalDate birthday, File imageFile) throws IOException {
        this(firstName, lastName, phoneNumber, birthday);
        setImageFile(imageFile);
        copyImageFile();
    }

    public int getVolunteerID() {
        return volunteerID;
    }

    public void setVolunteerID(int volunteerID) {
        if(volunteerID >= 0)
        this.volunteerID = volunteerID;
        else
            throw new IllegalArgumentException("VolunteerID must be >= 0");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**Area Code     City      House
     * NXX-          XXX-      XXXX
     *
     * Validation  
     * @param phoneNumber 
     */
    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber.matches("[2-9]\\d{2}[-.]?\\d{3}[-.]\\d{4}"))
        this.phoneNumber = phoneNumber;
        else 
            throw new IllegalArgumentException("Phone number must be in the pattern NXX-XXX-XXXX");
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * This will validate that the volunteer is between the ages of 10 and 100
     * @param birthday 
     */
    public void setBirthday(LocalDate birthday) {
        int age = Period.between(birthday, LocalDate.now()).getYears();
        
        if(age >= 10 && age <= 100)
        this.birthday = birthday;
        else
            throw new IllegalArgumentException("Volunteers must be 10-100 years of age");      
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }
    
    /**
     * This method will copy the file specified to the image directory on this server and give it a unique name
     */
    public void copyImageFile() throws IOException{
        //create a new path to copy the image into a local directory
        Path sourcePath = imageFile.toPath();
        
        String uniqueFileName = getUniqueFileName(imageFile.getName());
        
        Path targetPath = Paths.get("./src/images/"+uniqueFileName);
        
        //copy the file to the new directory
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
       
        //update the file image to point to the new file
        imageFile = new File(targetPath.toString());
 
    } 
    
    /**
     * This method will receive a string the represents a file name and return a string with a random, unique set of letters prefixed to it
     */
    private String getUniqueFileName(String oldFileName){
        String newName;
        
        //Create a random number generator
        SecureRandom rng = new SecureRandom();
        
        //loop until we have a unique file name
        do {
            newName = "";
            
            //generate 32 random characters
            for(int count=1; count <=32; count++){
                int nextChar;
                
                do {
                    
                    nextChar = rng.nextInt(123);
                    
                } while(!validCharacterValue(nextChar));
                
                newName = String.format("%s%c", newName, nextChar);
                
            }
            
            newName += oldFileName;
       
        } while(!uniqueFileInDirectory(newName));
        
        return newName;
    }
    
    
    /**
     * This method will search the images directory and ensure that the file name is unique
     */
    public boolean uniqueFileInDirectory(String fileName){
        File directory = new File("./src/images");
        
        File[] dir_contents = directory.listFiles();
        
        for(File file:dir_contents){
            if(file.getName().equals(fileName))
                return false;  
        }
        
        return true;
    }
    
    
    /**
             * This method will validate if the integer given corresponds to a valid ASCII character that could be used in file name
             */
            public boolean validCharacterValue(int asciiValue){
                //0-9 = ASCII range 48-57
                if(asciiValue >= 48 && asciiValue <= 57)
                    return true;
                
                //A-Z = ASCII range 65-90
                if(asciiValue >=65 && asciiValue <= 90)
                    return true;
                
                //a-z ASCII range 97-122
                if(asciiValue >=97 && asciiValue <= 122)
                    return true;
               
                
                return false;
            }
    
    
    /**
     * This method will return a formated string with the persons first name, last name and age
     *
     * @return 
     */
    public String toString(){
        return String.format("%s %s is %d years old", firstName, lastName, Period.between(birthday, LocalDate.now()).getYears());
    }
    
    /**
     * This method will write to the instance of the volunteer into the database 
     * @throws java.sql.SQLException
     */
    public void insertIntoDB() throws SQLException{
        
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        
        try
        {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://sql.computerstudi.es:3306/gc200186807", "gc200186807", "W-z68qGy");
     
            //2.create a string that holds the query with ? as users input
            String sql = "INSERT INTO volunteers (firstName, lastName, phoneNumber, birthday, imageFile)"
                    + "VALUES (?,?,?,?,?)";
            
            //3. Prepare the query 
            preparedStatement = conn.prepareStatement(sql);
            
            //4.Convert birthday into a sql date
             Date db = Date.valueOf(birthday);
            
            //5. Bind the values to the parameters
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setDate(4, db);
            preparedStatement.setString(5, imageFile.getName());
            
            preparedStatement.executeUpdate();
        }
        
         catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (preparedStatement != null)
                preparedStatement.close();
            
            if (conn != null)
                conn.close();
        }
    }
  
}
