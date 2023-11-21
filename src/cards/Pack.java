package cards;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Pack extends CardDeck{
    
    int n;
    public Pack(int n){            

        super(0);
        
        this.n = n;
    }
    public void loadPack(String fileLocation) throws FileNotFoundException, InvalidPackException{
        // check if file exists
        //validate all inputs are non-negative integers
        //validate number of rows is 8n.
        
        // ================================= CHANGE LINE BELOW WHEN PUT IN JAR ===============================
        File f = new File(fileLocation);//;fileLocation); //throws a FileNotFoundError if file doesn't exist.
        // =====================================================================================================
        try(Scanner scan= new Scanner(f)){
            try{
                while(scan.hasNext()){
                    int num = scan.nextInt();
                    
                    
                    if(scan.hasNextLine()){ //SEE IF THIS NEEDS TO BE CHANGED TO A NICER WAY
                        scan.nextLine();
                    }
                    if(num < 1){
                        throw new InvalidPackException(); //Create new more specific exception class - InvalidPackException??
                    }
                    this.add(new Card(num));
                }
                if(this.getSize() != 8 * this.n){
                    throw new InvalidPackException();
                }
            } catch(Exception e){
                throw new InvalidPackException();
            }

        }
    }
}
