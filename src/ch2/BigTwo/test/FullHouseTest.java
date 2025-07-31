package BigTwo.test;

import BigTwo.pattern.CardPattern;
import BigTwo.pattern.fullHouse.FullHouseCardPattern;
import BigTwo.pattern.fullHouse.FullHouseHandler;
import org.junit.jupiter.api.Test;
import BigTwo.model.Card;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FullHouseTest {
    @Test
    public void testValidFullHousePattern() {
        List<Card> cards = Arrays.asList(
                new Card(0, 5), // ♣6
                new Card(1, 5), // ♦6
                new Card(2, 5), // ♥6
                new Card(0, 9), // ♣10
                new Card(1, 9)  // ♦10
        );
        FullHouseHandler handler = new FullHouseHandler();
        CardPattern pattern = handler.recognize(cards);

        assertNotNull(pattern);
        assertTrue(pattern instanceof FullHouseCardPattern);
    }

    @Test
    public void testInvalidFullHousePattern() {
        List<Card> cards = Arrays.asList(
                new Card(0, 5), // ♣6
                new Card(1, 5), // ♦6
                new Card(2, 7), // ♥8
                new Card(0, 9), // ♣10
                new Card(1, 9)  // ♦10
        );
        FullHouseHandler handler = new FullHouseHandler();
        CardPattern pattern = handler.recognize(cards);

        assertNull(pattern); // 沒有三張同點，無法構成 Full House
    }

    @Test
    public void testFullHouseCompareGreater() {
        CardPattern fh1 = new FullHouseCardPattern(Arrays.asList(
                new Card(0, 8), new Card(1, 8), new Card(2, 8), // 三張9
                new Card(0, 3), new Card(1, 3)                  // 兩張4
        ));
        CardPattern fh2 = new FullHouseCardPattern(Arrays.asList(
                new Card(0, 6), new Card(1, 6), new Card(2, 6), // 三張7
                new Card(0, 9), new Card(1, 9)                  // 兩張10
        ));

        assertTrue(fh1.isGreaterThan(fh2)); // 9 > 7
    }

    @Test
    public void testFullHouseCompareSmaller() {
        CardPattern fh1 = new FullHouseCardPattern(Arrays.asList(
                new Card(0, 4), new Card(1, 4), new Card(2, 4),
                new Card(0, 11), new Card(1, 11)
        ));
        CardPattern fh2 = new FullHouseCardPattern(Arrays.asList(
                new Card(0, 7), new Card(1, 7), new Card(2, 7),
                new Card(0, 9), new Card(1, 9)
        ));

        assertFalse(fh1.isGreaterThan(fh2)); // 5 < 8
    }
}
