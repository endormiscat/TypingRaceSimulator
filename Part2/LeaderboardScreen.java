import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class LeaderboardScreen extends JPanel
{
    private RaceGUI raceGUI;

    public LeaderboardScreen(RaceGUI raceGUI, Typist[] typists,
                             int[] positions, double[] wpms, int[] burnouts)
    {
        this.raceGUI = raceGUI;

        // Save this race to leaderboard
        String[] names = new String[typists.length];
        for (int i = 0; i < typists.length; i++) names[i] = typists[i].getName();
        LeaderboardManager.saveRace(names, positions, wpms, burnouts);

        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("🏆 Leaderboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);
        add(buildLeaderboard(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildLeaderboard()
    {
        Map<String, Integer> totals = LeaderboardManager.loadTotals();

        // Sort by points descending
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(totals.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());

        JPanel panel = new JPanel(new GridLayout(sorted.size() + 1, 3, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        for (String h : new String[]{"Name", "Total Points", "Title"})
        {
            JLabel lbl = new JLabel(h, SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 13));
            panel.add(lbl);
        }

        // Rows
        for (Map.Entry<String, Integer> entry : sorted)
        {
            panel.add(new JLabel(entry.getKey(), SwingConstants.CENTER));
            panel.add(new JLabel(String.valueOf(entry.getValue()), SwingConstants.CENTER));
            panel.add(new JLabel(LeaderboardManager.getTitle(entry.getKey()), SwingConstants.CENTER));
        }

        return panel;
    }

    private JPanel buildFooter()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton again = new JButton("Race Again");
        again.addActionListener(e -> raceGUI.showConfig());
        panel.add(again);
        return panel;
    }
}