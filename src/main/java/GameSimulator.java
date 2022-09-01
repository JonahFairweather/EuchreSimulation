package main.java;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

public class GameSimulator {
    public GameSimulator(){

    }

    public static void main(String[] args){
        ArrayList<Player> Players = new ArrayList<>();
        Player PlayerOne = new Player("One");
        PlayerOne.AddToHand(new Card(Suit.Clubs, Rank.JACK));
        PlayerOne.AddToHand(new Card(Suit.Hearts, Rank.JACK));
        PlayerOne.AddToHand(new Card(Suit.Diamonds, Rank.QUEEN));
        PlayerOne.AddToHand(new Card(Suit.Diamonds, Rank.JACK ));
        PlayerOne.AddToHand(new Card(Suit.Spades, Rank.JACK));
        Players.add(PlayerOne);

        Player PlayerTwo = new Player("Two");
        PlayerTwo.AddToHand(new Card(Suit.Hearts, Rank.NINE));
        PlayerTwo.AddToHand(new Card(Suit.Hearts, Rank.ACE));
        PlayerTwo.AddToHand(new Card(Suit.Hearts, Rank.KING));
        PlayerTwo.AddToHand(new Card(Suit.Hearts, Rank.QUEEN));
        PlayerTwo.AddToHand(new Card(Suit.Diamonds, Rank.TEN));
        Players.add(PlayerTwo);

        Player PlayerThree = new Player("Three");
        PlayerThree.AddToHand(new Card(Suit.Clubs, Rank.TEN));
        PlayerThree.AddToHand(new Card(Suit.Clubs, Rank.ACE));
        PlayerThree.AddToHand(new Card(Suit.Clubs, Rank.NINE));
        PlayerThree.AddToHand(new Card(Suit.Clubs, Rank.KING));
        PlayerThree.AddToHand(new Card(Suit.Clubs, Rank.QUEEN));
        Players.add(PlayerThree);

        Player PlayerFour = new Player("Four");
        PlayerFour.AddToHand(new Card(Suit.Spades, Rank.TEN));
        PlayerFour.AddToHand(new Card(Suit.Spades, Rank.ACE));
        PlayerFour.AddToHand(new Card(Suit.Spades, Rank.KING));
        PlayerFour.AddToHand(new Card(Suit.Spades, Rank.QUEEN));
        PlayerFour.AddToHand(new Card(Suit.Spades, Rank.NINE));
        Players.add(PlayerFour);

        EuchreGame game = new EuchreGame();
        game.VerbosePlayerInfo = true;

        game.SimulateHand(Players, Suit.Clubs);
    }

    public void Run(int TotalPoints){
        //TODO:: Run a game which plays until TotalPoints points have been scored by either team.
        int PointsAtStart = TotalPoints;
        EuchreGame Game = new EuchreGame();
        Game.SetCanadianLoner(true);
        Game.SetAloneHandsAllowed(true);
        long StartTime = System.nanoTime();

        while(TotalPoints > 0){
            Game.SimulateHand();
            Game.AnnounceScore();
            TotalPoints -= Game.PointsScored;
        }



        Game.PrintHandStats(new Comparator<HandStatistics>() {
            @Override
            public int compare(HandStatistics o1, HandStatistics o2) {
                if(o1.AverageTricksWon() > o2.AverageTricksWon()) {
                    return 1;
                }else if(o1.AverageTricksWon() < o2.AverageTricksWon()){
                    return -1;
                }else{
                    if(o1.NumTricksWon > o2.NumTricksWon){
                        return 1;
                    }else if(o1.NumTricksWon < o2.NumTricksWon){
                        return -1;
                    }else{
                        return 0;
                    }
                }

            }
        });

        Game.PrintAllBauerStats();

        Game.WriteToFile("C:\\Users\\jonah\\IdeaProjects\\EuchreSimulation\\WrittenFiles", new Comparator<HandStatistics>() {
            @Override
            public int compare(HandStatistics o1, HandStatistics o2) {
                if (o1.GetNumOccurrences() > o2.GetNumOccurrences()) {
                    return 1;
                } else if (o1.GetNumOccurrences() < o2.GetNumOccurrences()) {
                    return -1;
                } else {
                    if (o1.AverageTricksWon() > o2.AverageTricksWon()) {
                        return 1;
                    } else if (o1.AverageTricksWon() < o2.AverageTricksWon()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        }, PointsAtStart, new Predicate<HandStatistics>() {
            @Override
            public boolean test(HandStatistics handStatistics) {
                int NumJacks = 0;
                int NumAces = 0;
                for(Card c : handStatistics.CardsHeldAtStart){
                    if(c.GetRank() == Rank.ACE){
                        NumAces++;
                    }else if(c.GetRank() == Rank.JACK){
                        NumJacks++;
                    }
                }

                return (NumAces >= 4) || (NumJacks == 4);
            }
        });
        System.out.println("The program took " + (System.nanoTime() - StartTime) + " to run.");

    }
}
