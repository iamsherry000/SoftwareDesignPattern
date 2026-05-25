package ch4_structural.Gym6c_log_framework.exporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeExporter implements Exporter {

    private final List<Exporter> children;

    public CompositeExporter(Exporter... exporters) {
        this.children = new ArrayList<>(Arrays.asList(exporters));
    }

    public CompositeExporter(List<Exporter> exporters) {
        this.children = new ArrayList<>(exporters);
    }

    public List<Exporter> getChildren() {
        return List.copyOf(children);
    }

    @Override
    public void export(String formatted) {
        for (Exporter child : children) {
            child.export(formatted);
        }
    }
}
