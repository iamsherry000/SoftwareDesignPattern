package lecture;

public class Test {
    public static void main(String[] args) {
        Student student1 = new Student("Sherry");
        Lecture lectureA = new Lecture("Software Design Pattern");

        lectureA.signUp(student1);

        try{
            lectureA.signUp(student1);
        } catch (Exception err) {
            System.out.println("Sherry has already signed up for this lecture.");
        }

        LectureAttendance attendance = lectureA.getLectureAttendance();
        assert attendance != null;
        attendance.receiveGrade(60); // give grades

        lectureA.signOff(student1);

        try{
            lectureA.signOff(student1);
        } catch (Exception err) {
            System.out.println("Sherry has not signed up for this lecture.");
        }

        Student student2 = new Student("WaterBall");
        //lectureA.signOff(student2); // should have exception
    }
}
