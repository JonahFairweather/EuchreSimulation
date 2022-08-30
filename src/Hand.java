import java.util.*;
import java.util.function.Predicate;

public class Hand {

    private Suit TrumpSuit = Suit.NoSuit;
    public ArrayList<Card> HeldCards = new ArrayList<>();

    public Card GetCard(int Index, Suit LedSuit) {
        if (Index > HeldCards.size() - 1) throw new IndexOutOfBoundsException("I do not have that many cards");
        return HeldCards.get(Index);
    }

    public void AddToCards(Card CardToAdd){
        HeldCards.add(CardToAdd);
    }

    public void ClearCards(){
        HeldCards.clear();
    }

    public Card DitchCard(){
        // This method ditches the worst card in the current hand given the trump, information here is only known to you,
        // Whoever told you to pick up the card is pretty much irrelevant
        Card WorstCard = HeldCards.get(0);
        for(Card c : HeldCards){
            if(!c.GetIsTrump() && c.GetRank() != Rank.ACE){
                WorstCard = c;
            }
        }

        System.out.println("Ditching " + WorstCard.ToString());
        return WorstCard;

    }

    public void SortHand(){
        Collections.sort(HeldCards, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                if(o1.GetSuit().compareTo(o2.GetSuit()) > 0){

                    return 1;
                }else if(o1.GetSuit().compareTo(o2.GetSuit()) < 0){
                    return -1;
                }else{
                    // suits are equal
                    return o1.GetRank().compareTo(o2.GetRank());
                }
            }
        });
    }

    public String ToString(){
        StringBuilder Hand = new StringBuilder(new String());
        for(Card c : HeldCards){
            Hand.append(c.ToString());
        }
        return Hand.toString();
    }

    public void SetTrumps(Suit TrumpSuit){
        this.TrumpSuit = TrumpSuit;
        for(Card c : HeldCards){
            c.SetIsTrump(false);
            c.SetIsLeftBauer(false);
        }
        Color TrumpColor = GetTrumpColor(TrumpSuit);
        for(Card c : HeldCards){
            if(TrumpSuit == c.GetSuit()){
                c.SetIsTrump(true);
            }else if(c.GetRank() == Rank.JACK && c.GetColor() == TrumpColor){
                c.SetIsTrump(true);
                c.SetIsLeftBauer(true);
            }

        }
    }

    private Color GetTrumpColor(Suit TrumpSuit){
        if((TrumpSuit == Suit.Clubs || TrumpSuit == Suit.Spades)){
            return Color.BLACK;
        }else{
            return Color.RED;
        }
    }

    public int GetHandStrength(Suit ToCheck){
        float HandStrength = 3;

        for(Card c : HeldCards){
            if(c.GetIsLeftBauer()){
                HandStrength += 2;
                continue;
            }
            if(c.GetIsTrump() && c.GetRank() == Rank.JACK){
                HandStrength += 3;
                continue;
            }if(c.GetIsTrump() && c.GetRank() == Rank.ACE){
                HandStrength += 1.7;
                continue;
            }
            if(c.GetIsTrump()){
                HandStrength += 1.5;
                continue;
            }
            if(c.GetRank() == Rank.ACE){
                HandStrength += 1 ;
                continue;
            }
            if(c.GetRank().compareTo(Rank.ACE) < 0){
                HandStrength -= 1;
            }

        }
        //System.out.println(HandStrength);
        return (int) HandStrength;
    }

    public void SetLedSuit(Suit ToSet){
        for(Card c : HeldCards){
            if(ToSet == c.GetSuit()){
                c.SetIsLedSuit(true);
            }else{
                if(c.GetIsLeftBauer() && ToSet == TrumpSuit){
                    c.SetIsLedSuit(true);
                }else{
                    c.SetIsLedSuit(false);
                }

            }
        }
    }

    public static void main(String[] args){
        Hand hand = new Hand();
        hand.AddToCards(new Card(Suit.Clubs, Rank.ACE));
        hand.AddToCards(new Card(Suit.Clubs, Rank.QUEEN));
        hand.AddToCards(new Card(Suit.Clubs, Rank.NINE));
        hand.AddToCards(new Card(Suit.Spades, Rank.KING));
        hand.AddToCards(new Card(Suit.Spades, Rank.ACE));
        hand.SetTrumps(Suit.Clubs);
        hand.GetHandStrength(Suit.Clubs);

    }

    public int GetNumCardsOfSuit(Suit ToCheck){
        int NumCards = 0;

        for(Card c : HeldCards){
            if(c.GetSuit() == ToCheck){
                NumCards++;
            }
        }
        return NumCards;
    }

    public void RemoveFromHand(Card CardToRemove){
        HeldCards.remove(CardToRemove);
    }



    public Card GetRandomCard(Suit LedSuit) {
        Random rn = new Random();
        ArrayList<Card> LegalCards = GetLegalCards(LedSuit);
        return LegalCards.remove(rn.nextInt(LegalCards.size()));
    }

    public ArrayList<Card> GetLegalCards(Suit LedSuit){
        //Legal cards are either all the cards of the same suit as the LedSuit, or all cards because you have none of the led suit;
        boolean bHasLedSuit = false;
        if(LedSuit == Suit.NoSuit){

            return HeldCards;
        }
        ArrayList<Card> LegalCards = new ArrayList<>();
        for (Card c : HeldCards) {
            LegalCards.add(c);
            if (c.GetSuit() == LedSuit || (c.GetIsLeftBauer() && GetTrumpColor(LedSuit) == c.GetColor())) {
                if (!bHasLedSuit) {
                    bHasLedSuit = true;
                    LegalCards.clear();
                }
                LegalCards.add(c);
            }else{
                if(bHasLedSuit){
                    LegalCards.remove(c);
                }
            }
        }
        return LegalCards;
    }

    @Override
    public int hashCode(){
        HeldCards.sort(new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                if(o1.GetSuit().compareTo(o2.GetSuit()) > 0){

                    return 1;
                }else if(o1.GetSuit().compareTo(o2.GetSuit()) < 0){
                    return -1;
                }else{
                    // suits are equal
                    return o1.GetRank().compareTo(o2.GetRank());
                }
            }
        });
        return Objects.hash(HeldCards.get(0), HeldCards.get(1), HeldCards.get(2), HeldCards.get(3), HeldCards.get(4));
    }
}
