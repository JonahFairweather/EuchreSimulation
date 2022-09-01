package main.java;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

import java.util.function.Predicate;
import java.util.logging.Filter;
import java.util.stream.Stream;

public class EuchreGame {

    public int PointsScored;

    private Suit TrumpSuit;

    private Suit UpturnedSuit;

    private int HandsWithBothBuried;

    private int NumHandsPlayed;

    private int NumHandsWithUpturnedJack;

    private int NumWithBauerTurnedDown;

    private HashMap<Hand, Integer> TricksWonByHand = new HashMap<>();

    private HashSet<HandStatistics> HandStats = new HashSet<>();



    private Player Dealer;

    private Player Leader;

    private boolean bCanadianLoner;

    private boolean bCachedCanadianLoner = false;

    public void SetCanadianLoner(boolean ToSet){
        if(!bAloneHandsAllowed){
            bCachedCanadianLoner = true;
        }else{
            bCanadianLoner = false;
        }

    }

    private boolean bAloneHandsAllowed;

    public void SetAloneHandsAllowed(boolean ToSet){
        // If Alone is not allowed, neither is Canadian Loner.
        bAloneHandsAllowed = ToSet;
        if(!ToSet){
            bCanadianLoner = false;
        }else{
            if(bCachedCanadianLoner){
                bCanadianLoner = true;
                bCachedCanadianLoner = false;
            }
        }
    }

    public boolean VerbosePlayerInfo = false;

    public Color TeamWhoCalled;

    private int RedTeamTricks = 0;

    private int BlackTeamTricks = 0;

    private int RedTeamPoints = 0;

    private int BlackTeamPoints = 0;



   private ArrayList<Player> Players = new ArrayList<>();

   private ArrayList<Card> Deck = new ArrayList<>();


    public static void main(String[] args){
        EuchreGame E = new EuchreGame();
        Card c = new Card(Suit.Clubs, Rank.JACK);
        Card d = new Card(Suit.Clubs, Rank.KING);
        c.SetIsTrump(true);
        d.SetIsTrump(true);
        System.out.println(d.CompareTo(c));
        E.SimulateHand();
    }


    public EuchreGame(){
        HandsWithBothBuried = 0;
        NumHandsWithUpturnedJack = 0;
        NumHandsPlayed = 0;
        NumWithBauerTurnedDown = 0;
    }

