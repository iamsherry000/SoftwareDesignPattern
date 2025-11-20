package TemplateMethod.example;

import TemplateMethod.example.common.Group;
import TemplateMethod.example.common.Student;

public class LanguageBasedGroupingStrategy extends CutBasedGroupingStrategy {
    protected LanguageBasedGroupingStrategy() {
        super(6);
    }

    @Override
    protected Object cutGroupBy(Student student) {
        return student.getLanguage();
    }

    @Override
    protected boolean meetMergeCriterial(Group nonFullGroup, Group fullGroup) {
        return nonFullGroup.get(0).getLanguage().equals(fullGroup.get(0).getLanguage());
    }

}
