package main.java;

import org.apache.commons.math3.analysis.function.Abs;

import java.util.*;

public class AbstractHandStatistics {

    private ArrayList<AbstractCard> CardsHeld;

    private int NumOccurrences = 0;

    private EuchreGame e;

    private Suit TrumpSuit;

    private Suit OffSuitOne = Suit.NoSuit;

    private int NumOfOffsuitOne = 0;

    private int NumOfOffsuitTwo = 0;

    public void IncreaseNumOccurrence(){
        NumOccurrences++;
    }

    public int GetNumOccurrences(){
        return NumOccurrences;
    }

    private int NumTricksWon = 0;

    public void IncreaseNumTricksWon(int ToIncrease){
        NumTricksWon += ToIncrease;
    }

    public int GetNumTricksWon(){
        return NumTricksWon;
    }

    private AbstractHandStatistics(){
        CardsHeld = new ArrayList<>();
    }

    public float AverageNumTricksWon(){
        return (((float)NumTricksWon)/((float)NumOccurrences));
    }

    private static ArrayList<AbstractHandStatistics> HandStatsBank = new ArrayList<>();

    public static ArrayList<AbstractHandStatistics> getHandStatsBank() {
        return HandStatsBank;
    }

    public static void PrintAllHandStats(Comparator<AbstractHandStatistics> SortBy){
        Collections.sort(HandStatsBank, SortBy);
        for(AbstractHandStatistics h : HandStatsBank){
            System.out.println(h.ToString() + " won " + h.NumTricksWon + " tricks over " + h.NumOccurrences +
                    " occurrences for an average of " + h.AverageNumTricksWon() + " tricks won per occurrence.");
        }
    }

    public static AbstractHandStatistics GetHandStats(ArrayList<Card> Cards, Suit trump){

        AbstractHandStatistics Stats = new AbstractHandStatistics();
        Stats.TrumpSuit = trump;
        for(Card c : Cards){
            Stats.AddToHand(c);
        }
        if(HandStatsBank.contains(Stats)){
            return HandStatsBank.get(HandStatsBank.indexOf(Stats));
        }else{
            HandStatsBank.add(Stats);
            return Stats;
        }

    }

    public String ToString(){
        StringBuilder Str = new StringBuilder();
        Str.append("Hand contains: ");
        for(AbstractCard c : CardsHeld){
            Str.append(c.ToString()).append(" ");
        }

        return Str.toString();
    }

    public String GetCardStrings(){
        StringBuilder Str = new StringBuilder();

        for(AbstractCard c : CardsHeld){
            Str.append(c.ToString()).append(" ");
        }

        return Str.toString();
    }

    private void AddToHand(Card card){

        AbstractCard ACard = new AbstractCard(card, TrumpSuit);

        if(CardsHeld.size() == 0){
            if(ACard.getCardType() == AbstractCardTypes.OffSuitOne){
                OffSuitOne = card.GetSuit();
                NumOfOffsuitOne++;
            }
            CardsHeld.add(ACard);
            return;
        }
        // Now we need to classify the card as either OffSuitTwo or OffSuitOne
        if (ACard.getCardType() == AbstractCardTypes.OffSuitOne) {

            if(NumOfOffsuitOne == 0){

                OffSuitOne = card.GetSuit();

                NumOfOffsuitOne++;
            }else{
                if(card.GetSuit() == OffSuitOne){

                    NumOfOffsuitOne++;
                }else{

                    ACard.setCardType(AbstractCardTypes.OffSuitTwo);

                    NumOfOffsuitTwo++;

                    if(NumOfOffsuitTwo > NumOfOffsuitOne){
                        ACard.setCardType(AbstractCardTypes.OffSuitOne);
                        OffSuitOne = card.GetSuit();
                        NumOfOffsuitOne = NumOfOffsuitTwo;
                        NumOfOffsuitTwo--;
                        for(AbstractCard c : CardsHeld){
                            if(c.getCardType() == AbstractCardTypes.OffSuitOne){
                                c.setCardType(AbstractCardTypes.OffSuitTwo);
                            }else if (c.getCardType() == AbstractCardTypes.OffSuitTwo){
                                c.setCardType(AbstractCardTypes.OffSuitOne);
                            }
                        }
                        Collections.sort(CardsHeld, new Comparator<AbstractCard>() {
                            @Override
                            public int compare(AbstractCard o1, AbstractCard o2) {
                                if(o2.GreaterThan(o1)){
                                    return 1;
                                }else{
                                    return -1;
                                }
                            }
                        });
                    }
                }
            }

        }
        //System.out.println(ACard.ToString());
//        for(AbstractCard c : CardsHeld){
//            System.out.print(c.ToString() + " ");
//        }
        //System.out.println();
        //System.out.println();
        int i = CardsHeld.size();
        while(ACard.GreaterThan(CardsHeld.get(i-1))){
            //System.out.println(ACard.ToString() + " is greater than " + CardsHeld.get(i-1).ToString());
            i--;
            if(i == 0){
                break;
            }
        }
        CardsHeld.add(i, ACard);
        //Now this card will be filled in based on whether it is trump, trump color, or OffSuit;
    }

