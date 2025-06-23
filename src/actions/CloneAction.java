package actions;

import java.util.*;
import java.io.*;
import java.nio.file.*;

public class CloneAction implements Action {
    
    private static String baseDir = "clones";
    private static File root = new File(System.getProperty("user.home"));
    private static int index = 0;
    private static List<Clone> clones = new ArrayList<>();
    
    public CloneAction() {
        update();
    }
    
    @Override
    public void execute() {
        String args = ask.next();
        if(args != null) {
            outcome(args);
        }
    }
    
    @Override
    public void showGuide() {
        System.out.println("Clone Files CLI cmd");
        System.out.println("Clone all occurences of target file: clone -f <filename>");
        System.out.println("Clear and delete existing clones: clone -clr --al");
        System.out.println("List existing clones: clones --list");
        System.out.println("Read clone data: clones --read <index>");
        System.out.println("\n");
    }
    
    @Override
    public void outcome(String args) {
        switch(args) {
            case "-f":
            String filename = ask.next();
            findClone(root,filename);
            break;
            case "-clr":
            String confirm = ask.next();
            if(confirm.equals("--all")) {
                clearClones();
            }
            break;
            case "--list":
            listClones();
            break;
            case "--read":
            int index = Integer.parseInt(ask.next());
            readClone(index);
            break;
            default:
            System.out.println("Invalid Input");
        }
    }
    
    static void findClone(File dir,String filename) {
        if(dir.isDirectory()) {
            File[] files = dir.listFiles();
            if(files != null) {
                for(File f:files) {
                    if(f.getName().equals(filename)) {
                        System.out.println("File found!");
                        index++;
                        cloneFile(f,new CloneAction.Clone(index,"txt",f.getName()).getFile());
                    } else if(f.isDirectory()) {
                        findClone(f,filename);
                    } else {
                        //System.out.println("File not found!");
                    }
                }
            }
        }
    }
    
    static void cloneFile(File sourceFile, File cloneFile) {
        try {
            Path srcD = Paths.get(sourceFile.getAbsolutePath());
            Path clone = Paths.get(cloneFile.getAbsolutePath());
            Files.copy(srcD,clone,StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    static void clearClones() {
        for(Clone c:clones) {
            c.deleteFile();
        }
        File base = new File(baseDir);
        File[] bases = base.listFiles();
        for(File b:bases) {
            if(b.delete()) {
                System.out.println(b.getName()+" deleted");
            }
        }
        clones.clear();
    }
    
    static void listClones() {
        File[] cloneFiles = new File(baseDir).listFiles();
        for(File c:cloneFiles) {
            System.out.println(c.getName());
        }
    }
    
    static void update() {
        File[] cloneFiles = new File(baseDir).listFiles();
        for(File c:cloneFiles) {
            Clone cc = new CloneAction.Clone(index,c);
            index++;
            clones.add(cc);
        }
    }
    
    static void readFile(File file) throws IOException {
        Scanner reader = new Scanner(file);
        while(reader.hasNext()) {
            String line = reader.nextLine();
            System.out.println(line);
        }
    }
    
    static void readClone(int index) {
        Clone clone = clones.get(index-1);
        try {
            readFile(clone.getFile());
            clone.readContents();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    static class Clone {
        private int index;
        private final Map<String,String> fileTypes = new HashMap<>();
        private String defaultFileType = ".txt";
        private File file;
        
        public Clone(int index) {
            initFileTypes();
            this.index = index;
            file = new File(baseDir+"/"+"clone"+"("+this.index+")"+this.defaultFileType);
            clones.add(this);
        }
        
        public Clone(int index,String type,String src) {
            initFileTypes();
            this.index = index;
            if(fileTypes.get(type) !=  null) {
                file = new File(baseDir+"/"+"clone"+"("+this.index+")-"+src+this.fileTypes.get(type));
            } else {
                file = new File(baseDir+"/"+"clone"+"("+this.index+")-"+src+this.defaultFileType);
            }
            clones.add(this);
        }
        
        public Clone(int index,File nfile) {
            this.index = index;
            file = new File(nfile.getName());
        }
        
        private void initFileTypes() {
            fileTypes.put("txt",".txt");
            fileTypes.put("js",".js");
            fileTypes.put("html",".html");
        }
        
        public File getFile() {
            return file;
        }
        
        public void deleteFile() {
            if(file.delete()) {
                System.out.println(this.getFile().getName()+" deleted");
            }
        }
        
        public void readContents() throws IOException {
            Scanner reader = new Scanner(file);
            while(reader.hasNext()) {
                String line = reader.nextLine();
                System.out.println(line);
            }
        }
        
    }
    
    
    
}