    public void SimulateHand(ArrayList<Player> Players, Suit trump){
        this.Players = Players;
        TrumpSuit = trump;
        SetHandTrump(trump);
        Random rd = new Random();
        int DealerIndex = rd.nextInt(Players.size());

        Dealer = Players.get(DealerIndex);
        Dealer.bIsDealer = true;
        int LeaderIndex = DealerIndex + 1;
        if(LeaderIndex == Players.size()){
            LeaderIndex = 0;
        }

        Leader = Players.get(LeaderIndex);

        for(int Trick = 0; Trick <= 4; Trick++){
            for(Player p : Players){
                System.out.println(p.ToString());
            }
            PlayTrick();
            //System.out.println(Leader.GetName() + " is the leader for this trick.");

        }

        for(Player p : Players){
            System.out.println("Player " + p.GetName() + " won " + p.NumTricksWon);
        }

    }
    public void SimulateHand(){
        RedTeamTricks = 0;
        BlackTeamTricks = 0;
        Players.clear();
        ShuffleDeck();
        Player Jonah = new Player("Jonah");
        Jonah.Color = Color.BLACK;
        Player James = new Player("James");
        James.Color = Color.RED;
        Player Barb = new Player("Barbra");
        Barb.Color = Color.BLACK;
        Player Allison = new Player("Allison");
        Allison.Color = Color.RED;

        Players.add(Jonah);
        Players.add(James);
        Players.add(Barb);
        Players.add(Allison);

        Random rd = new Random();
        int DealerIndex = rd.nextInt(Players.size());

        Dealer = Players.get(DealerIndex);
        Dealer.bIsDealer = true;
        int LeaderIndex = DealerIndex + 1;
        if(LeaderIndex == Players.size()){
            LeaderIndex = 0;
        }
        Leader = Players.get(LeaderIndex);
        for(Player p : Players){
            p.ClearHand();
        }

        DealCards();

        for(Player p : Players){
            p.SortCards();
           // System.out.println(p.ToString());
            //Cache this hand and then map it to the
            HandStatistics CurrentHandStats = new HandStatistics();
            CurrentHandStats.CacheCards(p);
            if(HandStats.contains(CurrentHandStats)){
                //System.out.println("Identical Hand Stats Found");
                for(HandStatistics h : HandStats){
                    if(h.hashCode() == CurrentHandStats.hashCode()){
                        p.CurrentHandStats = h;
                    }
                }
            }else{
                p.CurrentHandStats = CurrentHandStats;
                //System.out.println("Adding Hand Stats");
                HandStats.add(CurrentHandStats);
               // System.out.println(HandStats.size());
            }
        }

        Card UpTurnedCard = Deck.get(Deck.size() - 1);

        if(UpTurnedCard.GetRank() == Rank.JACK){

            NumHandsWithUpturnedJack++;
        }
        Suit UpturnedSuit = UpTurnedCard.GetSuit();
        UpTurnedCard.SetIsTrump(true);
        //System.out.println(UpTurnedCard.ToString());
        //System.out.println();
        boolean SuitHasBeenCalled = false;


        Suit CallableTrumpSuit = UpTurnedCard.GetSuit();
        int i = Players.indexOf(Dealer) + 1;
        //i represents the index of the first player to get to call;
        for(int j = 0; j <= 3; j++){
           // System.out.println();
            if(i == 4){
                i = 0;
            }
            Player CurrentPlayer = Players.get(i);
            CurrentPlayer.SetTrumps(CallableTrumpSuit);
            if(CurrentPlayer == Dealer){
                CurrentPlayer.AddToHand(UpTurnedCard);
                Card temp = CurrentPlayer.DitchCard();
                CurrentPlayer.RemoveFromHand(temp);


                if(CurrentPlayer.WantsSuitAsTrump(CallableTrumpSuit)){

                    //System.out.println("Player " + CurrentPlayer.GetName() + " has called " + CallableTrumpSuit.toString() + " as Trump."
                           // + " Their hand contains:\n" + CurrentPlayer.GetHand().ToString());
                    TeamWhoCalled = CurrentPlayer.Color;
                    SuitHasBeenCalled = true;
                    SetHandTrump(CallableTrumpSuit);
                    break;
                }else{
                    CurrentPlayer.RemoveFromHand(UpTurnedCard);
                    CurrentPlayer.AddToHand(temp);
                    //If the dealer does not want this card then remove it from the hand and give back their worst card
                    //System.out.println("Player " + CurrentPlayer.GetName() + " has passed on " + CallableTrumpSuit.toString() + " as Trump."
                            //+ " Their hand contains:\n" + CurrentPlayer.GetHand().ToString());
                }
            }else{
                if(CurrentPlayer.WantsSuitAsTrump(CallableTrumpSuit)){
                    Dealer.AddToHand(UpTurnedCard);
                    Card c = Dealer.DitchCard();
                    Dealer.RemoveFromHand(c);
                    //System.out.println("Player " + CurrentPlayer.GetName() + " has called " + CallableTrumpSuit.toString() + " as Trump."
                           // + " Their hand contains:\n" + CurrentPlayer.GetHand().ToString());
                   // System.out.println("Player " + Dealer.GetName() + " has picked up " + UpTurnedCard.ToString() + " and added it to their hand: " +
                           // Dealer.GetHand().ToString());
                    TeamWhoCalled = CurrentPlayer.Color;
                    SuitHasBeenCalled = true;

                    SetHandTrump(CallableTrumpSuit);
                    break;
                }else{
                    //System.out.println("Player " + CurrentPlayer.GetName() + " has passed on " + CallableTrumpSuit.toString() + " as Trump."
                           // + " Their hand contains:\n" + CurrentPlayer.GetHand().ToString());
                }
            }

            i++;
        }

        if(!SuitHasBeenCalled){
            if(UpTurnedCard.GetIsTrump() && UpTurnedCard.GetRank().equals(Rank.JACK)){
                NumWithBauerTurnedDown++;
            }
            for(int j = 0; j <= 3; j++){
                //System.out.println();
                if(SuitHasBeenCalled){
                    break;
                }
                if(i == 4){
                    i = 0;
                }
                Player CurrentPlayer = Players.get(i);
                if(j == 3){
                    // Dealer has been stuck, find which suit is best for the dealer.
                    Suit BestSuit = Suit.NoSuit;
                    int BestSuitScore = Integer.MIN_VALUE;
                    for(Suit s : Suit.values()){

                        if(s == Suit.NoSuit || s == UpturnedSuit){
                            //Don't want to do anything with these suits
                            continue;
                        }else{
                            CurrentPlayer.SetTrumps(s);
                            int ScoreForSuit = CurrentPlayer.GetHand().GetHandStrength(s);
                            if(ScoreForSuit > BestSuitScore){
                                BestSuit = s;
                                BestSuitScore = ScoreForSuit;
                            }
                        }
                    }
                    SetHandTrump(BestSuit);
                    //System.out.println("Player " + CurrentPlayer.GetName() + " has called " + BestSuit.toString() + " as Trump."
                           // + " Their hand contains:\n" + CurrentPlayer.GetHand().ToString());
                    TeamWhoCalled = CurrentPlayer.Color;

                }else{
                    for(Suit s : Suit.values()){
                        if(s == Suit.NoSuit || s == UpturnedSuit){
                            //Don't want to do anything with these suits
                            continue;
                        }
                        CurrentPlayer.SetTrumps(s);
                        if(CurrentPlayer.WantsSuitAsTrump(s)){

                           // System.out.println("Player " + CurrentPlayer.GetName() + " has called " + s.toString() + " as Trump."
                                    //+ " Their hand contains:\n" + CurrentPlayer.GetHand().ToString());
                            TeamWhoCalled = CurrentPlayer.Color;
                            SuitHasBeenCalled = true;
                            SetHandTrump(s);
                            break;
                        }
                    }
                }

                i++;
            }

        }

        //At this point the trump has been picked, cards are FINAL
        CheckIfBothBauersBuried();

        for(Player p : Players){
            p.CurrentHandStats.SetCurrentTrumpSuit(TrumpSuit);
        }





        //System.out.println(TrumpSuit.toString());

        //System.out.println(UpTurnedCard.GetSuit().toString() + " Is the Trump Suit for this hand.");

        // At this point the players should have the opportunity to call the suit or not, but in this instance
        // We will just let the players play

        for(int Trick = 0; Trick <= 4; Trick++){
            PlayTrick();
            //System.out.println(Leader.GetName() + " is the leader for this trick.");

        }
        for(Player p : Players){
            //System.out.println(p.GetName() + " won " + p.NumTricksWon + " tricks.");
            p.CurrentHandStats.IncreaseNumOccurrences();
            p.CurrentHandStats.IncreaseNumTricksWon(p.NumTricksWon);
        }
       // System.out.println("Red team won: " + RedTeamTricks + " Black team won: " + BlackTeamTricks
                //+ " " + TeamWhoCalled.toString() + " team called the suit.\n");
        Color TeamWhoScored = Color.RED;
        int PointsScored = 0;
        if(TeamWhoCalled == Color.BLACK && RedTeamTricks > BlackTeamTricks){
            PointsScored = 2;
            TeamWhoScored = Color.RED;
        }else if(TeamWhoCalled == Color.RED && BlackTeamTricks > RedTeamTricks){
            PointsScored = 2;
            TeamWhoScored = Color.BLACK;
        }else if(TeamWhoCalled == Color.BLACK && BlackTeamTricks > RedTeamTricks){
            if(BlackTeamTricks == 5){
                PointsScored = 2;
            }else{
                PointsScored = 1;
            }
            TeamWhoScored = Color.BLACK;
        }else if(TeamWhoCalled == Color.RED && RedTeamTricks > BlackTeamTricks){
            if(RedTeamTricks == 5){
                PointsScored = 2;
            }else{
                PointsScored = 1;
            }
            TeamWhoScored = Color.RED;
        }
        if(TeamWhoScored == Color.RED){
            RedTeamPoints += PointsScored;
        }else{
            BlackTeamPoints += PointsScored;
        }
        //System.out.println(TeamWhoScored.toString() + " has scored " + PointsScored + " points.");

        this.PointsScored = PointsScored;

        this.NumHandsPlayed++;
    }

