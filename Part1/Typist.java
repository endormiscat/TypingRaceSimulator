/**
 * Write a description of class Typist here.
 *
 * Starter code generously abandoned by Ty Posaurus, your predecessor,
 * who typed with two fingers and considered that "good enough".
 * He left a sticky note: "the slide-back thing is optional probably".
 * It is not optional. Good luck.
 *
 * @author Jakhongir Bakhromov
 * @version 18.04
 */
public class Typist
{
    // Fields of class Typist
    // Hint: you will need six fields. Think carefully about their types.
    // One of them tracks how far along the passage the typist has reached.
    // Another tracks whether the typist is currently burnt out.
    // A third tracks HOW MANY turns of burnout remain (not just whether they are burnt out).
    // The remaining three should be fairly obvious.

    private final String name;
    private char representation;
    private int progress;
    private boolean burntout;
    private int turnsBurntout;
    private double accuracy;
    private boolean justMistyped;


    // Constructor of class Typist
    /**
     * Constructor for objects of class Typist.
     * Creates a new typist with a given symbol, name, and accuracy rating.
     *
     * @param typistSymbol  a single Unicode character representing this typist (e.g. '①', '②', '③')
     * @param typistName    the name of the typist (e.g. "TURBOFINGERS")
     * @param typistAccuracy the typist's accuracy rating, between 0.0 and 1.0
     */
    public Typist(char typistSymbol, String typistName, double typistAccuracy)
    {
        this.name = typistName;
        this.representation = typistSymbol;
        this.accuracy = typistAccuracy;
    }


    // Methods of class Typist

    /**
     * Sets this typist into a burnout state for a given number of turns.
     * A burnt-out typist cannot type until their burnout has worn off.
     *
     * @param turns the number of turns the burnout will last
     */
    public void burnOut(int turns)
    {
        this.burntout = true;
        this.turnsBurntout = turns;
    }

    /**
     * Reduces the remaining burnout counter by one turn.
     * When the counter reaches zero, the typist recovers automatically.
     * Has no effect if the typist is not currently burnt out.
     */
    public void recoverFromBurnout()
    {
        if(this.burntout && this.turnsBurntout > 1){
            this.turnsBurntout = this.turnsBurntout - 1;
        }
        else if(this.burntout && this.turnsBurntout == 1){
            this.turnsBurntout = this.turnsBurntout - 1;
            this.burntout = false;
        }
    }

    /**
     * Returns the typist's accuracy rating.
     *
     * @return accuracy as a double between 0.0 and 1.0
     */
    public double getAccuracy()
    {
        return this.accuracy; // placeholder - replace with correct implementation
    }

    /**
     * Returns the typist's current progress through the passage.
     * Progress is measured in characters typed correctly so far.
     * Note: this value can decrease if the typist mistypes.
     *
     * @return progress as a non-negative integer
     */
    public int getProgress()
    {
        return this.progress; // placeholder - replace with correct implementation
    }

    /**
     * Returns the name of the typist.
     *
     * @return the typist's name as a String
     */
    public String getName()
    {
        return this.name; // placeholder - replace with correct implementation
    }

    /**
     * Returns the character symbol used to represent this typist.
     *
     * @return the typist's symbol as a char
     */
    public char getSymbol()
    {
        return this.representation; // placeholder - replace with correct implementation
    }

    /**
     * Returns the typist's current status on mistype
     * 
     * @return the typist's mistype status
     */
    public boolean getJustMistyped()
    {
        return this.justMistyped;
    }

    /**
     * Clears justMistyped
     */
    public void clearMistype()
    {
        this.justMistyped = false;
    }

    /**
     * Returns the number of turns of burnout remaining.
     * Returns 0 if the typist is not currently burnt out.
     *
     * @return burnout turns remaining as a non-negative integer
     */
    public int getBurnoutTurnsRemaining()
    {
        return this.turnsBurntout; // placeholder - replace with correct implementation
    }

    /**
     * Resets the typist to their initial state, ready for a new race.
     * Progress returns to zero, burnout is cleared entirely.
     */
    public void resetToStart()
    {
        this.burntout = false;
        this.progress = 0;
        this.turnsBurntout = 0;
        this.justMistyped = false;
    }

    /**
     * Returns true if this typist is currently burnt out, false otherwise.
     *
     * @return true if burnt out
     */
    public boolean isBurntOut()
    {
        return this.burntout; // placeholder - replace with correct implementation
    }

    /**
     * Advances the typist forward by one character along the passage.
     * Should only be called when the typist is not burnt out.
     */
    public void typeCharacter()
    {
        if(!this.burntout){
            this.progress = this.progress + 1;
        }
    }

    /**
     * Moves the typist backwards by a given number of characters (a mistype).
     * Progress cannot go below zero — the typist cannot slide off the start.
     *
     * @param amount the number of characters to slide back (must be positive)
     */
    public void slideBack(int amount)
    {
        this.progress = this.progress - amount;
        if(this.progress < 0){
            this.progress = 0;
        }
        this.justMistyped = true; //                                                                   added to implement the state of making a mistake
    }

    /**
     * Sets the accuracy rating of the typist.
     * Values below 0.0 should be set to 0.0; values above 1.0 should be set to 1.0.
     *
     * @param newAccuracy the new accuracy rating
     */
    public void setAccuracy(double newAccuracy)
    {
        if(newAccuracy < 0){
            this.accuracy = 0;
        }
        else if(newAccuracy > 1){
            this.accuracy = 1;
        }
        else{
            this.accuracy = newAccuracy;
        }
    }

    /**
     * Sets the symbol used to represent this typist.
     *
     * @param newSymbol the new symbol character
     */
    public void setSymbol(char newSymbol)
    {
        this.representation = newSymbol;
    }

}
