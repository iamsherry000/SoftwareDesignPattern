package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Gender;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.PatientDatabase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PatientDatabaseTest {
    @Test
    public void testFindByIdHit() {
        Patient patient = new Patient("A123456789", "Alice", Gender.FEMALE, 30, 165, 55);
        PatientDatabase db = new PatientDatabase(List.of(patient));

        Optional<Patient> found = db.findById("A123456789");
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getName());
    }

    @Test
    public void testFindByIdMiss() {
        PatientDatabase db = new PatientDatabase(List.of(
                new Patient("A123456789", "Alice", Gender.FEMALE, 30, 165, 55)));

        Optional<Patient> found = db.findById("Z999999999");
        assertTrue(found.isEmpty());
    }
}
