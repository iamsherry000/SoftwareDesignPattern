package lecture;

import org.jetbrains.annotations.Nullable;

public class Student {
    private final String name;

    @Nullable
    private LectureAttendance lectureAttendance; // means which lecture this student signed up for

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public LectureAttendance getLectureAttendance() {
        return lectureAttendance;
    }

    public void setLectureAttendance(@Nullable LectureAttendance lectureAttendance) {
        this.lectureAttendance = lectureAttendance;
    }
}