    public void DealCards(){
        for(int i = 0; i <= 4; i++){
            for(int j = 0; j <= 3; j++){
                Player CurrentPlayer = Players.get(j);
                CurrentPlayer.AddToHand(Deck.remove(Deck.size() - 1));
            }
        }
    }

    public void SetHandTrump(Suit TrumpSuit){
        this.TrumpSuit = TrumpSuit;
        for(Card c : Deck){
            if(c.GetSuit() == TrumpSuit){
                c.SetIsTrump(true);
            }
        }
        for(Player p : Players){
            p.SetTrumps(TrumpSuit);
        }
    }
    public void PlayTrick(){
        Player CurrentPlayer = Leader;

        Trick CurrentTrick = new Trick();
        Card CardPlay = CurrentPlayer.PlayCard(Suit.NoSuit);
        CurrentPlayer.RemoveFromHand(CardPlay);
        PlayerCard Play = new PlayerCard(CurrentPlayer, CardPlay);
        Suit LedSuit = Play.Card.GetSuit();
        for(Player p : Players){
            p.SetLedSuit(LedSuit);
        }
        if(VerbosePlayerInfo){
            System.out.println("Player: " + CurrentPlayer.GetName() + " has led: " + Play.Card.ToString() +
                    (Play.Card.GetIsTrump() ? "[T]" : "" + (Play.Card.GetIsLedSuit() ? "[L]" : "")));
        }
        int Index = Players.indexOf(CurrentPlayer) + 1;
        if(Index == 4){
            Index = 0;
        }
        CurrentPlayer = Players.get(Index);
        CurrentTrick.AddPlayerCard(Play);
        for(int i = 0; i <= 2; i++){
            Card c = CurrentPlayer.PlayCard(LedSuit);
            CurrentPlayer.RemoveFromHand(c);
            PlayerCard PlayersPlay = new PlayerCard(CurrentPlayer, c);
            CurrentTrick.AddPlayerCard(PlayersPlay);
            String Append;
            if(PlayersPlay.Card.GetIsTrump()){
                Append = "[T]";
            }else{
                Append = "";
            }
            String Le;
            if(PlayersPlay.Card.GetIsLedSuit()){
                Le = "[L]";
            }else{
                Le = "";
            }
            if(VerbosePlayerInfo){
                System.out.println("Player: " + CurrentPlayer.GetName() + " has played: " + PlayersPlay.Card.ToString() + Append + " " + Le);
            }


            int I = Players.indexOf(CurrentPlayer) + 1;
            if(I == 4){
                I = 0;
            }
            CurrentPlayer = Players.get(I);

        }

        PlayerCard Winner = CurrentTrick.PickWinner();
        Leader = Winner.Player;
        Winner.Player.NumTricksWon++;
        if(VerbosePlayerInfo){
            System.out.println("Player " + Winner.Player.GetName() + " has won with: " + Winner.Card.ToString());
        }


        if(Winner.Player.Color == Color.BLACK){
            BlackTeamTricks++;
        }else{
            RedTeamTricks++;
        }
    }

