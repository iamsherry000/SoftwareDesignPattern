package TemplateMethod.common;

import java.util.List;

public interface GroupingStrategy {
    List<Group> group(List<Student> students);
}
