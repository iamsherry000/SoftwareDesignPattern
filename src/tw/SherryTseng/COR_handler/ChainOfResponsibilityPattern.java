package src.tw.SherryTseng.COR_handler;

public class ChainOfResponsibilityPattern {
    public static void main(String[] args) {
        AbstractHandler h1 = new ConcreteHandler1();
        AbstractHandler h2 = new ConcreteHandler2();
        h1.setNextHanler(h2);

        int[] requests = {2, 5, 14, 22, 18, 3};

        for (int request : requests) {
            h1.handleRequest(request);
        }
    }
}
