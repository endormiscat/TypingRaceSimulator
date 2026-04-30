import java.awt.*;
import javax.swing.*;

public class ConfigScreen extends JPanel
{
    private static final String SHORT_PASSAGE  = "The quick brown fox jumps over the lazy dog.";
    private static final String MEDIUM_PASSAGE = "The quick brown fox jumps over the lazy dog. Pack my box with five dozen liquor jugs. How valiantly did brave Ajax fight.";
    private static final String LONG_PASSAGE   = "The quick brown fox jumps over the lazy dog. Pack my box with five dozen liquor jugs. How valiantly did brave Ajax fight. Sphinx of black quartz, judge my vow. Two driven jocks help fax my big quiz.";

    private JComboBox<String> passageDropdown;
    private JTextArea passagePreview;
    private JSpinner seatCountSpinner;
    private JCheckBox autocorrectBox;
    private JCheckBox caffeineModeBox;
    private JCheckBox nightShiftBox;
    private RaceGUI raceGUI;

    public ConfigScreen(RaceGUI raceGUI)
    {
        this.raceGUI = raceGUI;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        add(buildPassagePanel());
        add(Box.createVerticalStrut(10));
        add(buildSeatCountPanel());
        add(Box.createVerticalStrut(10));
        add(buildDifficultyPanel());
        add(Box.createVerticalStrut(15));
        add(buildStartButton());
    }

    private JPanel buildPassagePanel()
    {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Passage"));

        String[] options = {"Short", "Medium", "Long", "Custom"};
        passageDropdown = new JComboBox<>(options);

        passagePreview = new JTextArea(5, 30); // fixed 5 rows — always visible
        passagePreview.setLineWrap(true);
        passagePreview.setWrapStyleWord(true);
        passagePreview.setText(SHORT_PASSAGE);
        passagePreview.setEditable(false);
        passagePreview.setFont(new Font("Courier New", Font.PLAIN, 13));

        passageDropdown.addActionListener(e -> {
            switch ((String) passageDropdown.getSelectedItem()) {
                case "Short":  passagePreview.setText(SHORT_PASSAGE);  passagePreview.setEditable(false); break;
                case "Medium": passagePreview.setText(MEDIUM_PASSAGE); passagePreview.setEditable(false); break;
                case "Long":   passagePreview.setText(LONG_PASSAGE);   passagePreview.setEditable(false); break;
                case "Custom": passagePreview.setText("");              passagePreview.setEditable(true);  break;
            }
        });

        panel.add(passageDropdown, BorderLayout.NORTH);
        panel.add(new JScrollPane(passagePreview), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildSeatCountPanel()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Number of Typists"));
        seatCountSpinner = new JSpinner(new SpinnerNumberModel(3, 2, 6, 1));
        panel.add(new JLabel("Typists (2-6): "));
        panel.add(seatCountSpinner);
        return panel;
    }

    private JPanel buildDifficultyPanel()
    {
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Difficulty Modifiers"));
        autocorrectBox  = new JCheckBox("Autocorrect (slideBack halved)");
        caffeineModeBox = new JCheckBox("Caffeine Mode (speed boost, then burnout risk)");
        nightShiftBox   = new JCheckBox("Night Shift (reduced accuracy)");
        panel.add(autocorrectBox);
        panel.add(caffeineModeBox);
        panel.add(nightShiftBox);
        return panel;
    }

    private JPanel buildStartButton()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btn = new JButton("Next: Configure Typists");
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.addActionListener(e -> onStartClicked());
        panel.add(btn);
        return panel;
    }

    private void onStartClicked()
    {
        if (getPassage().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a passage.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        raceGUI.showTypistConfig();
    }

    public String  getPassage()       { return passagePreview.getText().trim(); }
    public int     getSeatCount()     { return (int) seatCountSpinner.getValue(); }
    public boolean isAutocorrectOn()  { return autocorrectBox.isSelected(); }
    public boolean isCaffeineModeOn() { return caffeineModeBox.isSelected(); }
    public boolean isNightShiftOn()   { return nightShiftBox.isSelected(); }
}