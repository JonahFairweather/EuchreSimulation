import java.util.ArrayList;

public class HandStatistics {

    public ArrayList<Card> CardsHeldAtStart;

    public int NumTricksWon = 0;

    public int NumOccurrences = 0;

    public HandStatistics(){
        CardsHeldAtStart = new ArrayList<>();
    }
    public void CacheCards(ArrayList<Card> Cards){
        for(Card c : Cards){
            Card ToCache = new Card(c.GetSuit(), c.GetRank());
            CardsHeldAtStart.add(ToCache);
        }
    }

    @Override
    public int hashCode(){
        int Hash = 0;
        for(Card c : CardsHeldAtStart){
            Hash += c.hashCode();
        }
        return Hash;
    }

    @Override
    public boolean equals(Object o){
        if(o == this) { return true;}
        if(!(o instanceof HandStatistics)){ return false; }
        HandStatistics Hand = (HandStatistics) o;

        return Hand.hashCode() == o.hashCode();
    }
}
