import java.awt.*;
import javax.swing.*;

public class TypistConfigScreen extends JPanel
{
    private static final String[] STYLE_NAMES    = {"Touch Typist", "Hunt & Peck", "Phone Thumbs", "Voice-to-Text"};
    private static final String[] KEYBOARD_NAMES = {"Mechanical", "Membrane", "Touchscreen", "Stenography"};
    private static final String[] SYMBOLS        = {"①","②","③","④","⑤","⑥"};

    private RaceGUI raceGUI;
    private int seatCount;
    private JTabbedPane tabs;

    private JTextField[]  nameFields;
    private JComboBox[]   styleDropdowns;
    private JComboBox[]   keyboardDropdowns;
    private JTextField[]  symbolFields;
    private JCheckBox[][] accessoryBoxes;

    public TypistConfigScreen(RaceGUI raceGUI, int seatCount)
    {
        this.raceGUI   = raceGUI;
        this.seatCount = seatCount;

        nameFields        = new JTextField[seatCount];
        styleDropdowns    = new JComboBox[seatCount];
        keyboardDropdowns = new JComboBox[seatCount];
        symbolFields      = new JTextField[seatCount];
        accessoryBoxes    = new JCheckBox[seatCount][3];

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("Configure Typists", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        for (int i = 0; i < seatCount; i++)
            tabs.addTab("Typist " + (i + 1), buildTab(i));
        add(tabs, BorderLayout.CENTER);

        add(buildButtons(), BorderLayout.SOUTH);
    }

    private JPanel buildTab(int i)
    {
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Name
        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameFields[i] = new JTextField("TYPIST_" + (i + 1), 15);
        nameRow.add(new JLabel("Name:"));
        nameRow.add(nameFields[i]);
        panel.add(nameRow);

        // Typing style
        JPanel styleRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        styleDropdowns[i] = new JComboBox<>(STYLE_NAMES);
        styleRow.add(new JLabel("Typing Style:"));
        styleRow.add(styleDropdowns[i]);
        panel.add(styleRow);

        // Keyboard
        JPanel keyboardRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        keyboardDropdowns[i] = new JComboBox<>(KEYBOARD_NAMES);
        keyboardRow.add(new JLabel("Keyboard:"));
        keyboardRow.add(keyboardDropdowns[i]);
        panel.add(keyboardRow);

        // Symbol
        JPanel symbolRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        symbolFields[i] = new JTextField(SYMBOLS[i], 3);
        symbolFields[i].setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        symbolRow.add(new JLabel("Symbol:"));
        symbolRow.add(symbolFields[i]);
        panel.add(symbolRow);

        // Accessories
        JPanel accPanel = new JPanel(new GridLayout(3, 1));
        accPanel.setBorder(BorderFactory.createTitledBorder("Accessories"));
        accessoryBoxes[i][0] = new JCheckBox("Wrist Support (less burnout)");
        accessoryBoxes[i][1] = new JCheckBox("Energy Drink (accuracy boost then drop)");
        accessoryBoxes[i][2] = new JCheckBox("Headphones (less mistyping)");
        for (JCheckBox box : accessoryBoxes[i]) accPanel.add(box);
        panel.add(accPanel);

        return panel;
    }

    private JPanel buildButtons()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton back = new JButton("← Back");
        back.addActionListener(e -> raceGUI.showConfig());

        JButton start = new JButton("Start Race →");
        start.setBackground(new Color(70, 130, 180));
        start.setForeground(Color.WHITE);
        start.setOpaque(true);
        start.setBorderPainted(false);
        start.addActionListener(e -> raceGUI.showRace(buildTypists()));

        panel.add(back);
        panel.add(start);
        return panel;
    }

    private Typist[] buildTypists()
    {
        Typist[] typists = new Typist[seatCount];
        for (int i = 0; i < seatCount; i++)
        {
            String name   = nameFields[i].getText().trim();
            String symbol = symbolFields[i].getText().trim();
            char   sym    = symbol.isEmpty() ? SYMBOLS[i].charAt(0) : symbol.charAt(0);

            double accuracy = 0.70;
            switch (styleDropdowns[i].getSelectedIndex()) {
                case 0: accuracy += 0.15; break; // Touch Typist
                case 1: accuracy -= 0.10; break; // Hunt & Peck
                case 2: accuracy -= 0.05; break; // Phone Thumbs
                case 3: accuracy -= 0.20; break; // Voice-to-Text
            }
            accuracy = Math.min(1.0, Math.max(0.0, accuracy));
            typists[i] = new Typist(sym, name.isEmpty() ? "TYPIST_" + (i+1) : name, accuracy);
        }
        return typists;
    }

    public boolean[] getWristSupport() {
        boolean[] result = new boolean[seatCount];
        for (int i = 0; i < seatCount; i++) {
            result[i] = accessoryBoxes[i][0].isSelected();
        }
        return result;
    }

    public boolean[] getEnergyDrink() {
        boolean[] result = new boolean[seatCount];
        for (int i = 0; i < seatCount; i++) {
            result[i] = accessoryBoxes[i][1].isSelected();
        }
        return result;
    }

    public boolean[] getHeadphones() {
        boolean[] result = new boolean[seatCount];
        for (int i = 0; i < seatCount; i++) {
            result[i] = accessoryBoxes[i][2].isSelected();
        }
        return result;
    }
}