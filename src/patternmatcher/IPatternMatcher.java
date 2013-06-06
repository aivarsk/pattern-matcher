package patternmatcher;

public interface IPatternMatcher {
    
    /** 
     * Adds a new pattern to the matcher. Does nothing if such pattern already exists 
     * @param pattern - any string of 3 to 300 symbols
     */
    void addPattern(String pattern);
   
    /** 
     * Checks if there is at least one pattern can be found in the text.
     * @param text - matched against patterns. Any string of 3 to 300 symbols.
     * @return first found pattern or null if there are no patterns found in the text 
     */
    String checkText(String text);
}
