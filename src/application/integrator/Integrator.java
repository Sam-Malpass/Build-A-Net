package application.integrator;

import application.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Integrator {

    public Integrator() {

    }

    public static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        Main.passMessage("Scanning folder");
        List<Class> classes = new ArrayList<Class>();
        if(!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for(File file : files) {
            if(file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                Main.passMessage(file.getName());
            }
        }
        return classes;
    }
}
