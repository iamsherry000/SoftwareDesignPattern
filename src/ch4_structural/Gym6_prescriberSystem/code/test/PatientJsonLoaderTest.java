package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Gender;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.PatientDatabase;
import ch4_structural.Gym6_prescriberSystem.code.io.PatientJsonLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PatientJsonLoaderTest {

    private static final String SAMPLE =
            "[{\"id\":\"A123456789\",\"name\":\"Alice\",\"gender\":\"female\","
            + "\"age\":30,\"height\":165,\"weight\":55},"
            + "{\"id\":\"C123456789\",\"name\":\"Carl\",\"gender\":\"male\","
            + "\"age\":45,\"height\":170,\"weight\":90}]";

    @Test
    public void testParsePatientsFromString() {
        List<Patient> patients = new PatientJsonLoader().parsePatients(SAMPLE);

        assertEquals(2, patients.size());

        Patient alice = patients.get(0);
        assertEquals("A123456789", alice.getId());
        assertEquals("Alice", alice.getName());
        assertEquals(Gender.FEMALE, alice.getGender());
        assertEquals(30, alice.getAge());
        assertEquals(165.0, alice.getHeight());
        assertEquals(55.0, alice.getWeight());

        Patient carl = patients.get(1);
        assertEquals(Gender.MALE, carl.getGender());
    }

    @Test
    public void testNewlyLoadedPatientHasNoCases() {
        List<Patient> patients = new PatientJsonLoader().parsePatients(SAMPLE);
        assertEquals(0, patients.get(0).getCases().size());
    }

    @Test
    public void testInvalidGenderRejected() {
        String json = "[{\"id\":\"A123456789\",\"name\":\"Alice\",\"gender\":\"other\","
                + "\"age\":30,\"height\":165,\"weight\":55}]";
        assertThrows(IllegalArgumentException.class,
                () -> new PatientJsonLoader().parsePatients(json));
    }

    @Test
    public void testRootMustBeArray() {
        assertThrows(IllegalArgumentException.class,
                () -> new PatientJsonLoader().parsePatients("{\"id\":\"A123456789\"}"));
    }

    @Test
    public void testLoadDatabaseFromTempFile() throws IOException {
        Path file = Files.createTempFile("patients", ".json");
        try {
            Files.writeString(file, SAMPLE, StandardCharsets.UTF_8);
            PatientDatabase db = new PatientJsonLoader().loadDatabase(file);

            Optional<Patient> found = db.findById("A123456789");
            assertTrue(found.isPresent());
            assertEquals("Alice", found.get().getName());
        } finally {
            Files.deleteIfExists(file);
        }
    }
}
