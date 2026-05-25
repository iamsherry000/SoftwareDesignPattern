package ch4_structural.Gym6_prescriberSystem.code;

import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Attractive;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Covid19;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.SleepApneaSyndrome;
import ch4_structural.Gym6_prescriberSystem.code.export.ExportFormat;
import ch4_structural.Gym6_prescriberSystem.code.prescriber.Sleeper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Path resource = Path.of("src", "ch4_structural", "Gym6_prescriberSystem", "code", "resource");
        Path resultDir = resource.getParent().resolve("result");
        Files.createDirectories(resultDir);

        PrescriberSystem system = new PrescriberSystem(Sleeper.realThreeSeconds());
        // client 註冊支援的規則（核心不內建）；provideData 讀 SupportDiseases.in 時才組鏈
        system.registerRule("COVID-19", Covid19::new);
        system.registerRule("Attractive", Attractive::new);
        system.registerRule("SleepApneaSyndrome", SleepApneaSyndrome::new);
        system.provideData(
                Files.readString(resource.resolve("AllPatients.json"), StandardCharsets.UTF_8),
                Files.readString(resource.resolve("SupportDiseases.in"), StandardCharsets.UTF_8));

        List<String> lines = Files.readAllLines(resource.resolve("PrescribePatient.in"), StandardCharsets.UTF_8)
                .stream().map(String::trim).filter(line -> !line.isEmpty()).toList();

        for (int i = 0; i + 3 <= lines.size(); i += 3) {
            String patientId = lines.get(i);
            List<Symptom> symptoms = Symptom.parse(lines.get(i + 1));
            String exportName = Path.of(lines.get(i + 2)).getFileName().toString();

            Path out = resultDir.resolve(exportName);
            boolean exported = system.diagnoseAndExport(patientId, symptoms, out, ExportFormat.fromFileName(exportName));
            System.out.println(patientId + (exported ? " -> " + out : " -> no matching prescription"));
        }
    }
}
