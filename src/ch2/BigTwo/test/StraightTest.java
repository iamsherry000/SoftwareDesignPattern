package BigTwo.test;

import org.junit.jupiter.api.Test;
import BigTwo.model.Card;
import java.util.List;
import java.util.Arrays;

public class StraightTest {
    @Test
    public void testStraightCardPatternValid() {
        // 3♦, 4♠, 5♣, 6♥, 7♦
        List<Card> straightCards = Arrays.asList(
                new Card(1, 3),  // Diamond 3
                new Card(3, 4),  // Spade 4
                new Card(0, 5),  // Club 5
                new Card(2, 6),  // Heart 6
                new Card(1, 7)   // Diamond 7
        );
    }
}
