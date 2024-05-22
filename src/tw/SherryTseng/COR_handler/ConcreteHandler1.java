package src.tw.SherryTseng.COR_handler;

public class ConcreteHandler1 extends AbstractHandler {
    
    @Override
    public void handleRequest(int request) {
        if(request < 10) {
            System.out.println("ConcreteHandler1:" + request);
        }
        else if(nextHandler != null) {
            nextHandler.handleRequest(request);
        }
    }
}
