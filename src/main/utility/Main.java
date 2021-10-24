package utility;


import model.Priority;
import model.Project;
import model.Task;
import model.Todo;
import parsers.TaskParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Project project = new Project("Some description");
        Task t = new Task("IU ## important; urgent;");
        Task u = new Task("U ## tag; urgent;");
        Task v = new Task("D ## tag;");
        Task w = new Task("I ## tag; important;");
        Task x = new Task("IU2 ## tag; important; urgent;");
        Project p2 = new Project("P2");
        p2.setPriority(new Priority(2));
        p2.add(new Task("This is a task ## important;"));
        project.add(t);
        project.add(u);
        project.add(v);
        project.add(w);
        project.add(x);
        project.add(p2);
        for (Todo a: project) {
            System.out.println(a.getDescription());
        }

        Project p = new Project("Description");
        System.out.println(p.getProgress());


    }
}
