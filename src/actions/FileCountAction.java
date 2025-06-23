package actions;

import java.io.*;

public class FileCountAction implements Action {
    
    private static int count = 0;
    private static File root = new File(System.getProperty("user.home"));
    
    @Override
    public void execute() {
        count = 0;
        String filename = ask.next();
        fileCount(root,filename);
        System.out.println(count+" occurences of "+filename+" found");
    }
    
    @Override
    public void showGuide() {
        System.out.println("File/Directory Count CLI cmd");
        System.out.println("Command: ocof <your dir/file name>");
        System.out.println("Returns the total occurences of target file/dir");
        System.out.println("\n");
    }
    
    @Override
    public void outcome(String opt) {}
    
    private static void fileCount(File dir,String filename) {
        if(dir.isDirectory()) {
            File[] files = dir.listFiles();
            if(files != null) {
                for(File f:files) {
                    if(f.getName().equals(filename)) {
                        count++;
                        System.out.println(f.getName()+" found at "+f.getAbsolutePath());
                    } else if(f.isDirectory()) {
                        fileCount(f,filename);
                    }
                }
            }
        }
    }
    
}