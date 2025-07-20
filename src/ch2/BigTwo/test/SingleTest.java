package BigTwo.test;

import BigTwo.model.Card;
import BigTwo.pattern.CardPattern;
import BigTwo.pattern.SingleCardPattern;
import BigTwo.pattern.SingleCompareStrategy;
import BigTwo.pattern.SingleHandler;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SingleTest {
    // ---------- Test: SingleCardPattern ----------
    @Test
    void testGetCards() {
        Card c = new Card(0, 2); // ♣3
        SingleCardPattern pattern = new SingleCardPattern(Arrays.asList(c));
        List<Card> cards = pattern.getCards();
        assertEquals(1, cards.size());
        assertEquals(c, cards.get(0));
    }

    @Test
    void testIsGreaterThanTrue() {
        Card c1 = new Card(2, 10); // ♥J
        Card c2 = new Card(0, 5);  // ♣6
        SingleCardPattern p1 = new SingleCardPattern(Arrays.asList(c1));
        SingleCardPattern p2 = new SingleCardPattern(Arrays.asList(c2));
        assertTrue(p1.isGreaterThan(p2));
    }

    @Test
    void testIsGreaterThanFalse() {
        Card c1 = new Card(1, 3); // ♦4
        Card c2 = new Card(3, 12); // ♠K
        SingleCardPattern p1 = new SingleCardPattern(Arrays.asList(c1));
        SingleCardPattern p2 = new SingleCardPattern(Arrays.asList(c2));
        assertFalse(p1.isGreaterThan(p2));
    }

    // ---------- Test: SingleCompareStrategy ----------
    @Test
    void testCompareHigher() {
        var c1 = new Card(2, 10); // ♥J
        var c2 = new Card(1, 9);  // ♦10
        var p1 = new SingleCardPattern(Arrays.asList(c1));
        var p2 = new SingleCardPattern(Arrays.asList(c2));
        var strategy = new SingleCompareStrategy();
        assertTrue(strategy.isGreater(p1, p2));
    }

    @Test
    void testCompareSameRankSuitHigher() {
        var c1 = new Card(3, 9);  // ♠10
        var c2 = new Card(0, 9);  // ♣10
        var p1 = new SingleCardPattern(Arrays.asList(c1));
        var p2 = new SingleCardPattern(Arrays.asList(c2));
        var strategy = new SingleCompareStrategy();
        assertTrue(strategy.isGreater(p1, p2));
    }

    @Test
    void testCompareLower() {
        var c1 = new Card(1, 3);  // ♦4
        var c2 = new Card(2, 5);  // ♥6
        var p1 = new SingleCardPattern(Arrays.asList(c1));
        var p2 = new SingleCardPattern(Arrays.asList(c2));
        var strategy = new SingleCompareStrategy();
        assertFalse(strategy.isGreater(p1, p2));
    }

    // ---------- Test: SingleHandler ----------
    @Test
    void testRecognizeValidSingle() {
        List<Card> cards = Arrays.asList(new Card(0, 2)); // ♣3
        SingleHandler handler = new SingleHandler();
        CardPattern pattern = handler.recognize(cards);
        assertNotNull(pattern);
        assertTrue(pattern instanceof SingleCardPattern);
        assertEquals(1, pattern.getCards().size());
    }

    @Test
    void testRecognizeInvalidMultipleCards() {
        List<Card> cards = Arrays.asList(new Card(0, 2), new Card(1, 2)); // ♣3, ♦3
        SingleHandler handler = new SingleHandler();
        CardPattern pattern = handler.recognize(cards);
        assertNull(pattern); // 無法辨識為單張
    }

    @Test
    void testChainToNullHandler() {
        List<Card> cards = Arrays.asList(new Card(0, 2), new Card(1, 2));
        SingleHandler handler = new SingleHandler(); // 未接下一個 handler
        assertNull(handler.recognize(cards));
    }
}
