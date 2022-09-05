package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Trick {

    private ArrayList<PlayerCard> Plays = new ArrayList<>();

    public void AddPlayerCard(PlayerCard ToAdd){
        Plays.add(ToAdd);
    }

    public PlayerCard PickWinner(){
        PlayerCard Winner = Plays.get(0);
        Plays.remove(0);
        for(PlayerCard Play : Plays){

            if(!Winner.Card.CompareTo(Play.Card)){
                //System.out.println(Play.Card.ToString() + " has beaten " + Winner.Card.ToString());

                Winner = Play;

            }
            //System.out.println(Winner.Card.ToString() + " has won!");


        }
        return Winner;
    }



}
