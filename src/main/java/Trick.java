package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Trick {

    private ArrayList<PlayerCard> Plays = new ArrayList<>();

    private PlayerCard winner;

    private Suit TrumpSuit;

    private Card UpTurnedCard;

    public void setUpTurnedCard(Card upTurnedCard) {
        UpTurnedCard = upTurnedCard;
    }

    public Card getUpTurnedCard() {
        return UpTurnedCard;
    }

    public void setTrumpSuit(Suit trumpSuit) {
        TrumpSuit = trumpSuit;
    }

    public void AddPlayerCard(PlayerCard ToAdd){
        Plays.add(ToAdd);
        if(winner == null){
            winner = ToAdd;
        }else if(ToAdd.Card.CompareTo(winner.Card)){
            winner = ToAdd;
        }
    }

    public int GetNumPlays(){
        return Plays.size();
    }

    public PlayerCard PickWinner(){
        return winner;
    }



}
