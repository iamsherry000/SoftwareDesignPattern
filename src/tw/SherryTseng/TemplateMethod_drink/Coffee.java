package src.tw.SherryTseng.TemplateMethod_drink;

public class Coffee extends Drink{
    public void brew() {
        System.out.println("Dripping Coffee through filter");
    }

    public void addCondiments() {
        System.out.println("Adding Sugar and Milk");
    }
}
