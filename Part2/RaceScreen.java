
import java.awt.*;
import javax.swing.*;

public class RaceScreen extends JPanel {

    private static final int TURN_DELAY = 200;
    private static final double MISTYPE_BASE = 0.30;
    private static final int SLIDE_BACK = 2;
    private static final int BURNOUT_DUR = 3;

    private RaceGUI raceGUI;
    private Typist[] typists;
    private String passage;
    private JPanel[] lanePanels;

    private boolean autocorrect, caffeineMode, nightShift;
    private boolean[] wristSupport, energyDrink, headphones;

    private int[] burnoutCounts, mistypeCounts, finishOrder;
    private long[] finishTimes;
    private int finishIndex = 0, turnCount = 0;
    private long startTime;

    private JLabel[] statusLabels;
    private JLabel headerLabel;
    private Timer timer;

    public RaceScreen(RaceGUI raceGUI, Typist[] typists, String passage,
            boolean autocorrect, boolean caffeineMode, boolean nightShift,
            boolean[] wristSupport, boolean[] energyDrink, boolean[] headphones) {
        this.raceGUI = raceGUI;
        this.typists = typists;
        this.passage = passage;
        this.autocorrect = autocorrect;
        this.caffeineMode = caffeineMode;
        this.nightShift = nightShift;
        this.wristSupport = wristSupport;
        this.energyDrink = energyDrink;
        this.headphones = headphones;

        burnoutCounts = new int[typists.length];
        mistypeCounts = new int[typists.length];
        finishOrder = new int[typists.length];
        finishTimes = new long[typists.length];
        statusLabels = new JLabel[typists.length];

        if (nightShift) {
            for (Typist t : typists) {
                t.setAccuracy(t.getAccuracy() - 0.10);
            }
        }

        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildLanes(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        for (Typist t : typists) {
            t.resetToStart();
        }
        startTime = System.currentTimeMillis();
        timer = new Timer(TURN_DELAY, e -> runTurn());
        timer.start();
    }

    private JLabel buildHeader() {
        headerLabel = new JLabel("Race in progress...", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        return headerLabel;
    }

    private JPanel buildLanes() {
        JPanel panel = new JPanel(new GridLayout(typists.length, 1, 0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        lanePanels = new JPanel[typists.length]; // initialise array

        for (int i = 0; i < typists.length; i++) {
            JPanel lane = new JPanel(new BorderLayout(5, 2));
            lane.setBorder(BorderFactory.createTitledBorder(
                    typists[i].getSymbol() + "  " + typists[i].getName()));

            JTextPane text = new JTextPane();
            text.setEditable(false);
            text.setFont(new Font("Courier New", Font.PLAIN, 13));
            text.setText(passage);
            lane.add(new JScrollPane(text), BorderLayout.CENTER);

            statusLabels[i] = new JLabel(" ");
            statusLabels[i].setFont(new Font("Arial", Font.ITALIC, 11));
            statusLabels[i].setForeground(Color.GRAY);
            lane.add(statusLabels[i], BorderLayout.SOUTH);

            lanePanels[i] = lane; // store reference
            panel.add(lane);
        }
        return panel;
    }

    private JPanel buildFooter() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton quit = new JButton("Quit");
        quit.addActionListener(e -> {
            timer.stop();
            raceGUI.showConfig();
        });
        panel.add(quit);
        return panel;
    }

    private void runTurn() {
        turnCount++;
        for (int i = 0; i < typists.length; i++) {
            if (hasFinished(i)) {
                continue;
            }
            advanceTypist(i);
            updateLane(i);
            if (typists[i].getProgress() >= passage.length()) {
                finishTimes[i] = System.currentTimeMillis();
                finishOrder[finishIndex++] = i;
            }
        }

        if (finishIndex == typists.length) {
            timer.stop();
            onRaceFinished();
        } else if (finishIndex > 0) {
            headerLabel.setText(typists[finishOrder[finishIndex - 1]].getName() + " finished!");
        }
    }

    private void advanceTypist(int i) {
        typists[i].clearMistype();

        if (typists[i].isBurntOut()) {
            typists[i].recoverFromBurnout();
            return;
        }

        double acc = typists[i].getAccuracy();
        if (caffeineMode) {
            acc += turnCount <= 10 ? 0.15 : -0.05;
        }
        if (energyDrink[i]) {
            acc += typists[i].getProgress() < passage.length() / 2 ? 0.10 : -0.10;
        }
        acc = Math.min(1.0, Math.max(0.0, acc));

        if (Math.random() < acc) {
            typists[i].typeCharacter();
        }

        double mistypeChance = (1 - acc) * MISTYPE_BASE;
        if (headphones[i]) {
            mistypeChance = Math.max(0, mistypeChance - 0.05);
        }
        if (Math.random() < mistypeChance) {
            typists[i].slideBack(autocorrect ? SLIDE_BACK / 2 : SLIDE_BACK);
            mistypeCounts[i]++;
        }

        double burnoutChance = 0.05 * acc * acc;
        if (caffeineMode && turnCount > 10) {
            burnoutChance += 0.05;
        }
        if (Math.random() < burnoutChance) {
            typists[i].burnOut(wristSupport[i] ? Math.max(1, BURNOUT_DUR - 1) : BURNOUT_DUR);
            burnoutCounts[i]++;
        }
    }

    private void updateLane(int i) {
        for (Component c : lanePanels[i].getComponents()) {
            if (c instanceof JScrollPane) {
                JTextPane tp = (JTextPane) ((JScrollPane) c).getViewport().getView();
                int prog = Math.min(typists[i].getProgress(), passage.length());
                javax.swing.text.StyledDocument doc = tp.getStyledDocument();
                javax.swing.text.Style done = tp.addStyle("done", null);
                javax.swing.text.Style normal = tp.addStyle("normal", null);
                javax.swing.text.StyleConstants.setForeground(done, new Color(34, 139, 34));
                javax.swing.text.StyleConstants.setForeground(normal, Color.BLACK);
                try {
                    doc.setCharacterAttributes(0, prog, doc.getStyle("done"), true);
                    doc.setCharacterAttributes(prog, passage.length() - prog, doc.getStyle("normal"), true);
                } catch (Exception ex) {
                }
            }
        }

        if (typists[i].isBurntOut()) {
            statusLabels[i].setText("💤 Burnt out — " + typists[i].getBurnoutTurnsRemaining() + " turns"); 
        }else if (typists[i].getJustMistyped()) {
            statusLabels[i].setText("❌ Mistyped!"); 
        }else if (hasFinished(i)) {
            statusLabels[i].setText("✅ Finished!"); 
        }else {
            statusLabels[i].setText(" ");
        }
    }

    private boolean hasFinished(int i) {
        for (int j = 0; j < finishIndex; j++) {
            if (finishOrder[j] == i) {
                return true;
            }
        }
        return false;
    }

    private void onRaceFinished() {
        int words = passage.split("\\s+").length;
        double[] wpms = new double[typists.length];
        for (int i = 0; i < typists.length; i++) {
            double timeMins = (finishTimes[i] - startTime) / 60000.0;
            wpms[i] = words / timeMins;
        }
        headerLabel.setText("Winner: " + typists[finishOrder[0]].getName() + "!");
        raceGUI.showStats(typists, wpms, burnoutCounts, mistypeCounts, finishOrder, passage);
    }
}
