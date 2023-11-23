package cards;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

//=======================================
// LOOK INTO HOW TO STRUCTURE THE PACKAGE TO TURN INTO JAR FILE.
//=======================================
public class CardGame {
    /** The entry point for the executable
     * @param args
     */
    public static void main(String[] args){
            Scanner scan = new Scanner(System.in);

            boolean validInput = false;
            Pack pack = null;
            String fileLocation = "CA_FINAL/pack.txt";
            int numPlayers = 4;
            try{
                pack = new Pack(0);
                pack.loadPack(fileLocation);
            } catch(Exception e){}
            /*
            while(!validInput) {

                try {
                    System.out.println("Please enter the number of players:");

                    numPlayers = scan.nextInt();
                    if (numPlayers < 2) {
                        throw new Exception("Invalid number of players (must be an integer greater than two).");
                    }
                    scan.nextLine();
                    System.out.println("Please enter location of pack to load:");
                    fileLocation = scan.nextLine();
                    pack = new Pack(numPlayers);
                    pack.loadPack(fileLocation);
                    validInput = true;

                } catch (InvalidPackException e) {
                    e.printStackTrace();
                    System.out.printf("%s is not a valid pack file.%n", fileLocation);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Pack file specified does not exist.");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Invalid number of players (must be an integer greater than two).");
                    scan.nextLine();
                }
            }
            scan.close();*/


            // Initialise the player and deck arrays with the length specified by the user input for the number of players.
            Player[] players = new Player[numPlayers];
            CardDeck[] decks = new CardDeck[numPlayers];

            //Initialise each deck object necessary for the game.
            for(int i=0;i<numPlayers;i++){
                decks[i] = new CardDeck(i+1);
            }
            // Initialise each player object necessary for the game - with each player having a left and right deck passed into the constructor.
            CountDownLatch latch = new CountDownLatch(1);
            for(int i=0;i<numPlayers;i++){
                if(i == 0){
                    players[i] = new Player(i+1, decks[0], decks[3], players, latch);
                } else{
                    players[i] = new Player(i+1, decks[i], decks[i-1], players, latch);
                }
            }

            try{
                // Deal the cards to the players in a round-robin fashion
                for(int i=0;i<numPlayers;i++){ //Give each player 4 cards in a round-robin fashion.
                    for(Player p:players){
                        p.addCard(pack.remove());
                    }
                }
                // Deal the remaining cards to the decks in a round-robin fashion until the pack is empty.
                while(pack.getSize() > 0){
                    for(int i=0;i<numPlayers;i++){
                        decks[i].add(pack.remove());
                    }
                }
                // Initialise and start each player thread.
                Thread[] threads = new Thread[numPlayers];
                for(int i=0;i<numPlayers;i++){
                    threads[i] = new Thread(players[i]);
                    threads[i].start();
                }
                
                latch.countDown();
                
             } catch(Exception e){

             }
             //Write deck to their own output files at the end of the game

    }
}
