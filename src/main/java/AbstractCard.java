package main.java;

import java.util.Objects;

public class AbstractCard {

    private AbstractCardTypes CardType;
    private Rank Rank;

    public AbstractCard(Card c, Suit TrumpSuit){
        this.Rank = c.GetRank();
        ClassifyFromCard(c, TrumpSuit);
    }

    public static void main(String[] args){
        AbstractCard a = new AbstractCard(new Card(Suit.Clubs, main.java.Rank.QUEEN), Suit.Clubs);
        AbstractCard b = new AbstractCard(new Card(Suit.Diamonds, main.java.Rank.NINE), Suit.Clubs);
        a.CardType = AbstractCardTypes.Trump;
        b.CardType = AbstractCardTypes.OffSuitOne;
        System.out.println(a.GreaterThan(b));
        System.out.println(AbstractCardTypes.Trump.compareTo(AbstractCardTypes.OffSuitOne));
    }

    public AbstractCardTypes getCardType() {
        return CardType;
    }

    public void setCardType(AbstractCardTypes cardType) {
        CardType = cardType;
    }

    public void ClassifyFromCard(Card c, Suit trumpSuit){
        if(c.GetIsTrump()){
            if(!c.GetIsLeftBauer()){
                CardType = AbstractCardTypes.Trump;
            }else{
                CardType = AbstractCardTypes.LeftBauer;
            }
            return;
        }


        //At this point we can safely assume that the card is NOT trump
        boolean bTrumpIsBlack = trumpSuit == Suit.Clubs || trumpSuit == Suit.Spades;
        if(bTrumpIsBlack){
            if(c.GetSuit() == Suit.Clubs || c.GetSuit() == Suit.Spades){
                CardType = AbstractCardTypes.TrumpColor;
            }else{
                CardType = AbstractCardTypes.OffSuitOne;
            }
        }else{
            //Trump suit is red
            if(c.GetSuit() == Suit.Diamonds || c.GetSuit() == Suit.Hearts){
                CardType = AbstractCardTypes.TrumpColor;
            }else{
                CardType = AbstractCardTypes.OffSuitOne;
            }
        }
    }

    public String ToString(){
        StringBuilder Str = new StringBuilder();

        Str.append(Rank.toString() + "-" + CardType.toString());
        return Str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractCard)) return false;
        AbstractCard that = (AbstractCard) o;
        return CardType == that.CardType && Rank == that.Rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(CardType, Rank);
    }

    public boolean GreaterThan(AbstractCard other){
        if(CardType == AbstractCardTypes.Trump && Rank == main.java.Rank.JACK) { return true; } //If we are the right bauer
        if(other.CardType == AbstractCardTypes.Trump && other.Rank == main.java.Rank.JACK) { return false; }
        if(CardType == AbstractCardTypes.LeftBauer){
            return true;
        }
        if(other.CardType == AbstractCardTypes.LeftBauer) { return false; }

        //At this point neither card is a bauer, just go by if it is trump, then if it is trump color, then by rank
        if(other.CardType != CardType){
            if(CardType.compareTo(other.CardType) > 0){

                return true;
            }else if(CardType.compareTo(other.CardType) < 0){

                return false;
            }else{
                // The Cardtypes are the same, just go by rank at this point
                if(Rank.compareTo(other.Rank) > 0){
                    return true;
                }else if(Rank.compareTo(other.Rank) < 0){
                    return false;
                }else{
                    throw new IllegalArgumentException("Cards are identical, should not be able to happen");
                }
            }
        }else{
            if(Rank.compareTo(other.Rank) > 0){
                return true;
            }else if(Rank.compareTo(other.Rank) < 0) {
                return false;
            }else{
                System.out.println(ToString() + " " + other.ToString());
                throw new IllegalArgumentException("Cards are identical, should not be able to happen");
            }
        }
    }
}
