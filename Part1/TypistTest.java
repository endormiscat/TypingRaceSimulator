public class TypistTest
{
    public static void main(String[] args)
    {
        // 1. Normal forward movement
        Typist t = new Typist('①', "TURBOFINGERS", 0.85);
        t.typeCharacter();
        System.out.println("typeCharacter: " + (t.getProgress() == 1 ? "PASS" : "FAIL"));

        // 2. Progress cannot go below zero
        t.slideBack(99);
        System.out.println("slideBack clamp to 0: " + (t.getProgress() == 0 ? "PASS" : "FAIL"));

        // 3. Burnout counts down turn by turn and clears at zero
        t.burnOut(2);
        t.recoverFromBurnout();
        boolean stillBurntOut = t.isBurntOut();
        t.recoverFromBurnout();
        boolean clearedAtZero = !t.isBurntOut();
        System.out.println("Burnout countdown: " + (stillBurntOut && clearedAtZero ? "PASS" : "FAIL"));

        // 4. resetToStart clears progress and burnout
        t.typeCharacter();
        t.burnOut(3);
        t.resetToStart();
        System.out.println("resetToStart: " + (t.getProgress() == 0 && !t.isBurntOut() ? "PASS" : "FAIL"));

        // 5. Accuracy clamped below 0
        t.setAccuracy(-1.0);
        System.out.println("accuracy clamp below 0: " + (t.getAccuracy() == 0.0 ? "PASS" : "FAIL"));

        // 6. Accuracy clamped above 1
        t.setAccuracy(2.0);
        System.out.println("accuracy clamp above 1: " + (t.getAccuracy() == 1.0 ? "PASS" : "FAIL"));
    }
}