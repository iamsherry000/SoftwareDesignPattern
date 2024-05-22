package src.tw.SherryTseng.COR_handler;

public abstract class AbstractHandler {
    protected AbstractHandler nextHandler;

    public void setNextHanler(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handleRequest(int request);
}
