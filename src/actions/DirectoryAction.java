package actions;

import java.util.*;
import java.text.*;
import java.io.*;
import java.nio.file.*;

public class DirectoryAction implements Action {
    
    static String root = System.getProperty("user.home");
    static String currentDir = root;
    static File rootFile = new File(root);
    static BuildAction builder = new BuildAction();
    
    @Override
    public void execute() {
        String args = ask.next();
        if(args != null) {
            outcome(args);
        }
    } 
    
    @Override
    public void showGuide() {
        System.out.println("Directory CLI cmd");
        System.out.println("Get current directory: dir --cd");
        System.out.println("Change directory: dir -cd <dir name>");
        System.out.println("List files and folders in current directory: dir -ls");
        System.out.println("Return back to root directory: dir --back");
        System.out.println("Make folder at current directory: dir -mk <folder name>");
        System.out.println("Make new file at current directory: dir -live <filename> <file extension>");
        System.out.println("Argument: <file extension> carries only valid extensions like js,cpp,rust,yaml. Default is txt");
        System.out.println("Delete file at current directory: dir --die <filename>");
        System.out.println("Copy files from current directory to target directory(anywhere in storage): dir --copy <filename> <target dir>");
        System.out.println("Copy target files to target directory(might copy all instances of the file to target directory: dir ---dcopy <filename> <target dir>");
        System.out.println("Get current date and time cuz why not: dir -date");
        System.out.println("\n");
    }
    
    @Override
    public void outcome(String args) {
        switch(args) {
            case "-cd":
            String dir = ask.next();
            checkValidDir(dir);
            break;
            case "-ls":
            listDirItems();
            break;
            case "--back":
            currentDir = root;
            break;
            case "-mk":
            String foldername = ask.next();
            makeDir(foldername);
            break;
            case "--cd":
            System.out.println(currentDir);
            break;
            case "-live":
            String filename = ask.next();
            String fileType = ask.next();
            makeFile(filename,fileType);
            break;
            case "--die":
            String fname = ask.next();
            deleteFile(fname);
            break;
            case "--copy":
            String file = ask.next();
            String tgtDir = ask.next();
            copyFile(file,tgtDir);
            break;
            case "---dcopy":
            String fileName = ask.next();
            String targetDir = ask.next();
            deepCopyFile(fileName,targetDir);
            break;
            case "-date":
            showDate();
            break;
            default:
            System.out.println("Invalid Input");
        }
    }
    
    static void checkValidDir(String dir) {
        File check = new File(currentDir+"/"+dir);
        if(check.exists()) {
            currentDir = check.getAbsolutePath();
            System.out.println("Valid directory entered");
        } else {
            System.out.println("Invalid directory");
        }
    }
    
    static void listDirItems() {
        File base = new File(currentDir);
        File[] files = base.listFiles();
        if(files != null) {
            for(File f:files) {
                System.out.println(f.getName());
            }
        }
    }
    
    static void makeDir(String fname) {
        File folder = new File(currentDir+"/"+fname);
        if(!folder.exists()) {
            folder.mkdirs();
        }
    }
    
    static void makeFile(String fname,String type) {
        File file;
        if(type != null) {
            file = new File(currentDir+"/"+fname+"."+type);
        } else {
            file = new File(currentDir+"/"+fname+".txt");
        }
        try(FileWriter fw = new FileWriter(file)) {
            fw.write("");
        } catch(IOException e) {System.out.println(e.getMessage());}
    }
    
    static void deleteFile(String fname) {
        File file = new File(currentDir+"/"+fname);
        if(file.exists()) {
            if(file.delete()) {
                System.out.println(file.getName()+" deleted successfully");
            }
        } else {
            System.out.println(file.getName()+" not found!");
        }
    }
    
    static void copyFile(String fname,String targetDir) {
        File copy = new File(currentDir+"/"+fname);
        File target = builder.findTargetDir(rootFile,targetDir);
        if(copy.exists() && target.exists()) {
            try {
                Path src = Paths.get(copy.getAbsolutePath());
                Path tgt = Paths.get(target.getAbsolutePath(),copy.getName());
                Files.copy(src,tgt,StandardCopyOption.REPLACE_EXISTING);
                System.out.println(fname+" copied into "+targetDir+" successfully");
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("File or directory does not exist!");
        }
    }
    
    static void deepCopyFile(String fname,String targetDir) {
        File copy = builder.findTargetDir(rootFile,fname);
        File target = builder.findTargetDir(rootFile,targetDir);
        if(copy.exists() && target.exists()) {
            try {
                Path src = Paths.get(copy.getAbsolutePath());
                Path tgt = Paths.get(target.getAbsolutePath(),copy.getName());
                Files.copy(src,tgt,StandardCopyOption.REPLACE_EXISTING);
                System.out.println(fname+" copied into "+targetDir+" successfully");
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("File or directory does not exist!");
        }
    }
    
    static void showDate() {
        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE");
        
        String currentDateTime = sdf1.format(date) +" "+ sdf2.format(date);
        System.out.println(currentDateTime);
    }
    
}