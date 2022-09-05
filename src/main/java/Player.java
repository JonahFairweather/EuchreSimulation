package main.java;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {

    private Hand Cards = new Hand();

    public int NumTricksWon = 0;

    public boolean bIsDealer = false;

    public HandStatistics CurrentHandStats;

    public AbstractHandStatistics CurrentAbstractStatistics;

    public CardStatistics CurrentCardStats;

    private String Name;

    public Color Color;

    public void ClearHand(){
        Cards.ClearCards();
    }

    public Hand GetHand() { return Cards; }

    public Player(String name){
        Name = name;
    }
    public String GetName(){
        return Name;
    }

    public void SortCards(){
        Cards.SortHand();
    }

    public boolean WantsSuitAsTrump(Suit PossibleSuit){
        return Cards.GetHandStrength(PossibleSuit) >= 8;
    }

    public Card PlayCard(Color TeamColor, Suit LedSuit, Trick CurrentTrick, HashMap<Player, ArrayList<Card>> HandPlayerHistories){
        // In order to know which card is best to play you need the current trick, current winner, the current hand's trick score,
        // Each player's card history for this hand.
        return Cards.GetCard(TeamColor, LedSuit, CurrentTrick, HandPlayerHistories);
    }

    public void SetTrumps(Suit TrumpSuit){
        Cards.SetTrumps(TrumpSuit);
    }

    public void SetLedSuit(Suit ToSet){
        Cards.SetLedSuit(ToSet);
    }

    public void AddToHand(Card CardToAdd){
        Cards.AddToCards(CardToAdd);
    }

    public void RemoveFromHand(Card CardToRemove){
        Cards.RemoveFromHand(CardToRemove);
    }

    public String ToString(){
        return "Player name: " + Name + "\nPlayer Hand: " + Cards.ToString();
    }

    public Card DitchCard(ArrayList<Card> Cards1){
        return Cards.DitchCard(Cards1);
    }


}
