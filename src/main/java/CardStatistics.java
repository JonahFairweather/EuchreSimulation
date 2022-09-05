package main.java;

import com.sun.source.tree.NewArrayTree;

import java.util.ArrayList;
import java.util.Objects;

public class CardStatistics {
    private Card Owner;

    public static ArrayList<CardStatistics> CardStatisticsHold = new ArrayList<>();

    private CardStatistics(Card owner) {
        Owner = owner;
    }

    public static CardStatistics GetStatisticsForCard(Card c){
        CardStatistics Stats = new CardStatistics(c);
        if (CardStatisticsHold.contains(Stats)) {
            return CardStatisticsHold.get(CardStatisticsHold.indexOf(Stats));
        }else{
            CardStatisticsHold.add(Stats);
            return Stats;
        }
    }

    private int NumOccurrences = 0;

    public static ArrayList<CardStatistics> getCardStatisticsHold() {
        return CardStatisticsHold;
    }

    public int getNumOccurrences() {
        return NumOccurrences;
    }

    public void IncrementNumOccurrences(){
        NumOccurrences++;
    }

    private int NumTricksWon = 0;

    public void IncrementNumTricksWon() {
        NumTricksWon++;
    }

    public int getNumTricksWon() {
        return NumTricksWon;
    }

    public float GetAverageTricksWon(){
        return ((float) NumTricksWon / (float) NumOccurrences);
    }

    public String ToString(){
        StringBuilder Str = new StringBuilder();
        Str.append(Owner.GetRank().toString()).append(" subclass ");
        if(Owner.GetIsTrump()){
            if(Owner.GetIsLeftBauer()){
                if(Owner.GetIsLedSuit()){
                    Str.append(" Led Suit ");
                }else{
                    Str.append(" Free Play ");
                }
                Str.append("LeftBauer.");
            }else{
                if(Owner.GetIsLedSuit()){
                    Str.append(" Led Suit ");
                }else{
                    Str.append(" Free Play ");
                }
                Str.append("Trump.");
            }
        }else{
            if(Owner.GetIsLedSuit()){
                Str.append(" Led Suit ");
            }else{
                Str.append(" Free Play ");
            }
            Str.append("OffSuit.");
        }

        Str.append(" Won " + NumTricksWon + " over " + NumOccurrences + " occurences. Giving an average of " + GetAverageTricksWon() + " tricks won per occurrence");

        return Str.toString();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardStatistics)) return false;
        CardStatistics that = (CardStatistics) o;
        return (Owner.GetIsTrump() == that.Owner.GetIsTrump()) && (Owner.GetIsLeftBauer() == that.Owner.GetIsLeftBauer())
                && (Owner.GetRank() == that.Owner.GetRank()) && (Owner.GetIsLedSuit() == that.Owner.GetIsLedSuit());
    }

    @Override
    public int hashCode() {
        return Objects.hash(Owner);
    }
}
