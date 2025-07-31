package BigTwo.test;

import BigTwo.pattern.CardPattern;
import BigTwo.pattern.straight.StraightCardPattern;
import BigTwo.pattern.straight.StraightHandler;
import org.junit.jupiter.api.Test;
import BigTwo.model.Card;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class StraightTest {
    @Test
    public void testStraightCardPatternValid() {
        List<Card> cards = Arrays.asList(
                new Card(0, 3), // ♣4
                new Card(1, 4), // ♦5
                new Card(2, 5), // ♥6
                new Card(3, 6), // ♠7
                new Card(0, 7)  // ♣8
        );

        StraightHandler handler = new StraightHandler();
        CardPattern pattern = handler.recognize(cards);
        assertNotNull(pattern);
        assertTrue(pattern instanceof StraightCardPattern);
    }

    @Test
    public void testStraightCardPatternInvalid() {
        List<Card> cards = Arrays.asList(
                new Card(0, 3), // ♣4
                new Card(1, 3), // ♦4
                new Card(2, 4), // ♥5
                new Card(3, 6), // ♠7
                new Card(0, 8)  // ♣9
        );

        StraightHandler handler = new StraightHandler();
        CardPattern pattern = handler.recognize(cards);
        assertNull(pattern); // 非連續，無法成為順子
    }

    @Test
    public void testStraightCompareStrategyGreater() {
        CardPattern straightA = new StraightCardPattern(Arrays.asList(
                new Card(0, 4), new Card(1, 5), new Card(2, 6), new Card(3, 7), new Card(0, 8) // 5-9
        ));
        CardPattern straightB = new StraightCardPattern(Arrays.asList(
                new Card(1, 3), new Card(2, 4), new Card(3, 5), new Card(0, 6), new Card(1, 7) // 4-8
        ));

        assertTrue(straightA.isGreaterThan(straightB));
    }

    @Test
    public void testStraightCompareStrategySmaller() {
        CardPattern straightA = new StraightCardPattern(Arrays.asList(
                new Card(0, 2), new Card(1, 3), new Card(2, 4), new Card(3, 5), new Card(0, 6) // 3-7
        ));
        CardPattern straightB = new StraightCardPattern(Arrays.asList(
                new Card(1, 4), new Card(2, 5), new Card(3, 6), new Card(0, 7), new Card(1, 8) // 5-9
        ));

        assertFalse(straightA.isGreaterThan(straightB));
    }

    @Test
    public void testStraightCompareStrategyEqualSuitCompare() {
        CardPattern straightA = new StraightCardPattern(Arrays.asList(
                new Card(0, 4), new Card(1, 5), new Card(2, 6), new Card(3, 7), new Card(2, 8) // max ♥9
        ));
        CardPattern straightB = new StraightCardPattern(Arrays.asList(
                new Card(0, 4), new Card(1, 5), new Card(2, 6), new Card(3, 7), new Card(3, 8) // max ♠9
        ));

        assertTrue(straightB.isGreaterThan(straightA)); // ♠ > ♥
    }
}