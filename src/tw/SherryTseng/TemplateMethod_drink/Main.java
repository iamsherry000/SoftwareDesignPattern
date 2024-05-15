package src.tw.SherryTseng.TemplateMethod_drink;

public class Main {
    public static void main(String[] args) {
        Drink tea = new Tea();
        Drink coffee = new Coffee();

        System.out.println("Making tea...");
        tea.prepareRecipe();

        System.out.println("\nMaking coffee...");
        coffee.prepareRecipe();
    }
}
