
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class StatsScreen extends JPanel {

    private RaceGUI raceGUI;
    private Typist[] typists;
    private double[] wpms;
    private int[] burnouts, mistypes, finishOrder;
    private String passage;

    public StatsScreen(RaceGUI raceGUI, Typist[] typists, double[] wpms,
            int[] burnouts, int[] mistypes, int[] finishOrder, String passage) {
        this.raceGUI = raceGUI;
        this.typists = typists;
        this.wpms = wpms;
        this.burnouts = burnouts;
        this.mistypes = mistypes;
        this.finishOrder = finishOrder;
        this.passage = passage;

        // Save this race
        String[] names = new String[typists.length];
        double[] accuracies = new double[typists.length];
        for (int i = 0; i < typists.length; i++) {
            names[i] = typists[i].getName();
            int total = typists[i].getProgress() + mistypes[i];
            accuracies[i] = total == 0 ? 0 : (double) typists[i].getProgress() / total;
        }
        StatsManager.saveRace(names, wpms, accuracies, burnouts, finishOrder);

        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(names, accuracies), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JLabel buildHeader() {
        JLabel label = new JLabel("Race Results — "
                + typists[finishOrder[0]].getName() + " wins!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private JTabbedPane buildTabs(String[] names, double[] accuracies) {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("This Race", buildRacePanel(names, accuracies));
        tabs.addTab("Personal Bests", buildBestsPanel(names));
        tabs.addTab("Compare", buildComparePanel(names));
        return tabs;
    }

    // ── This Race ─────────────────────────────────────────────────
    private JPanel buildRacePanel(String[] names, double[] accuracies) {
        // Find fastest and most burnouts
        int fastestIndex = 0;
        int burnoutIndex = 0;
        int mostBurnouts = 0;
        for (int i = 0; i < typists.length; i++) {
            if (wpms[i] > wpms[fastestIndex]) {
                fastestIndex = i;
            }
            if (burnouts[i] > mostBurnouts) {
                mostBurnouts = burnouts[i];
                burnoutIndex = i;
            }
        }

        final int fi = fastestIndex;
        final int bi = burnoutIndex;

        JPanel panel = new JPanel(new GridLayout(typists.length + 2, 6, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header row
        for (String h : new String[]{"Position", "Name", "WPM", "Accuracy", "Burnouts", "Points"}) {
            JLabel lbl = new JLabel(h, SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 12));
            panel.add(lbl);
        }

        // One row per typist in finish order
        for (int rank = 0; rank < typists.length; rank++) {
            int i = finishOrder[rank];
            int pts = LeaderboardManager.calculatePoints(rank + 1);
            String pointsStr = String.valueOf(pts);
            if (i == fi) {
                pointsStr += " +1 ⚡ fastest";
            }
            if (i == bi && mostBurnouts > 0) {
                pointsStr += " -1 💤 burnouts";
            }

            panel.add(centredLabel("#" + (rank + 1)));
            panel.add(centredLabel(typists[i].getName()));
            panel.add(centredLabel(String.format("%.1f", wpms[i])));
            panel.add(centredLabel(String.format("%.0f%%", accuracies[i] * 100)));
            panel.add(centredLabel(String.valueOf(burnouts[i])));
            panel.add(centredLabel(pointsStr));
        }

        // Key row at the bottom
        JLabel key = new JLabel("$ = fastest WPM bonus (+1 pt)    & = most burnouts penalty (-1 pt)",
                SwingConstants.CENTER);
        key.setFont(new Font("Arial", Font.ITALIC, 11));
        key.setForeground(Color.GRAY);
        for (int col = 0; col < 6; col++) {
            panel.add(col == 0 ? key : new JLabel());
        }

        return panel;
    }

    // ── Personal Bests ────────────────────────────────────────────
    private JPanel buildBestsPanel(String[] names) {
        JPanel panel = new JPanel(new GridLayout(typists.length + 1, 3, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (String h : new String[]{"Name", "Best WPM", "Races Played"}) {
            JLabel lbl = new JLabel(h, SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 12));
            panel.add(lbl);
        }

        for (String name : names) {
            double best = StatsManager.getBestWPM(name);
            int races = StatsManager.getHistory(name).size();
            panel.add(centredLabel(name));
            panel.add(centredLabel(String.format("%.1f", best)));
            panel.add(centredLabel(String.valueOf(races)));
        }
        return panel;
    }

    // ── Compare ───────────────────────────────────────────────────
    private JPanel buildComparePanel(String[] names) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Two dropdowns to pick typists
        JComboBox<String> dropA = new JComboBox<>(names);
        JComboBox<String> dropB = new JComboBox<>(names);
        if (names.length > 1) {
            dropB.setSelectedIndex(1);
        }

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
        top.add(new JLabel("Compare:"));
        top.add(dropA);
        top.add(new JLabel("vs"));
        top.add(dropB);
        panel.add(top, BorderLayout.NORTH);

        // Result panel updated on change
        JPanel result = new JPanel(new GridLayout(4, 3, 5, 5));
        panel.add(result, BorderLayout.CENTER);

        Runnable update = () -> {
            result.removeAll();
            String nameA = (String) dropA.getSelectedItem();
            String nameB = (String) dropB.getSelectedItem();

            List<double[]> histA = StatsManager.getHistory(nameA);
            List<double[]> histB = StatsManager.getHistory(nameB);

            double avgWpmA = histA.stream().mapToDouble(e -> e[0]).average().orElse(0);
            double avgWpmB = histB.stream().mapToDouble(e -> e[0]).average().orElse(0);
            double avgAccA = histA.stream().mapToDouble(e -> e[1]).average().orElse(0);
            double avgAccB = histB.stream().mapToDouble(e -> e[1]).average().orElse(0);
            double avgBrnA = histA.stream().mapToDouble(e -> e[2]).average().orElse(0);
            double avgBrnB = histB.stream().mapToDouble(e -> e[2]).average().orElse(0);

            for (String h : new String[]{"Metric", nameA, nameB}) {
                JLabel lbl = new JLabel(h, SwingConstants.CENTER);
                lbl.setFont(new Font("Arial", Font.BOLD, 12));
                result.add(lbl);
            }

            result.add(centredLabel("Avg WPM"));
            result.add(centredLabel(String.format("%.1f", avgWpmA)));
            result.add(centredLabel(String.format("%.1f", avgWpmB)));

            result.add(centredLabel("Avg Accuracy"));
            result.add(centredLabel(String.format("%.0f%%", avgAccA * 100)));
            result.add(centredLabel(String.format("%.0f%%", avgAccB * 100)));

            result.add(centredLabel("Avg Burnouts"));
            result.add(centredLabel(String.format("%.1f", avgBrnA)));
            result.add(centredLabel(String.format("%.1f", avgBrnB)));

            result.revalidate();
            result.repaint();
        };

        dropA.addActionListener(e -> update.run());
        dropB.addActionListener(e -> update.run());
        update.run();

        return panel;
    }

    private JLabel centredLabel(String text) {
        return new JLabel(text, SwingConstants.CENTER);
    }

    private JPanel buildFooter() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton again = new JButton("Race Again");
        again.addActionListener(e -> raceGUI.showConfig());

        JButton leaderboard = new JButton("🏆 Leaderboard");
        leaderboard.addActionListener(e -> raceGUI.showLeaderboard(typists, finishOrder, wpms, burnouts));

        panel.add(again);
        panel.add(leaderboard);
        return panel;
    }
}
