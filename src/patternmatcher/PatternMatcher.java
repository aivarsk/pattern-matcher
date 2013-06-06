package patternmatcher;
import java.lang.reflect.Field;

public class PatternMatcher implements IPatternMatcher {
    // Compact version of trie http://en.wikipedia.org/wiki/Trie
    class Trie {
        char min;
        char max;
        boolean leaf;
        Trie[] childs;
    }

    private Trie trie_;

    // Fields for accessing private members of java.lang.String instead of
    // calling toCharArray() that does array copy (AFAIK)
    private Field valueField_;
    private Field offsetField_;

    public PatternMatcher() {
        // The first level with all edges to skip initial min/max comparison
        trie_ = new Trie();
        trie_.min = 0;
        trie_.max = Character.MAX_VALUE;
        trie_.childs = new Trie[Character.MAX_VALUE + 1];

        try {
            valueField_ = String.class.getDeclaredField("value");
            offsetField_ = String.class.getDeclaredField("offset");
            valueField_.setAccessible(true);
            offsetField_.setAccessible(true);
        } catch (NoSuchFieldException e) {
            valueField_ = null;
            offsetField_ = null;
        }
    }

    @Override
    public void addPattern(String pattern) {
        Trie trie = trie_;
        Trie next = trie_;
        
        for (char c : pattern.toCharArray()) {
            if (trie.childs == null) {
                trie.childs = new Trie[1];
                trie.min = trie.max = c;

            // Reallocate edges as needed, lazy and stupid
            // performance of addPattern is not important
            } else if (c < trie.min || c > trie.max) {
                if (c < trie.min) {
                    Trie childs[] = new Trie[trie.max - c + 1];
                    for (int i = 0; i < trie.childs.length; i++) {
                        childs[trie.min - c + i] = trie.childs[i];
                    }
                    trie.childs = childs;
                    trie.min = c;
                } else if (c > trie.max) {
                    Trie childs[] = new Trie[c - trie.min + 1];
                    for (int i = 0; i < trie.childs.length; i++) {
                        childs[i] = trie.childs[i];
                    }
                    trie.childs = childs;
                    trie.max = c;
                }
            }

            next = trie.childs[c - trie.min];
            if (next == null) {
                next = trie.childs[c - trie.min] = new Trie();
            }
            trie = next;

        }
        next.leaf = true;
    }

    @Override
    public String checkText(String text) {
        char[] chars;
        int offset; 
        int length = text.length();
        
        // Avoid array copy, improves performance on larger strings
        // and fallback to the usual implementation
        try {
            chars = (char [])valueField_.get(text);
            offset = offsetField_.getInt(text);
        } catch (IllegalAccessException e) {
            chars = text.toCharArray();
            offset = 0;
        } catch (NullPointerException e) {
            chars = text.toCharArray();
            offset = 0;
        }
 
        Trie trie = trie_;
        for (int i = 0; i < length - 2; i++) {

            Trie t = trie.childs[chars[offset + i]];
            if (t != null) {
                for (int j = i + 1; j < length; j++) {
                    char c = chars[offset + j];
                    if (c < t.min || c > t.max) {
                        break;
                    }
                    Trie next = t.childs[c - t.min];
                    if (next == null) {
                        break;
                    }
                    if (next.leaf) {
                        return text.substring(i, j + 1);
                    } 
                    if (next.childs == null) {
                        break;
                    }

                    t = next;
                }
            }
        }
        return null;
    }
}
