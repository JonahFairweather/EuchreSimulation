package main.java;

public class Duo {
    public int TricksWon;

    public int Occurrences;

    public Duo(){
        TricksWon = 0;

        Occurrences = 0;
    }

    public float GetAverageTricksWon(){
        return TricksWon/Occurrences;
    }
}
