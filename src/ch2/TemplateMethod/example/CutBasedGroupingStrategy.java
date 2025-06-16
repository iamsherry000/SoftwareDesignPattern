package TemplateMethod.example;

import TemplateMethod.example.common.GroupingStrategy;
import TemplateMethod.example.common.Group;
import TemplateMethod.example.common.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CutBasedGroupingStrategy implements GroupingStrategy {
    public final int GROUP_MIN_SIZE;

    protected CutBasedGroupingStrategy(int groupMinSize) {
        GROUP_MIN_SIZE = groupMinSize;
    }

    public List<Group> group(List<Student> students) {
        // First cut with students attribute
        List<Group> firstCut = cutGroupByAttribute(students);

        // Second cut with minmum size of group number
        List<Group> secondCut = cutGroupByMinSize(firstCut);

        return balanceGroupSizes(secondCut);
    }

    protected List<Group> cutGroupByAttribute(List<Student> students) {
        Map<Object, Group> group = new HashMap<>();
        for (Student student : students) {
            Object key = cutGroupBy(student);
            if (!group.containsKey(key)) {
                group.put(key, new Group());
            }
            group.get(key).addStudent(student);
        }
        return new ArrayList<>(group.values());
    }

    protected abstract Object cutGroupBy(Student student);

    protected List<Group> cutGroupByMinSize(List<Group> cutGroups) {
        List<Group> groups = new ArrayList<>();
        for(Group group : cutGroups) {
            groups.addAll(group.splitBySize(GROUP_MIN_SIZE));
        }
        return groups;
    }

    protected List<Group> balanceGroupSizes(List<Group> groups) {
        List<Group> nonFulLGroups = new ArrayList<>();
        List<Group> fullGroups = new ArrayList<>();
        for (Group group : groups) {
            if (group.size() < GROUP_MIN_SIZE) {
                nonFulLGroups.add(group);
            } else {
                fullGroups.add(group);
            }
        }

        for (Group nonFullGroup : nonFulLGroups) {
            for (Group fullGroup : fullGroups) {
                // 符合併組的條件的話才併組
                if (meetMergeCriterial(nonFullGroup, fullGroup)) {
                    System.out.printf("Merge group (%d) to (%d).\n", nonFullGroup.getNumber(), fullGroup.getNumber());
                    fullGroup.merge(nonFullGroup);
                    break;
                }
            }
        }
        return fullGroups;
    }

    protected boolean meetMergeCriterial(Group nonFullGroup, Group fullGroup) {
        return true;
    }
}
