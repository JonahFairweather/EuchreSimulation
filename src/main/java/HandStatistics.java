package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class HandStatistics {

    public ArrayList<Card> CardsHeldAtStart;

    public int NumTricksWon = 0;

    public void IncreaseNumTricksWon(int ToAdd){
        NumTricksWon += ToAdd;
        if(!AverageAcrossSuits.containsKey(TrumpSuit)){
            Duo d = new Duo();
            d.TricksWon += ToAdd;

            AverageAcrossSuits.put(TrumpSuit, d);
        }else{
            AverageAcrossSuits.get(TrumpSuit).TricksWon += ToAdd;
        }
    }

    private Suit TrumpSuit;

    public int NumTricksWonLastOccurrence = 0;

    private int NumOccurrences = 0;

    public int GetNumOccurrences(){
        return NumOccurrences;
    }
    public void IncreaseNumOccurrences(){
        NumOccurrences++;
        if (!AverageAcrossSuits.containsKey(TrumpSuit)) {
            Duo d = new Duo();
            d.Occurrences++;
            AverageAcrossSuits.put(TrumpSuit, d);
        }else{
            AverageAcrossSuits.get(TrumpSuit).Occurrences++;
        }
    }

    ArrayList<Suit> TrumpWhenPlayed;

    HashMap<Suit, Duo> AverageAcrossSuits;



    public String GetStatsBySuit(Suit ToGet){
        StringBuilder Str = new StringBuilder();
        if(AverageAcrossSuits.containsKey(ToGet)){
            Duo Stats = AverageAcrossSuits.get(ToGet);
            Str.append("Average Tricks Won By ");
            Str.append(ToGet.toString()).append(": ");
            Str.append(Stats.GetAverageTricksWon());
            Str.append(", over ");
            Str.append(Stats.Occurrences);
            Str.append(" occurence(s). ");
            Str.append(Stats.TricksWon);
            Str.append(" total tricks won.");

        }else{
            Str.append("Not Found.");
        }


        return Str.toString();
    }

    public HandStatistics(){
        CardsHeldAtStart = new ArrayList<>();
        TrumpWhenPlayed = new ArrayList<>();
        AverageAcrossSuits = new HashMap<>();
    }


    public void CacheCards(Player Player){
        for(Card c : Player.GetHand().HeldCards){
            Card ToCache = new Card(c.GetSuit(), c.GetRank());
            CardsHeldAtStart.add(ToCache);
            TrumpWhenPlayed.add(Player.GetHand().TrumpSuit);
            this.TrumpSuit = Player.GetHand().TrumpSuit;
        }
    }

    public void SetCurrentTrumpSuit(Suit toSet){
        this.TrumpSuit = toSet;
    }

    public String ToString(){
        StringBuilder Str = new StringBuilder();
        Str.append("Hand: ");
        for(Card c : CardsHeldAtStart){
            Str.append(c.ToString() + " ");
        }

        Str.append(". Won an average of " + AverageTricksWon() + " over " + NumOccurrences + " occurrences.\n");
        Str.append("It occurred with the following suits as trump: " );
        for(Suit s : AverageAcrossSuits.keySet()){
            Str.append("[ " + s.toString().toUpperCase(Locale.ROOT) + ", ");
            Str.append(AverageAcrossSuits.get(s).GetAverageTricksWon());
            Str.append("]");
        }


        return Str.toString();
    }



    @Override
    public int hashCode(){
        int Hash = 0;
        return CardsHeldAtStart.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if(o == this) { return true;}
        if(!(o instanceof HandStatistics)){ return false; }
        HandStatistics Hand = (HandStatistics) o;

        return Hand.hashCode() == o.hashCode();
    }

    public float AverageTricksWon(){
        return NumTricksWon/NumOccurrences;
    }
}
