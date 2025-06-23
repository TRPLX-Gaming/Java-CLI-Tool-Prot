package commands;

import java.util.*;
import actions.*;

public class InputHandler {
    static private final Map<String, Action> actions = new HashMap<>();
    
    public InputHandler() {
        loadActions();
    }
    
    public void handle(String args) {
        Action action = actions.get(args);
        if(action != null) {
            action.execute();
        } else if(args.equals("help")) {
            showGuides();
        } else {System.out.println("Fly");}
    }
    
    static void loadActions() {
        actions.put("dir",new DirectoryAction());
        actions.put("greet",new GreetAction());
        actions.put("notes",new NotesAction());
        actions.put("clone",new CloneAction());
        actions.put("build",new BuildAction());
        actions.put("ocof",new FileCountAction());
    }
    
    public void showGuides() {
        System.out.println("TRPLX terminal v1.beta commands and parameteers");
        for(Action act:actions.values()) {
            act.showGuide();
        }
    }
    
}