import java.util.Comparator;

public class GameSimulator {
    public GameSimulator(){

    }

    public void Run(int TotalPoints){
        //TODO:: Run a game which plays until TotalPoints points have been scored by either team.
        EuchreGame Game = new EuchreGame();
        while(TotalPoints > 0){
            Game.SimulateHand();
            Game.AnnounceScore();
            TotalPoints -= Game.PointsScored;
        }

        Game.PrintHandStats(new Comparator<HandStatistics>() {
            @Override
            public int compare(HandStatistics o1, HandStatistics o2) {
                return 0;
            }
        });


    }
}
