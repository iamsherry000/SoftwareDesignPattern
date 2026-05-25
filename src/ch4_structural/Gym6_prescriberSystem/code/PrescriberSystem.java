package ch4_structural.Gym6_prescriberSystem.code;

import ch4_structural.Gym6_prescriberSystem.code.core.Case;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.PatientDatabase;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.DiagnosisRuleRegistry;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.DiseaseHandler;
import ch4_structural.Gym6_prescriberSystem.code.export.DiagnosisRecord;
import ch4_structural.Gym6_prescriberSystem.code.export.ExportFormat;
import ch4_structural.Gym6_prescriberSystem.code.export.SaveStrategy;
import ch4_structural.Gym6_prescriberSystem.code.io.DiseaseFileLoader;
import ch4_structural.Gym6_prescriberSystem.code.io.PatientJsonLoader;
import ch4_structural.Gym6_prescriberSystem.code.prescriber.Prescriber;
import ch4_structural.Gym6_prescriberSystem.code.prescriber.PrescriptionDemand;
import ch4_structural.Gym6_prescriberSystem.code.prescriber.Queue;
import ch4_structural.Gym6_prescriberSystem.code.prescriber.Sleeper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PrescriberSystem {
    private final DiagnosisRuleRegistry registry = new DiagnosisRuleRegistry();
    private final Sleeper sleeper;
    private PatientDatabase patientDB;
    private Prescriber prescriber;

    public PrescriberSystem(Sleeper sleeper) {
        if (sleeper == null) {
            throw new IllegalArgumentException("sleeper must not be null.");
        }
        this.sleeper = sleeper;
        // 核心不內建任何規則：client 透過 registerRule 註冊「支援哪些病」，
        // provideData 讀疾病清單時才依 registry 組鏈。核心模組對具體 handler 零依賴。
    }

    public void registerRule(String diseaseName, Supplier<DiseaseHandler> factory) {
        registry.register(diseaseName, factory);
    }

    public void provideData(String patientsJson, String diseases) {
        patientDB = new PatientDatabase(new PatientJsonLoader().parsePatients(patientsJson));
        List<String> diseaseNames = new DiseaseFileLoader(registry).parseDiseaseNames(diseases);
        DiseaseHandler chainHead = registry.buildChain(diseaseNames)
                .orElseThrow(() -> new IllegalArgumentException("Disease list produced an empty diagnosis chain."));
        prescriber = new Prescriber(chainHead, patientDB, new Queue(), sleeper);
    }

    public void requestDiagnosis(String patientId, List<Symptom> symptoms,
                                 Consumer<Optional<Prescription>> onComplete) {
        requireReady();
        prescriber.submit(new PrescriptionDemand(patientId, symptoms, onComplete));
    }

    public void processAll() {
        requireReady();
        prescriber.processAll();
    }

    public boolean diagnoseAndExport(String patientId, List<Symptom> symptoms,
                                     Path outFile, ExportFormat format) throws IOException {
        if (outFile == null) {
            throw new IllegalArgumentException("outFile must not be null.");
        }
        if (format == null) {
            throw new IllegalArgumentException("format must not be null.");
        }
        var captured = new AtomicReference<Optional<Prescription>>(Optional.empty());
        requestDiagnosis(patientId, symptoms, captured::set);
        processAll();

        Optional<Prescription> result = captured.get();
        if (result.isEmpty()) {
            return false;
        }
        LocalDateTime caseTime = LocalDateTime.now();
        Prescription prescription = result.get();
        saveCase(patientId, symptoms, prescription, caseTime);

        DiagnosisRecord record = new DiagnosisRecord(patientId, symptoms, prescription, caseTime);
        SaveStrategy exporter = format.createExporter();
        Files.writeString(outFile, exporter.export(record), StandardCharsets.UTF_8);
        return true;
    }

    public Optional<Case> saveCase(String patientId, List<Symptom> symptoms,
                                   Prescription prescription, LocalDateTime caseTime) {
        if (prescription == null) {
            return Optional.empty();
        }
        Optional<Patient> patient = patientDB.findById(patientId);
        if (patient.isEmpty()) {
            return Optional.empty();
        }
        Case aCase = new Case(symptoms, prescription, caseTime);
        patient.get().addCase(aCase);
        return Optional.of(aCase);
    }

    private void requireReady() {
        if (prescriber == null) {
            throw new IllegalStateException("provideData must be called before diagnosing.");
        }
    }
}
