import java.awt.*;
import javax.swing.*;

public class RaceGUI
{
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ConfigScreen configScreen;
    private TypistConfigScreen typistConfigScreen;

    public RaceGUI()
    {
        frame = new JFrame("Typing Race Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(620, 560);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);

        configScreen = new ConfigScreen(this);
        mainPanel.add(configScreen, "config");

        frame.add(mainPanel);
        frame.setVisible(true);
        cardLayout.show(mainPanel, "config");
    }

    public void showConfig()
    {
        cardLayout.show(mainPanel, "config");
    }

    public void showTypistConfig()
    {
        typistConfigScreen = new TypistConfigScreen(this, configScreen.getSeatCount());
        mainPanel.add(typistConfigScreen, "typistConfig");
        cardLayout.show(mainPanel, "typistConfig");
    }

    public void showRace(Typist[] typists)
    {
        RaceScreen raceScreen = new RaceScreen(
            this, typists,
            configScreen.getPassage(),
            configScreen.isAutocorrectOn(),
            configScreen.isCaffeineModeOn(),
            configScreen.isNightShiftOn(),
            typistConfigScreen.getWristSupport(),
            typistConfigScreen.getEnergyDrink(),
            typistConfigScreen.getHeadphones()
        );
        mainPanel.add(raceScreen, "race");
        cardLayout.show(mainPanel, "race");
    }

    public void showStats(Typist[] typists, double[] wpms, int[] burnouts,
            int[] mistypes, int[] finishOrder, String passage) {
        StatsScreen stats = new StatsScreen(
                this, typists, wpms, burnouts, mistypes, finishOrder, passage);
        mainPanel.add(stats, "stats");
        cardLayout.show(mainPanel, "stats");
    }

    public void showLeaderboard(Typist[] typists, int[] finishOrder, double[] wpms, int[] burnouts) {
        // Convert finishOrder to positions array
        int[] positions = new int[typists.length];
        for (int i = 0; i < finishOrder.length; i++) {
            positions[finishOrder[i]] = i + 1;
        }

        LeaderboardScreen lb = new LeaderboardScreen(this, typists, positions, wpms, burnouts);
        mainPanel.add(lb, "leaderboard");
        cardLayout.show(mainPanel, "leaderboard");
    }

    public static void main(String[] args)
    {
        new RaceGUI();
    }
}