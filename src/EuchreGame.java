import java.util.*;

public class EuchreGame {

    public int PointsScored;

    private Suit TrumpSuit;

    private Suit UpturnedSuit;

    private HashMap<Hand, Integer> TricksWonByHand = new HashMap<>();

    private HashSet<HandStatistics> HandStats = new HashSet<>();

    private Player Dealer;

    private Player Leader;

    private boolean VerbosePlayerInfo = false;

    public Color TeamWhoCalled;

    private int RedTeamTricks = 0;

    private int BlackTeamTricks = 0;

    private int RedTeamPoints = 0;

    private int BlackTeamPoints = 0;

   private ArrayList<Player> Players = new ArrayList<>();

   private ArrayList<Card> Deck = new ArrayList<>();


    public static void main(String[] args){
        EuchreGame E = new EuchreGame();

        E.SimulateHand();
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
            CurrentHandStats.CacheCards(p.GetHand().HeldCards);
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

        Card UpTurnedCard = Deck.remove(Deck.size() - 1);
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
            for(int j = 0; j <= 3; j++){
                System.out.println();
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



        //System.out.println(UpTurnedCard.GetSuit().toString() + " Is the Trump Suit for this hand.");

        // At this point the players should have the opportunity to call the suit or not, but in this instance
        // We will just let the players play

        for(int Trick = 0; Trick <= 4; Trick++){
            PlayTrick();
            //System.out.println(Leader.GetName() + " is the leader for this trick.");

        }
        for(Player p : Players){
            System.out.println(p.GetName() + " won " + p.NumTricksWon + " tricks.");
            p.CurrentHandStats.NumOccurrences++;
            p.CurrentHandStats.NumTricksWon += p.NumTricksWon;
        }
        System.out.println("Red team won: " + RedTeamTricks + " Black team won: " + BlackTeamTricks
                + " " + TeamWhoCalled.toString() + " team called the suit.\n");
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
        for(Player p : Players){
            p.SetTrumps(TrumpSuit);
        }
    }
    public void PlayTrick(){
        Player CurrentPlayer = Leader;

        Trick CurrentTrick = new Trick();
        PlayerCard Play = new PlayerCard(CurrentPlayer, CurrentPlayer.PlayCard(Suit.NoSuit));
        Suit LedSuit = Play.Card.GetSuit();
        for(Player p : Players){
            p.SetLedSuit(LedSuit);
        }
        if(VerbosePlayerInfo){
            System.out.println("Player: " + CurrentPlayer.GetName() + " has led: " + Play.Card.ToString());
        }
        int Index = Players.indexOf(CurrentPlayer) + 1;
        if(Index == 4){
            Index = 0;
        }
        CurrentPlayer = Players.get(Index);
        CurrentTrick.AddPlayerCard(Play);
        for(int i = 0; i <= 2; i++){
            PlayerCard PlayersPlay = new PlayerCard(CurrentPlayer, CurrentPlayer.PlayCard(LedSuit));
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
        //System.out.println("Player " + Winner.Player.GetName() + " has won with: " + Winner.Card.ToString());
        if(Winner.Player.Color == Color.BLACK){
            BlackTeamTricks++;
        }else{
            RedTeamTricks++;
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

        for(HandStatistics h : HandStats){
            Str.append("The following hand: ");
            for(Card c : h.CardsHeldAtStart){
                Str.append(c.ToString() + " ");
            }
            Str.append(" won " + h.NumTricksWon + " tricks in " + h.NumOccurrences + " times being held for an average of " +
                    h.NumTricksWon/h.NumOccurrences + " tricks won per occurrence.\n");

        }

        System.out.println(Str.toString());
        System.out.println(HandStats.size());
    }


}
