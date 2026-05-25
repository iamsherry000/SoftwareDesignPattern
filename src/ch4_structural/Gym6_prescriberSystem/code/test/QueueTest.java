package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.prescriber.PrescriptionDemand;
import ch4_structural.Gym6_prescriberSystem.code.prescriber.Queue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QueueTest {
    private PrescriptionDemand demand(String id) {
        return new PrescriptionDemand(id, List.of(Symptom.SNEEZE), result -> {});
    }

    @Test
    public void testEmptyOnCreation() {
        assertTrue(new Queue().isEmpty());
    }

    @Test
    public void testFifoOrder() {
        Queue queue = new Queue();
        queue.enqueue(demand("A123456789"));
        queue.enqueue(demand("B123456789"));
        queue.enqueue(demand("C123456789"));

        assertEquals("A123456789", queue.dequeue().getPatientId());
        assertEquals("B123456789", queue.dequeue().getPatientId());
        assertEquals("C123456789", queue.dequeue().getPatientId());
        assertTrue(queue.isEmpty());
    }
}
