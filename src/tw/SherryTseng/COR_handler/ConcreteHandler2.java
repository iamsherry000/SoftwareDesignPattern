package src.tw.SherryTseng.COR_handler;

public class ConcreteHandler2 extends AbstractHandler{
    
    @Override
    public void handleRequest(int request) {
        if (request >= 10 && request < 20) {
            System.out.println("ConcreteHandler2:" + request);
        }
        else if (nextHandler != null) {
            nextHandler.handleRequest(request);
        }
    }
}
