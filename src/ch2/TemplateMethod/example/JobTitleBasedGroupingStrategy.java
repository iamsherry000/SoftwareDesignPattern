package TemplateMethod.example;

import TemplateMethod.example.common.Group;
import TemplateMethod.example.common.Student;

public class JobTitleBasedGroupingStrategy extends CutBasedGroupingStrategy{
    protected JobTitleBasedGroupingStrategy(int groupMinSize) {
        super(groupMinSize);
    }

    @Override
    protected Object cutGroupBy(Student student) {
        return student.getJobTitle();
    }

    @Override
    protected boolean meetMergeCriterial(Group nonFullGroup, Group fullGroup) {
        return nonFullGroup.get(0).getJobTitle().equals(fullGroup.get(0).getJobTitle());
    }
}
