package ch4_structural.Gym6_prescriberSystem.code.prescriber;

@FunctionalInterface
public interface Sleeper {
    void sleep();

    static Sleeper realThreeSeconds() {
        return () -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }
}
