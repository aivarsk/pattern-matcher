package patternmatcher;

/**
 * This is simple pattern matcher usage example.
 * Contest implementations will be tested using
 * our real functional and performance test which 
 * is much more complicated.
 */
public class TestPatternMatcher {

    public static void main(String[] args) {

        IPatternMatcher patternMatcher = new PatternMatcher();

        // add patterns here (real test has 25K patterns)
        patternMatcher.addPattern("iPhone");
        patternMatcher.addPattern("youtube.com/watch");
        patternMatcher.addPattern("profil");
        patternMatcher.addPattern("Заходи, сюда");

        // Check texts against patterns here (real test has 1 million texts)
        String p1 = patternMatcher.checkText("What type of iPhone do you have?");
        String p2 = patternMatcher.checkText("Hi, take a look here: https://www.youtube.com/watch?v=RC_6skf1-t");
        String p3 = patternMatcher.checkText("Salut est-ce que tu peux aimer ma photo de profil?");
        String p4 = patternMatcher.checkText("Привет! У нас сегодня акция. Заходи, сюда узнаешь больше!");
        String p5 = patternMatcher.checkText("Yanıtını Beğen Hediye Yolluyor Dene Gör :)");

        // this line prints: "p1: iPhone p2: youtube.com/watch p3: profil p4: Заходи, сюда p5: null"
        System.out.printf("p1: %s p2: %s p3: %s p4: %s p5: %s\n", p1, p2, p3, p4, p5);
    }
}
