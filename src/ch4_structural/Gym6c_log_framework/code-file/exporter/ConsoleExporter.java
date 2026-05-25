package ch4_structural.Gym6c_log_framework.exporter;

public class ConsoleExporter implements Exporter {

    @Override
    public void export(String formatted) {
        System.out.println(formatted);
    }
}
