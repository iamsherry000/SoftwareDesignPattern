/* 單張 (Single)：一張牌。
* 大小比較規則：先比數字再比花色。
* 數字由小到大依序為：3<4<5<...<10<J<Q<K<A<2
* 花色大小規則：♣ < ♦ < ♥ < ♠
*/
public class Single {
    private Card card;
    public Single(Card card) {
        this.card = card;
    }
    public Card getCard() {
        return card;
    }
    public boolean isGreaterThan(Single other) {
        return this.card.getRank().getValue() > other.card.getRank().getValue() ||
               (this.card.getRank().getValue() == other.card.getRank().getValue() &&
                this.card.getSuit().getValue() > other.card.getSuit().getValue());
    }
}