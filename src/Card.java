import java.util.Objects;

public class Card {
    private Rank Rank;

    public void SetIsTrump(boolean bIsTrump) {
        this.Properties.bIsTrump = bIsTrump;
    }

    public boolean GetIsTrump() {
        return Properties.bIsTrump;
    }
    public void SetIsLeftBauer(boolean bIsLeftBauer){
        Properties.bIsLeftBauer = bIsLeftBauer;
    }

    public boolean GetIsLeftBauer(){
        return Properties.bIsLeftBauer;
    }

    public Card(Suit MySuit, Rank MyRank){
        Suit = MySuit;
        Rank = MyRank;
        if(Suit == Suit.Spades || Suit == Suit.Clubs){
            Color = Color.BLACK;
        }else{
            Color = Color.RED;
        }
        Properties = new CardProperties();
    }

    private CardProperties Properties;

    public boolean GetIsLedSuit() { return Properties.bIsLedSuit; }

    public void SetIsLedSuit(boolean bIsLedSuit){ this.Properties.bIsLedSuit = bIsLedSuit; }

    public void SetColor(Color color) {
        Color = color;
    }

    public void SetSuit(Suit suit) {
        Suit = suit;
    }

    private Color Color;
    private Suit Suit;

    public Suit GetSuit(){ return Suit; }

    public Color GetColor(){ return Color; }

    public Rank GetRank(){ return Rank; }

    public void SetRank(Rank Rank){ this.Rank = Rank; }

    @Override
    public boolean equals(Object o) throws IllegalArgumentException{
        if (this == o) return true;

        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        if(card.GetSuit() == GetSuit() && card.GetRank() == GetRank()) throw new IllegalArgumentException("Cards are identical, " +
                "there is an issue with the shuffling/deck");
        boolean bCardsAreTheSame = Properties.bIsLeftBauer == card.Properties.bIsLeftBauer;
        bCardsAreTheSame = Properties.bIsUpturnedSuit == card.Properties.bIsUpturnedSuit;
        bCardsAreTheSame = GetRank() == card.GetRank();
        if(bCardsAreTheSame){
            //System.out.println(card.ToString() + " and the: " + ToString() + " are considered equal at the moment.");
            return true;
        }else{
            return false;
        }
    }

    public String ToString(){
        return "The " + Rank.toString() + " of " + Suit.toString() + " ";
    }
    /*
    * Returns true if this card is greater than the other, false if it is lower. In the event that the cards are equal
    * an error will be thrown.
    *
    * */
    boolean CompareTo(Card other) throws IllegalArgumentException{
        //System.out.println(ToString() + " is being compared with: " + other.ToString());
        if(other.GetRank() == GetRank() && this.GetSuit() == other.GetSuit()) throw new IllegalArgumentException("Cards are identical, not allowed");
        if(!other.GetIsTrump() && (this.GetRank().compareTo(other.GetRank()) > 0)) return true;
        if(!other.GetIsTrump() && !other.GetIsLedSuit()) return true;
        // If the other card is not trump and this card is a higher rank there is no way we can lose
        if(this.GetIsTrump() && !other.GetIsTrump()) {
            return true;
        }else if (other.GetIsTrump() && !GetIsTrump()) {
            return false;
        }
        // At this point they are either both trump or neither are trump.
        //There is no way for a card which is NOT the led suit to be getting comapared to a card which is not trump and lose
        if(!GetIsTrump()){
            if(GetRank().compareTo(other.GetRank()) > 0) {
                return true;
            }if(GetRank().compareTo(other.GetRank()) < 0){
                return false;
            }
            else{
                //This means that the cards have the same rank
                throw new IllegalArgumentException("Cards are identical, " +
                        "there is an issue with the shuffling/deck");
            }
        }else if (GetRank() == Rank.JACK && other.Properties.bIsLeftBauer){
            return true;
        }
        if(GetIsTrump()){

        }

        return false;

    }

    @Override
    public int hashCode() {
        return Objects.hash(Rank, Color, Suit);
    }
}