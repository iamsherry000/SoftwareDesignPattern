package TemplateMethod.example.common;

import java.util.List;

public class Utils {

    public static String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    public static String getFilePath(String filePath) {
        return filePath.substring(0, filePath.lastIndexOf("/"));
    }

    public static void printGroups(List<Group> groups) {
        System.out.print("printGroups");
        for (Group group : groups) {
            System.out.print("[");
            for (int i = 0; i < group.size(); i++) {
                if (i % 2 == 0) {
                    System.out.print(group.get(i));
                } else {
                    System.out.printf(", %s", group.get(i));
                    if (i != group.size() - 1) {
                        System.out.println();
                    }
                }
            }
            System.out.print("]\n\n");
        }
    }
}
