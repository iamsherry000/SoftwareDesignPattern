package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Gender;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Attractive;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Covid19;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.DiseaseHandler;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.DiagnosisRuleRegistry;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.SleepApneaSyndrome;
import ch4_structural.Gym6_prescriberSystem.code.io.DiseaseFileLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DiseaseFileLoaderTest {

    private DiagnosisRuleRegistry registryWithThreeRules() {
        DiagnosisRuleRegistry registry = new DiagnosisRuleRegistry();
        registry.register("COVID-19", Covid19::new);
        registry.register("Attractive", Attractive::new);
        registry.register("SleepApneaSyndrome", SleepApneaSyndrome::new);
        return registry;
    }

    @Test
    public void testParseDiseaseNamesSkipsBlanksAndTrims() {
        DiseaseFileLoader loader = new DiseaseFileLoader(registryWithThreeRules());
        String content = "COVID-19\n\n  Attractive  \nSleepApneaSyndrome\n";

        List<String> names = loader.parseDiseaseNames(content);
        assertEquals(List.of("COVID-19", "Attractive", "SleepApneaSyndrome"), names);
    }

    @Test
    public void testLoadChainBuildsChainOfCorrectLength() throws IOException {
        // 三行學名 → 組出 3-handler 鏈。驗證：每條規則的代表病患都能在這條鏈上命中。
        Path file = Files.createTempFile("diseases", ".txt");
        try {
            Files.writeString(file, "COVID-19\nAttractive\nSleepApneaSyndrome\n", StandardCharsets.UTF_8);
            DiseaseHandler head = new DiseaseFileLoader(registryWithThreeRules())
                    .loadChain(file).orElseThrow();

            // 鏈首 Covid
            Patient covidPatient = new Patient("A123456789", "Alice", Gender.FEMALE, 30, 165, 55);
            Optional<Prescription> r1 = head.handle(covidPatient,
                    List.of(Symptom.SNEEZE, Symptom.HEADACHE, Symptom.COUGH));
            assertEquals("清冠一號", r1.orElseThrow().getName());

            // 鏈中 Attractive
            Patient bella = new Patient("B123456789", "Bella", Gender.FEMALE, 18, 160, 50);
            Optional<Prescription> r2 = head.handle(bella, List.of(Symptom.SNEEZE));
            assertEquals("青春抑制劑", r2.orElseThrow().getName());

            // 鏈尾 SleepApnea（要走到第三個 handler 才命中 → 證明鏈長為 3）
            Patient carl = new Patient("C123456789", "Carl", Gender.MALE, 45, 170, 90);
            Optional<Prescription> r3 = head.handle(carl, List.of(Symptom.SNORE));
            assertEquals("打呼抑制劑", r3.orElseThrow().getName());
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    public void testLoadChainSingleDiseaseChainLengthOne() throws IOException {
        // 只有一行 → 鏈長 1。打呼病患在「只含 Covid」的鏈上不該命中。
        Path file = Files.createTempFile("diseases", ".txt");
        try {
            Files.writeString(file, "COVID-19\n", StandardCharsets.UTF_8);
            DiseaseHandler head = new DiseaseFileLoader(registryWithThreeRules())
                    .loadChain(file).orElseThrow();

            Patient carl = new Patient("C123456789", "Carl", Gender.MALE, 45, 170, 90);
            assertTrue(head.handle(carl, List.of(Symptom.SNORE)).isEmpty());
        } finally {
            Files.deleteIfExists(file);
        }
    }
}
