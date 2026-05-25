package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Attractive;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Covid19;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.SleepApneaSyndrome;
import ch4_structural.Gym6_prescriberSystem.code.export.ExportFormat;
import ch4_structural.Gym6_prescriberSystem.code.io.MiniJsonParser;
import ch4_structural.Gym6_prescriberSystem.code.prescriber.Sleeper;
import ch4_structural.Gym6_prescriberSystem.code.PrescriberSystem;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PrescriberSystemAdvancedTest {
    private final Sleeper noWait = () -> {};

    private static final String PATIENTS =
            "[{\"id\":\"A123456789\",\"name\":\"Alice\",\"gender\":\"female\",\"age\":30,\"height\":165,\"weight\":55},"
                    + "{\"id\":\"D123456789\",\"name\":\"Dan\",\"gender\":\"male\",\"age\":25,\"height\":175,\"weight\":65}]";

    private PrescriberSystem system() {
        PrescriberSystem system = new PrescriberSystem(noWait);
        system.registerRule("COVID-19", Covid19::new);
        system.registerRule("Attractive", Attractive::new);
        system.registerRule("SleepApneaSyndrome", SleepApneaSyndrome::new);
        system.provideData(PATIENTS, "COVID-19,Attractive,SleepApneaSyndrome");
        return system;
    }

    @Test
    public void testDiagnoseAndExportWritesJsonFile() throws IOException {
        PrescriberSystem system = system();
        Path out = Files.createTempFile("result", ".json");
        try {
            boolean exported = system.diagnoseAndExport("A123456789",
                    List.of(Symptom.SNEEZE, Symptom.HEADACHE, Symptom.COUGH), out, ExportFormat.JSON);

            assertTrue(exported);
            Map<?, ?> object = (Map<?, ?>) MiniJsonParser.parse(Files.readString(out, StandardCharsets.UTF_8));
            assertEquals("A123456789", object.get("patientId"));
            assertEquals("清冠一號", ((Map<?, ?>) object.get("prescription")).get("name"));
        } finally {
            Files.deleteIfExists(out);
        }
    }

    @Test
    public void testDiagnoseAndExportWritesCsvFile() throws IOException {
        PrescriberSystem system = system();
        Path out = Files.createTempFile("result", ".csv");
        try {
            boolean exported = system.diagnoseAndExport("A123456789",
                    List.of(Symptom.SNEEZE, Symptom.HEADACHE, Symptom.COUGH), out, ExportFormat.CSV);

            assertTrue(exported);
            String[] lines = Files.readString(out, StandardCharsets.UTF_8).split("\n");
            assertTrue(lines[0].startsWith("patientId,caseTime,symptoms"));
            assertTrue(lines[1].contains("\"清冠一號\""));
        } finally {
            Files.deleteIfExists(out);
        }
    }

    @Test
    public void testDiagnoseAndExportNoMatchDoesNotWriteFile() throws IOException {
        PrescriberSystem system = system();
        Path dir = Files.createTempDirectory("nomatch");
        Path out = dir.resolve("should-not-exist.json");
        try {
            boolean exported = system.diagnoseAndExport("D123456789",
                    List.of(Symptom.HEADACHE), out, ExportFormat.JSON);

            assertFalse(exported);
            assertFalse(Files.exists(out), "查無對應處方時不應寫出結果檔");
        } finally {
            Files.deleteIfExists(out);
            Files.deleteIfExists(dir);
        }
    }

    @Test
    public void testProvideDataBuildsChainFromCommaSeparatedDiseases() throws IOException {
        PrescriberSystem system = system();
        Path out = Files.createTempFile("result", ".json");
        try {
            boolean exported = system.diagnoseAndExport("A123456789",
                    List.of(Symptom.SNEEZE, Symptom.HEADACHE, Symptom.COUGH), out, ExportFormat.JSON);

            assertTrue(exported);
            Map<?, ?> object = (Map<?, ?>) MiniJsonParser.parse(Files.readString(out, StandardCharsets.UTF_8));
            assertEquals("清冠一號", ((Map<?, ?>) object.get("prescription")).get("name"));
        } finally {
            Files.deleteIfExists(out);
        }
    }
}
