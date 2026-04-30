import java.io.*;
import java.util.*;

public class StatsManager
{
    private static final String FILE = "stats.txt";

    // Each entry: name, wpm, accuracy, burnouts, position
    public static void saveRace(String[] names, double[] wpms,
                                 double[] accuracies, int[] burnouts, int[] finishOrder)
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE, true)))
        {
            for (int i = 0; i < names.length; i++)
            {
                writer.println(names[i] + "," + wpms[i] + "," +
                               accuracies[i] + "," + burnouts[i] + "," +
                               (getRank(i, finishOrder) + 1));
            }
            writer.println("---"); // race separator
        }
        catch (IOException e) { System.out.println("Could not save stats: " + e.getMessage()); }
    }

    public static double getBestWPM(String name)
    {
        double best = 0;
        for (double[] entry : loadEntries(name))
            if (entry[0] > best) best = entry[0];
        return best;
    }

    public static List<double[]> getHistory(String name)
    {
        return loadEntries(name); // [wpm, accuracy, burnouts, position]
    }

    private static List<double[]> loadEntries(String name)
    {
        List<double[]> entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (line.equals("---")) continue;
                String[] parts = line.split(",");
                if (parts[0].equals(name))
                    entries.add(new double[]{
                        Double.parseDouble(parts[1]),
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[4])
                    });
            }
        }
        catch (IOException e) {}
        return entries;
    }

    private static int getRank(int index, int[] finishOrder)
    {
        for (int i = 0; i < finishOrder.length; i++)
            if (finishOrder[i] == index) return i;
        return finishOrder.length - 1;
    }
}