
import cards.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.DisplayName;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;
    private CardDeck deck1;
    private CardDeck deck2;
    //Creates a fresh player object for each test.
    @BeforeEach
    public void setUp(){
        deck1 = new CardDeck(1);
        deck2 = new CardDeck(2);
        CountDownLatch latch = new CountDownLatch(0);
        Player[] listeners = new Player[1];
        player = new Player(1, deck1, deck2, listeners, latch);
        listeners[0] = player;
        //System.out.println()
    }

    //=================================================================== AddCard =====================================================
    @Test
    @DisplayName("Ensure cards have been added to the hand.")
    public void testAddCard_HandSize(){
        try {
            player.addCard(new Card(4));
            assertEquals(1, player.getHandSize());
        } catch(Exception e) {

        }
    }
    /*
     * Check that when a card (of the preferred type) is added it is swapped to the correct position
     * (the start of the hand).
     */
    @Test
    @DisplayName("Ensure a card of the preferred type is inserted correctly.") //Should the tests be grouped based on their setup or what they are testing?? (assuming second - can use @Nested annotation).
    public void testAddCard_MovePreferredCard(){
        try {
            player.addCard(new Card(2));
            player.addCard(new Card(3));
            player.addCard(new Card(4));
            player.addCard(new Card(1));
            assertEquals("1 3 4 2", player.showHand());
        } catch(Exception e){

        }
    }
    @Test
    @DisplayName("Cannot add a card to a full hand.")
    public void testAddCard_OnFullHand(){
        try{
            player.addCard(new Card(2));
            player.addCard(new Card(3));
            player.addCard(new Card(4));
            player.addCard(new Card(1));
            player.addCard(new Card(4));
            fail("Should not be able to add a card to a full hand.");
        } catch(Exception e){ // Successfully passed.

        }
    }
    //=================================================================== RemoveCard =====================================================
    @Test
    @DisplayName("Ensure card is removed from hand.")
    public void testRemoveCard_HandSize() {
        try {
            player.addCard(new Card(3));
            player.addCard(new Card(3));
            player.addCard(new Card(4));
            player.addCard(new Card(5));
            player.removeCard();
            assertEquals(3,player.getHandSize());
        } catch (Exception e) {
            fail();
        }
    }
    // To remove a card from a player's hand the hand must have a size of 4.
    @Test
    @DisplayName("Ensure card cannot be removed when the hand is not full.")
    public void testRemoveCard_OnNotFullHand(){
        try{
            player.addCard(new Card(2));
            player.removeCard();
            fail("A card should not be able to be removed from a not full hand.");
        } catch(Exception e){

        }
    }
    @Test
    @DisplayName("Ensure card cannot be removed when the hand is a winning hand.")
    public void testRemoveCard_WithWinningHand(){

        for(int i=0;i<4;i++) {
            try {
                player.addCard(new Card(1));
            } catch(Exception e){

            }
        }
        try{
            player.removeCard();
        } catch(Exception e){
            //Gives error of bound must be positive (add a 'checkIfWon()' check in the method).
            assertEquals("Cannot remove card from winning hand.", e.getMessage());
        }

    }
    Random rnd = new Random();
    @RepeatedTest(5)
    @DisplayName("Ensure non-preferred card is not kept indefinitely.")
    public void testRemoveCard_NonPreferredCardNotKeptIndefinitely(){
        try{
            player.addCard(new Card(rnd.nextInt(8) + 2)); //add random card with value of 2 to 9.
            player.addCard(new Card(rnd.nextInt(8) + 2));
            player.addCard(new Card(1));
            player.addCard(new Card(1));
            if(player.removeCard().getValue() == 1){
                fail("Should not remove a card with a preferred value.");
            }

        } catch(Exception e){

        }
    }
    //=================================================================== CheckIfWon =====================================================
    @Test
    @DisplayName("Check a winning hand.")
    public void testCheckIfWon_True(){
        for(int i=0;i<4;i++){
            try {
                player.addCard(new Card(1));
            } catch(Exception e){

            }
        }
        assertTrue(player.checkIfWon());
    }
    @Test
    @DisplayName("Check a non-winning hand.")
    public void testCheckIfWon_False(){
        for(int i=0;i<4;i++){
            try{
                player.addCard(new Card(i));
            } catch(Exception e){

            }
        }
        assertFalse(player.checkIfWon());
    }



    //=================================================================== writeMoveToFile =====================================================
    @Test
    @DisplayName("Correct output for writeMoveToFile.")
    public void testWriteMoveToFile_CorrectOutput() {
        try {
            player.addCard(new Card(3));
            player.addCard(new Card(5));
            player.addCard(new Card(9));
            player.addCard(new Card(2));
        } catch (Exception e) {

        }
        //Delete player1_output.txt so it doesn't interfere with the test.
        File file = new File("player1_output.txt");
        file.delete();
        player.writeMoveToFile(5, 8);
        ArrayList<String> expectedLines = new ArrayList<>();
        expectedLines.add("player 1 draws a 5 from deck 1");
        expectedLines.add("player 1 discards a 8 to deck 2");
        expectedLines.add("player 1 current hand is 3 5 9 2");
        File f = new File("player1_output.txt");
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner scan = new Scanner(f);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                lines.add(line);
            }
            scan.close();
        } catch (FileNotFoundException e) {
            fail("Player output file not created.");
        }
        //Compare the actual lines and expected lines of the file.
        if (lines.size() == 3) {
            for (int i = 0; i < 3; i++) {
                assertEquals(expectedLines.get(i), lines.get(i));
            }
        } else {
            fail("There are more than 3 lines in the output text file.");
        }

        f.delete(); //delete the file that has been created or overwritten.

    }
    //gameHasBeenWon_WinningPlayer()
    @Test
    @DisplayName("Ensure correct output in output file for winning player.")
    public void testGameHasBeenWon_WinningPlayer() {
        File f = new File("player1_output.txt"); //Delete file if it already exists.
        f.delete();
        try{
            player.addCard(new Card(1));
            player.addCard(new Card(1));
            player.addCard(new Card(1));
            player.addCard(new Card(1));
        } catch (Exception e) {}
        player.gameHasBeenWon(player);
        ArrayList<String> expectedLines = new ArrayList<>();
        expectedLines.add("player 1 wins");
        expectedLines.add("player 1 exits");
        expectedLines.add("player 1 final hand: 1 1 1 1");
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner scan = new Scanner(f);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                lines.add(line);
            }
            scan.close();
        } catch (FileNotFoundException e) {
            fail("Player output file not created.");
        }
        //Compare the actual lines and expected lines of the file.
        if (lines.size() == 3) {
            for (int i = 0; i < 3; i++) {
                assertEquals(expectedLines.get(i), lines.get(i));
            }
        } else {
            fail("There are more than 3 lines in the output text file.");
        }

        f.delete(); //delete the file that has been created or overwritten.



    }
    //gameHasBeenWon_LosingPlayer()
    @Test
    @DisplayName("Ensure correct output in output file for losing player")
    public void testGameHasBeenwon_LosingPlayer() {
        try {
            player.addCard(new Card(1));
            player.addCard(new Card(2));
            player.addCard(new Card(3));
            player.addCard(new Card(4));
        }
        catch(Exception e){}
        Player player2 = new Player(2, null, null, null, null);
        player.gameHasBeenWon(player2);
        File f = new File("player1_output.txt");
        ArrayList<String> expectedLines = new ArrayList<>();
        expectedLines.add("player 2 has informed player 1 that player 2 has won");
        expectedLines.add("player 1 exits");
        expectedLines.add("player 1 final hand: 1 2 3 4");
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner scan = new Scanner(f);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                lines.add(line);
            }
            scan.close();
        } catch (FileNotFoundException e) {
            fail("Player output file not created.");
        }
        //Compare the actual lines and expected lines of the file.
        if (lines.size() == 3) {
            for (int i = 0; i < 3; i++) {
                assertEquals(expectedLines.get(i), lines.get(i));
            }
        } else {
            fail("There are more than 3 lines in the output text file.");
        }

        f.delete(); //delete the file that has been created or overwritten.
    }
    //writeIntiialHandsToFile()
    @Test
    @DisplayName("")
    public void writeInitialHandsToFile() {
        
    }
    //writeDeckContentsToFile()

    //run_WinningInitialHand()
    @Test
    @DisplayName("Ensure game stops straight away if the initial hand is a winning hand.")
    public void testRun_WinningInitialHand() {
        try{
            player.addCard(new Card(1));
            player.addCard(new Card(1));
            player.addCard(new Card(1));
            player.addCard(new Card(1));
        } catch (Exception e) {
        }
        player.run();
        File f = new File("player1_output.txt");
        ArrayList<String> expectedLines = new ArrayList<>();
        expectedLines.add("player 1 initial hand: 1 1 1 1");
        expectedLines.add("player 1 wins");
        expectedLines.add("player 1 exits");
        expectedLines.add("player 1 final hand: 1 1 1 1");
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner scan = new Scanner(f);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                lines.add(line);
            }
            scan.close();
        } catch (FileNotFoundException e) {
            fail("Player output file not created.");
        }
        //Compare the actual lines and expected lines of the file.
        if (lines.size() == 4) {
            for (int i = 0; i < 4; i++) {
                assertEquals(expectedLines.get(i), lines.get(i));
            }
        } else {
            fail("There are more than 3 lines in the output text file.");
        }

        f.delete(); //delete the file that has been created or overwritten.
    }

}
