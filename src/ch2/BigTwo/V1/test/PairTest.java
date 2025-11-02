package BigTwo.test;

import BigTwo.pattern.pair.PairCardPattern;
import BigTwo.pattern.pair.PairHandler;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import BigTwo.model.*;
import BigTwo.pattern.*;

import static org.junit.jupiter.api.Assertions.*;

public class PairTest {
    @Test
    public void testPairCardPatternValid() {
        Card card1 = new Card(0, 5); // ♣6
        Card card2 = new Card(1, 5); // ♦6
        List<Card> cards = Arrays.asList(card1, card2);

        PairHandler handler = new PairHandler();
        CardPattern pattern = handler.recognize(cards);
        assertNotNull(pattern);
        assertTrue(pattern instanceof PairCardPattern);
    }

    @Test
    public void testPairCardPatternInvalid() {
        Card card1 = new Card(0, 5); // ♣6
        Card card2 = new Card(1, 7); // ♦8
        List<Card> cards = Arrays.asList(card1, card2);

        PairHandler handler = new PairHandler();
        CardPattern pattern = handler.recognize(cards);
        assertNull(pattern); // 不同點數，無法成對
    }

    @Test
    public void testPairCompareStrategyGreater() {
        CardPattern pairA = new PairCardPattern(Arrays.asList(
                new Card(0, 7), new Card(1, 7))); // 對 8
        CardPattern pairB = new PairCardPattern(Arrays.asList(
                new Card(0, 5), new Card(2, 5))); // 對 6

        assertTrue(pairA.isGreaterThan(pairB));
    }

    @Test
    public void testPairCompareStrategySmaller() {
        CardPattern pairA = new PairCardPattern(Arrays.asList(
                new Card(0, 3), new Card(1, 3))); // 對 4
        CardPattern pairB = new PairCardPattern(Arrays.asList(
                new Card(2, 9), new Card(3, 9))); // 對 10

        assertFalse(pairA.isGreaterThan(pairB));
    }

    @Test
    public void testPairCompareStrategyEqualRankSuitCompare() {
        CardPattern pairA = new PairCardPattern(Arrays.asList(
                new Card(3, 6), new Card(2, 6))); // ♠7 ♥7
        CardPattern pairB = new PairCardPattern(Arrays.asList(
                new Card(1, 6), new Card(0, 6))); // ♦7 ♣7

        assertTrue(pairA.isGreaterThan(pairB)); // ♠ > ♦
    }
}
