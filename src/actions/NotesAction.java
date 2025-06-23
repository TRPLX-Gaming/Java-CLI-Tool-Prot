package actions;

import java.io.*;
import java.util.*;
import java.text.*;

public class NotesAction implements Action {
    
    static NotesAction.WritingToFile wtf = new NotesAction.WritingToFile();
    static boolean save = true;
    
    @Override
    public void execute() {
        String args = ask.next();
        if(args != null) {
            outcome(args);
        } else {
            System.out.println("Invalid Input!");
        }
    }
    
    @Override
    public void showGuide() {
        System.out.println("Notes CLI cmd");
        System.out.println("Write notes: notes -n <your text>");
        System.out.println("Command: notes -n"+"\n"+"Argument: <your text> can take any value");
        System.out.println("Read notes: notes -r");
        System.out.println("Command: notes -r"+"\n"+"Argumen:none");
        System.out.println("Read logging notes: notes --br");
        System.out.println("Command: notes --br"+"\n"+"Argument:none");
        System.out.println("Clear notes: notes -clr yes");
        System.out.println("Clear all notes data: notes -clr --all");
        System.out.println("Command::default => notes");
        System.out.println("\n");
    }
    
    @Override
    public void outcome(String prompt) {
        switch(prompt) {
            case "-n":
            String note = ask.nextLine();
            wtf.addNotes(note);
            break;
            case "-r":
            wtf.readNotes();
            break;
            case "--br":
            wtf.readBackupNotes();
            break;
            case "-clr":
            //System.out.println("Are you sure you want to delete all notes?");
            String confirm = ask.next().toLowerCase();
            
            if(confirm.equals("yes")) {
                wtf.refresh();
            } else if(confirm.equals("no")) {
                System.out.println("Oyanna XD");
            } else if(confirm.equals("--all")) {
                wtf.clearAll();
            } else {
                System.out.println("Invalid Input");
            }
            
            break;
            case "--save":
            save = true;
            System.out.println("Notes will be saved permanently");
            break;
            case "--!save":
            save = false;
            System.out.println("Notes will be deleted upon exit");
            break;
            default:
            System.out.println("Invalid input");
        }
    }
    
    static class WritingToFile {
        
        private static File notes = new File("notes/notes.txt");
        private static File backup = new File("notes/backup-notes.txt");
        public static File rootFile = new File(System.getProperty("user.home"));
        
        public WritingToFile() {
            
        }
        
        public void addNotes(String note) {
            try {
                FileWriter writer = new FileWriter(notes,true);
                FileWriter logger = new FileWriter(backup,true);
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String serialDate = sdf.format(date);
                
                writer.write(note+"\n");
                logger.write(note+" was added at "+serialDate+"\n");
                
                writer.close();
                logger.close();
                
                System.out.println("Note written successfully!");
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        
        public void refresh() {
            try(FileWriter clear = new FileWriter(notes)) {
                clear.write("");
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        
        public void clearAll() {
            refresh();
            try(FileWriter clear = new FileWriter(backup)) {
                clear.write("");
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        
        public void readNotes() {
            try {
                Scanner reader = new Scanner(notes);
                while(reader.hasNext()) {
                    String note = reader.nextLine();
                    System.out.println(note);
                }
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        
        public void readBackupNotes() {
            try {
                Scanner reader = new Scanner(backup);
                while(reader.hasNext()) {
                    String note = reader.nextLine();
                    System.out.println(note);
                }
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        
    }
    
}