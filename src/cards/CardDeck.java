package cards;
import java.util.ArrayList;


/*
 * Look at https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html
 * Can remove synchronized methods.
 */
public class CardDeck {
    /** The cards contained within the deck of cards, acting as a queue.
     */
    protected ArrayList<Card> cards;
    /** The number associated with the deck. 
     */
    private final int deckNumber;
    /** Creates a deck of cards with the specified deck number.
     * @param deckNumber The number associated with the deck.
     */
    public CardDeck(int deckNumber){
        cards = new ArrayList<>();
        this.deckNumber = deckNumber;
    }
    /** A thread-safe method to add a card to the card deck.
     * @param cardToAdd The card to add to the deck.
     */
    public synchronized void add(Card cardToAdd){
        cards.add(cardToAdd);
    }
    /** A thread-safe method to remove a card from deck of cards.
     * @return The card removed from the top of the deck.
     * @throws Exception When the deck is empty.
     */
    public synchronized Card remove() throws Exception{
        if(cards.size() ==0){
            throw new Exception("Deck is empty.");
        }
        return cards.remove(0);
    }
    /** Gets the number of cards in the deck.
     * @return The number of cards in the deck.
     */
    public int getSize(){
        return cards.size();
    }
    /** Gets a string representation of the CardDeck instance.
     * @return a string representation of the card deck.
     */
    @Override
    public String toString(){
        String s = String.format("deck%d contents: ");
        for(Card c : this.cards){
            s += c.getValue()+" "; //change so there isn't a space after last value.
        }
        return s;
    }
    /** Get the number associated with the deck of cards.
     * @return the number associated with the deck of cards.
     */
    public int getDeckNumber(){
        return this.deckNumber;
    }

}
