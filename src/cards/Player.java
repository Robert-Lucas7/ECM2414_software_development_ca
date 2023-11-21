package cards;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class Player implements Runnable, ActionListener{
    private final ArrayList<Card> hand; //When declared as final, the arraylist's methods (add and remove) can still be called but the variable cannot be reassigned.
    private int pointer;

    //https://stackoverflow.com/questions/10750791/what-is-the-sense-of-final-arraylist
    private final CardDeck leftDeck;
    private final CardDeck rightDeck;
    private final int preferredCard;
    private boolean gameNotFinished;
    private final ActionListener[] listeners;

    /* Method that acts as a response for when a player wins a game, so each player threads terminated elegantly.
    */
    @Override
    public void actionPerformed(ActionEvent e){ //What to do when someone has won.
        this.gameNotFinished = false;
        System.out.println(this.preferredCard + "Stopping now");
        //Write "Player i has informed player j that player i has won" to player j's output file.
        //The source object of this event is a player that has won the game, so cast the object to a player object and write the message to each player's output file.

        int playerWonNumber = ((Player)e.getSource()).getPreferredCard();
        try(FileWriter f = new FileWriter(String.format("player%d_output.txt", this.preferredCard),true)){
            if(playerWonNumber == this.preferredCard){
                f.write(String.format("player %d wins\n", this.preferredCard));
            } else{
                f.write(String.format("player %d has informed player %d that player %d has won\n", playerWonNumber, this.preferredCard, playerWonNumber));
            }
            f.write(String.format("player%d exits\n",this.preferredCard));
            f.write(String.format("player%d final hand: %s\n", this.preferredCard, this.showHand()));

        } catch(IOException ex){

        }

    }
    /** Method required for the Runnable interface and implements game-playing strategy for when the player thread is started.
     */
    //========================================== IMPORTANT ========================================================
    /*
     * The combination of a card draw and a discard should be treated as a single atomic action. Therefore,
at the end of the game every player should hold four cards. The program developed should follow
the object-oriented paradigm.
     * 
     * Also, print deck contents to their own output files at the end of the game.
     * 
     * Due to this, check if there is a card to draw from the leftdeck before discarding a card - hence all player should end the game with 4 cards.
     */
     @Override
    public void run(){
        //Write initial hand to the output file.
        try(FileWriter f = new FileWriter(String.format("player%d_output.txt", this.preferredCard))){

        } catch(IOException e){

        }
        int count = 0;
        while(this.gameNotFinished){  //Execute the game-playing strategy whilst the game has not finished...
            try{
                if(this.checkIfWon()){
                    System.out.println("Player"+this.preferredCard+" wins");
                    for(ActionListener l:listeners){ //Notify the other players that someone has won the game.
                        l.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
                    }
                }
                Card cardRemoved = this.removeCard(); //SORT OUT SO PLAYERS DON'T END WITH THREE CARDS.
                rightDeck.add(cardRemoved); //
                Card cardDrawn = leftDeck.remove();
                this.addCard(cardDrawn);
                System.out.println(this.preferredCard+": " +this.showHand());
                // System.out.println(rightDeck);
                // System.out.println(leftDeck);
                this.writeMoveToFile(cardDrawn.getValue(), cardRemoved.getValue(), count == 0);
                count++;
                
            }
            catch(IndexOutOfBoundsException e){
                e.printStackTrace();;
                return;
            } 
            catch(Exception e){
                //System.out.println(e);
            }
        }
    }
    
    /** Creates a new player with the following parameters:
     * @param preferredCard The card value that the player will collect and not get rid of.
     * @param leftDeck The CardDeck where the player draws their cards from.
     * @param rightDeck The CardDeck where the player puts the cards they removed from their hand.
     * @param listeners An array containing the players in the game.
     */
    public Player(int preferredCard, CardDeck leftDeck, CardDeck rightDeck, ActionListener[] listeners ){
        this.hand = new ArrayList<>();
        this.preferredCard = preferredCard;
        this.rightDeck = rightDeck;
        this.leftDeck = leftDeck;
        this.pointer = 0;
        this.gameNotFinished = true;
        this.listeners = listeners;
    }

    /** Removes a card from the player's hand. The card removed is chosen at random from the cards in the hand that are not of the preferred card value.
     * @return The card that has been removed.
     * @throws Exception Thrown when the hand is not full.
     */
    public Card removeCard() throws Exception{
        Random r = new Random();
        
        
        if (this.hand.size() != 4){
            throw new Exception("Hand is not full");
        }else {
            // System.out.println(this.hand.size() - this.pointer);
            if(!this.checkIfWon()) {
                int index = r.nextInt(this.hand.size() - this.pointer) + this.pointer;

                Card card1 = this.hand.remove(index);
                return card1;
            } else{
                throw new Exception("Cannot remove card from winning hand.");
            }
        }
    }


    /** Adds the specified card to the player's hand.
     * @param card1 The card to be added to the player's hand.
     * @throws Exception Thrown when there is no room to add a card.
     */
    public void addCard (Card card1) throws Exception{
        if (this.hand.size() >= 4 ){
            throw new Exception("Hand is full");
        }else {
            this.hand.add(card1);
            if (card1.getValue() == preferredCard) {
                Collections.swap(this.hand, this.hand.size() - 1, this.pointer++);
            }
        }
    }

    /** Gets a string representation of the player's hand.
     * @return A string representation of the player's hand.
     */
    public String showHand(){
        String s = ""; //A string builder may be more efficient but the difference will be negligable for small inputs such as n=4.
        for (int i=0;i<this.getHandSize();i++){
            if(i != this.getHandSize() - 1){
                s += this.hand.get(i).getValue() + " ";
            } else{
                s += this.hand.get(i).getValue();
            }

        }
        return s;
    }

    /** Checks if the player has won the game by checking two conditions: if the hand contains four cards and each card in the hand has the same value.
     * @return A boolean value for if the player has a winning hand.
     */
    public boolean checkIfWon(){
        if(this.hand.size() == 4){
            for (Card c : this.hand ){
                if(c.getValue() != this.preferredCard){
                    return false;
                }
            }
            return true;
        } else{
            return false;
        }
        
    }

    /** Writes the player's move for that round to their respective file.
     * @param cardDrawn The card drawn from the left deck.
     * @param cardRemoved The card that has been removed from the player's hand.
     * @param isFirstGo A boolean value for if it is the player's first go in the game.
     */
    public void writeMoveToFile(int cardDrawn, int cardRemoved, boolean isFirstGo){
        String filename = "player" + this.preferredCard + "_output.txt";
        try {
            FileWriter f = new FileWriter(filename, !isFirstGo);
            f.write("Player " + this.preferredCard + " draws a " + cardDrawn + " from deck " + this.leftDeck.getDeckNumber() + "\n");
            f.write("Player " + this.preferredCard + " discards a " + cardRemoved + " to deck " + this.rightDeck.getDeckNumber() + "\n");
            f.write("Player " + this.preferredCard + " current hand is " + this.showHand() + "\n");
            f.close();

        }catch(IOException e){
            System.out.println(e);
        }
    }
    /** Gets the number of cards in the player's hand.
     * @return The number of cards in the player's hand.
     */
    public int getHandSize(){
        return this.hand.size();
    }
    public int getPreferredCard(){
        return this.preferredCard;
    }

}
