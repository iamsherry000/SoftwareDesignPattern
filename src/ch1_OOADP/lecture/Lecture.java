package lecture;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Lecture {
    private final String name;
    private LectureAttendance lectureAttendance; // means which student signed up for this lecture

    public Lecture(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void signUp(@NotNull Student student) {
        if (student.getLectureAttendance() != null) {
            throw new IllegalStateException("Already has taken a lecture.");
        }

        lectureAttendance = new LectureAttendance(student, this);
        student.setLectureAttendance(lectureAttendance);
    }

    public void signOff(Student student) {
        if (lectureAttendance == null || lectureAttendance.getStudent() != student) {
            throw new IllegalStateException("Not signed up for this lecture.");
        }
        lectureAttendance = null;
        student.setLectureAttendance(null);
    }

    @Nullable
    public LectureAttendance getLectureAttendance() {
        return lectureAttendance;
    }
}
