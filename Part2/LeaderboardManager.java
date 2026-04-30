
import java.io.*;
import java.util.*;

public class LeaderboardManager {

    private static final String FILE = "leaderboard.txt";

    // F1-style points: 25, 18, 15, 12, 10, 8, 6, 4, 2, 1 for positions 1-10
    private static final int[] POSITION_POINTS = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};

    public static int calculatePoints(int position) {
        if (position <= 0 || position > POSITION_POINTS.length) {
            return 0;
        }
        return POSITION_POINTS[position - 1];
    }

    public static void saveRace(String[] names, int[] positions,
            double[] wpms, int[] burnouts) {
        // Find fastest WPM and most burnouts
        int fastestIndex = 0;
        int mostBurnouts = 0;
        int burnoutIndex = 0;
        for (int i = 0; i < wpms.length; i++) {
            if (wpms[i] > wpms[fastestIndex]) {
                fastestIndex = i;
            }
            if (burnouts[i] > mostBurnouts) {
                mostBurnouts = burnouts[i];
                burnoutIndex = i;
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE, true))) {
            for (int i = 0; i < names.length; i++) {
                int pts = calculatePoints(positions[i]);
                if (i == fastestIndex) {
                    pts += 1;  // fastest WPM bonus

                                }if (i == burnoutIndex && mostBurnouts > 0) {
                    pts -= 1; // most burnouts penalty

                                }writer.println(names[i] + "," + pts + "," + positions[i] + "," + burnouts[i]);
            }
            writer.println("---");
        } catch (IOException e) {
            System.out.println("Could not save leaderboard: " + e.getMessage());
        }
    }

    public static Map<String, Integer> loadTotals() {
        Map<String, Integer> totals = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("---")) {
                    continue;
                }
                String[] parts = line.split(",");
                totals.merge(parts[0], Integer.parseInt(parts[1]), Integer::sum);
            }
        } catch (IOException e) {
        }
        return totals;
    }

    public static Map<String, int[]> loadStreaks() {
        Map<String, int[]> streaks = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("---")) {
                    continue;
                }
                String[] parts = line.split(",");
                String name = parts[0];
                int pos = Integer.parseInt(parts[2]);
                int[] streak = streaks.getOrDefault(name, new int[]{0, 0});
                if (pos == 1) {
                    streak[0]++;
                    streak[1] = Math.max(streak[1], streak[0]);
                } else {
                    streak[0] = 0;
                }
                streaks.put(name, streak);
            }
        } catch (IOException e) {
        }
        return streaks;
    }

    public static Map<String, Integer> loadBurnoutFreeRaces() {
        Map<String, Integer> counts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("---")) {
                    continue;
                }
                String[] parts = line.split(",");
                String name = parts[0];
                int burnouts = Integer.parseInt(parts[3]);
                int current = counts.getOrDefault(name, 0);
                counts.put(name, burnouts == 0 ? current + 1 : 0);
            }
        } catch (IOException e) {
        }
        return counts;
    }

    public static String getTitle(String name) {
        Map<String, int[]> streaks = loadStreaks();
        Map<String, Integer> bfr = loadBurnoutFreeRaces();
        Map<String, Integer> totals = loadTotals();

        int bestStreak = streaks.getOrDefault(name, new int[]{0, 0})[1];
        int burnoutFree = bfr.getOrDefault(name, 0);
        int totalPoints = totals.getOrDefault(name, 0);

        if (bestStreak >= 3) {
            return "Speed Demon 🏆";
        }
        if (burnoutFree >= 5) {
            return "Iron Fingers 💪";
        }
        if (totalPoints >= 20) {
            return "Veteran ⭐";
        }
        return "Rookie";
    }
}
