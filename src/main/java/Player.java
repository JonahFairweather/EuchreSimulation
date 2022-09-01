package main.java;

public class Player {

    private Hand Cards = new Hand();

    public int NumTricksWon = 0;

    public boolean bIsDealer = false;

    public HandStatistics CurrentHandStats;

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

    public Card PlayCard(Suit LedSuit){
        return Cards.GetRandomCard(LedSuit);
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

    public Card DitchCard(){
        return Cards.DitchCard();
    }


}