    public static void main(String[] args){
        EuchreGame e = new EuchreGame();
        e.TrumpSuit = Suit.Diamonds;
        Player PlayerTwo = new Player("Two");
        PlayerTwo.AddToHand(new Card(Suit.Diamonds, Rank.JACK));
        PlayerTwo.AddToHand(new Card(Suit.Hearts, Rank.JACK));
        PlayerTwo.AddToHand(new Card(Suit.Diamonds, Rank.ACE));
        PlayerTwo.AddToHand(new Card(Suit.Clubs, Rank.KING));
        PlayerTwo.AddToHand(new Card(Suit.Spades, Rank.ACE));

        PlayerTwo.SetTrumps(e.TrumpSuit);
        Player PlayerOne = new Player("One");
        PlayerOne.AddToHand(new Card(Suit.Diamonds, Rank.JACK));
        PlayerOne.AddToHand(new Card(Suit.Hearts, Rank.JACK));
        PlayerOne.AddToHand(new Card(Suit.Diamonds, Rank.ACE));
        PlayerOne.AddToHand(new Card(Suit.Clubs, Rank.ACE));
        PlayerOne.AddToHand(new Card(Suit.Spades, Rank.KING));
        e.TrumpSuit = Suit.Diamonds;
        PlayerOne.SetTrumps(e.TrumpSuit);
        AbstractHandStatistics AHS2 = AbstractHandStatistics.GetHandStats(PlayerOne.GetHand().HeldCards, Suit.Diamonds);
        AbstractHandStatistics AHS = AbstractHandStatistics.GetHandStats(PlayerTwo.GetHand().HeldCards, Suit.Diamonds);


        System.out.println(AHS.ToString());
        System.out.println(AHS2.ToString());
        System.out.println(AHS == AHS2);

    }

    @Override
    public int hashCode(){
        return CardsHeld.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractHandStatistics)) return false;
        AbstractHandStatistics that = (AbstractHandStatistics) o;
        boolean toreturn = true;
        for(AbstractCard c : CardsHeld){
            if(!(that.CardsHeld.contains(c))){
                if(c.getCardType() == AbstractCardTypes.Trump || c.getCardType() == AbstractCardTypes.LeftBauer || c.getCardType() == AbstractCardTypes.TrumpColor){
                    return false;
                }
                //System.out.println("Other does not contain: " + c.ToString());
                for(AbstractCard c1 : CardsHeld) {

                    // Swap the OffSuitOne and OffSuitTwo, if they are now equal we can return true
                    if (c1.getCardType() == AbstractCardTypes.OffSuitOne) {

                        c1.setCardType(AbstractCardTypes.OffSuitTwo);
                    } else if (c1.getCardType() == AbstractCardTypes.OffSuitTwo) {

                        c1.setCardType(AbstractCardTypes.OffSuitOne);
                    }
                }

                for(AbstractCard c2 : CardsHeld){
                    if(!(that.CardsHeld.contains(c2))){

                        toreturn = false;
                    }
                }
                for(AbstractCard c3: CardsHeld){
                    if(c3.getCardType() == AbstractCardTypes.OffSuitOne){
                        c3.setCardType(AbstractCardTypes.OffSuitTwo);
                    }else if(c3.getCardType() == AbstractCardTypes.OffSuitTwo){
                        c3.setCardType(AbstractCardTypes.OffSuitOne);
                    }
                }
            }
        }

        return toreturn;
    }
}