    public void CheckIfBothBauersBuried(){
        int BauersFound = 0;
        for(Card c : Deck){
            if(c.GetIsLeftBauer()){
                BauersFound++;
            }else if(c.GetRank().equals(Rank.JACK) && c.GetIsTrump()){
                BauersFound++;
            }
        }
        if(BauersFound == 2){
            System.out.println("Both bauers are buried!");
            HandsWithBothBuried++;
        }
    }

    public void PrintAllBauerStats(){
        StringBuilder Str = new StringBuilder();
        Str.append("There were " + NumHandsPlayed + " hands played.\n " + "There were " + HandsWithBothBuried + " hands in which both bauers were in the deck."
        + " On the contrary there were " + NumHandsWithUpturnedJack + " hands in which there was an upturned jack.\n"
        + "There were" + (NumHandsWithUpturnedJack - NumWithBauerTurnedDown) + " hands in which a bauer was turned DOWN.");
        System.out.println(Str);
    }

    public void WriteToFile(String Filename, Comparator<HandStatistics> Comp, int PointsSimulated, Predicate<HandStatistics> FilterBy){

        try{
            StringBuilder Str = new StringBuilder();
            Str.append(Filename);
            Str.append("\\OutputSpreadsheet.xlsx");
            FileInputStream file = new FileInputStream(new File(Str.toString()));

            Workbook Workbook = new XSSFWorkbook(file);
            StringBuilder Str2 = new StringBuilder();
            Str2.append(PointsSimulated);
            Str2.append("DataFourJacksOrFourAces");

            Sheet sheet = Workbook.createSheet(Str2.toString());
            sheet.setColumnWidth(0, 6000);
            sheet.setColumnWidth(1, 4000);
            ArrayList<HandStatistics> Hands = new ArrayList<>(HandStats);
            Collections.sort(Hands, Comp);
            Hands.removeIf(h -> !FilterBy.test(h));
            for(int i = 0; i <= Hands.size() - 1 ; i++){
                Row CurrentRow = sheet.createRow(i);
                WriteHandstatisticsToRow(i, sheet, Hands.get(i));
            }
            FileOutputStream Output = new FileOutputStream(Str.toString());
            Workbook.write(Output);
            Workbook.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void WriteHandstatisticsToRow(int RowToWriteTo, Sheet WriteTo, HandStatistics StatsToWrite){
        Row CurrentRow = WriteTo.createRow(RowToWriteTo);
        Cell FirstCell = CurrentRow.createCell(0);
        StringBuilder Str = new StringBuilder();
        for(Card c : StatsToWrite.CardsHeldAtStart){
            Str.append(c.ToString());
        }
        FirstCell.setCellValue(Str.toString());

        Cell SecondCell = CurrentRow.createCell(1);
        SecondCell.setCellValue(StatsToWrite.GetNumOccurrences());

        Cell ThirdCell = CurrentRow.createCell(2);
        ThirdCell.setCellValue(StatsToWrite.NumTricksWon);

        Cell FourthCell = CurrentRow.createCell(3);

        FourthCell.setCellValue(StatsToWrite.AverageTricksWon());

        for(Suit s : Suit.values()){
            if(s.equals(Suit.NoSuit)) break;
            Cell CurCell = CurrentRow.createCell(s.ordinal() + 4);
            CurCell.setCellValue(StatsToWrite.GetStatsBySuit(s));
        }


    }

    public void AnnounceScore(){
        System.out.println("Red Team: " + RedTeamPoints + " Black Team: " + BlackTeamPoints);
    }

    public void ShuffleDeck(){
        Deck.clear();
        for(Suit s : Suit.values()){
            for(Rank r : Rank.values()){
                if(s == Suit.NoSuit) continue;
                Deck.add(new Card(s, r));
            }
        }

        Collections.shuffle(Deck);


    }

    public void PrintDeck(){
        Collections.sort(Deck, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                if(o1.GetSuit().compareTo(o2.GetSuit()) > 0){

                    return 1;
                }else if(o1.GetSuit().compareTo(o2.GetSuit()) < 0){
                    return -1;
                }else{
                    // suits are equal
                    return o1.GetRank().compareTo(o2.GetRank());
                }
            }
        });
        for(Card c : Deck){
            System.out.println(c.ToString());
        }
    }

    public void PrintHandStats(Comparator<HandStatistics> SortBy){

        System.out.println();
        System.out.println();
        System.out.println("NOW PRINTING HAND STATS");
        StringBuilder Str = new StringBuilder();
        ArrayList<HandStatistics> Hands = new ArrayList<>(HandStats);
        Collections.sort(Hands, SortBy);
        for(HandStatistics h : Hands){
            System.out.println(h.ToString());

        }

        System.out.println(Str.toString());

    }


}
