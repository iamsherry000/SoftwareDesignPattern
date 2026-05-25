package ch4_structural.Gym6_prescriberSystem.code.prescriber;

import java.util.ArrayDeque;
import java.util.Deque;

public class Queue {
    private final Deque<PrescriptionDemand> demands = new ArrayDeque<>();

    public void enqueue(PrescriptionDemand demand) {
        if (demand == null) {
            throw new IllegalArgumentException("demand must not be null.");
        }
        demands.addLast(demand);
    }

    public PrescriptionDemand dequeue() {
        return demands.pollFirst();
    }

    public boolean isEmpty() {
        return demands.isEmpty();
    }
}
