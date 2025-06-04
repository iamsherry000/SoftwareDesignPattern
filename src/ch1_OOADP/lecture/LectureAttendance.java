package lecture;

import org.jetbrains.annotations.Nullable;

import static utils.ValidationUtils.shouldWithinRange;

public class LectureAttendance {
    private final Student student;
    private final Lecture lecture;

    @Nullable
    // private int grade;
    private Integer grade; // upgrade int -> Integer

    public LectureAttendance(Student student, Lecture lecture) {
        this.student = student;
        this.lecture = lecture;
    }

    public void receiveGrade(int grade) {
        /*
        this.grade = grade;
        if(grade < 0 || grade > 100) {
            throw new IllegalArgumentException("grade must be between 0 and 100.");
        }
        */
        this.grade = shouldWithinRange("Grade", grade, 0, 100);
        System.out.printf("%s received grade %d.\n", student.getName(), grade);
    }

    @Nullable
    public Integer getGrade() {
        return grade;
    }

    public Student getStudent() {
        return student;
    }

    public Lecture getLecture() {
        return lecture;
    }
}
