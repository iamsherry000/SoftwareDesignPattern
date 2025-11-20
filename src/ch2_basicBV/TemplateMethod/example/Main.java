package TemplateMethod.example;

import TemplateMethod.example.common.Group;
import TemplateMethod.example.common.GroupingStrategy;
import TemplateMethod.example.common.Student;
import TemplateMethod.example.common.ReadStudents;
import static TemplateMethod.example.common.Utils.printGroups;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        GroupingStrategy groupingStrategy = new TimeSlotsBasedGroupingStrategy();

        List<Student> students = ReadStudents.fromFile("src/ch2/TemplateMethod/example/student.data");
        List<Group> groups = groupingStrategy.group(students);

        System.out.println("學生數量：" + students.size());
        printGroups(groups);
    }
}