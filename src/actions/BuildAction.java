package actions;

import java.util.*;
import java.io.*;
import java.nio.file.*;

public class BuildAction implements Action {
    
    private static File root = new File(System.getProperty("user.home"));
    private static final Map<String,String> gameDevPkgs = new HashMap<>();
    private static final Map<String,String> webDevPkgs = new HashMap<>();
    //dynamically generate base dir
    static String baseDir = findTargetDir(root,"build.process.lib").getAbsolutePath();
    
    public BuildAction() {
        loadGamePkgs();
        loadWebPkgs();
        System.out.println(baseDir);
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
        System.out.println("TRPLX Custom Build Process CLI cmds");
        System.out.println("");
        System.out.println("Setup Starting Web dev pkg: build trplx -w <working dir> init");
        System.out.println("Setup Starting Web Game Dev pkg: build trplx -g <working dir> init");
        System.out.println("Add custom TRPLX game dev library to project: build trplx -g <working dir> <package>");
        System.out.println("More packages integrated soon ;)");
        System.out.println("\n");
    }
    
    @Override
    public void outcome(String config) {
        if(config.equals("trplx")) {
            String extraConfig = ask.next();
            switch(extraConfig) {
                case "-g":
                String dir = ask.next();
                String pkg = ask.next();
                createGameBuild(dir,pkg);
                break;
                case "-w":
                String tgtdir = ask.next();
                String webpkg = ask.next();
                createWebBuild(tgtdir,webpkg);
                break;
                default:
                System.out.println("Invalid Input");
            }
        } else {
            System.out.println("Ya think ya swift XD");
        }
    }
    
    //all libs in correonding dirs for more dynamism
    static void loadGamePkgs() {
        gameDevPkgs.put("init",baseDir+"/game-dev/setup");
        gameDevPkgs.put("init-hc",baseDir+"/game-dev/serious-setup");
        gameDevPkgs.put("collider",baseDir+"/game-dev/utils/collide");
        gameDevPkgs.put("input",baseDir+"/game-dev/utils/input");
        gameDevPkgs.put("map-data",baseDir+"/game-dev/utils/sample-maps");
        gameDevPkgs.put("utils",baseDir+"/game-dev/utils");
    }
    
    static void loadWebPkgs() {
        webDevPkgs.put("init",baseDir+"/web-dev");
    }
    
    static void createGameBuild(String targetDir,String pkg) {
        File target = findTargetDir(root,targetDir);
        File source = new File(gameDevPkgs.get(pkg));
        
        if(target != null && source.exists()) {
            initBuild(source,target);
        } else {
            System.out.println("Package not found");
        }
    }
    
    static void createWebBuild(String targetDir,String pkg) {
        File target = findTargetDir(root,targetDir);
        File source = new File(webDevPkgs.get(pkg));
        
        if(target != null && source.exists()) {
            initBuild(source,target);
        } else {
            System.out.println("Package not found");
        }
    }
    
    static File findTargetDir(File root,String fname) {
        if(root.isDirectory()) {
            File[] files = root.listFiles();
            if(files != null) {
                for(File f:files) {
                    if(f.getName().equals(fname)) {
                        System.out.println("Target directory found at "+f.getAbsolutePath());
                        return f;
                    } else if(f.isDirectory()) {
                        File subtgt = findTargetDir(f,fname);
                        if(subtgt != null) {
                            return subtgt;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    static void initBuild(File source,File target) {
        File[] srcFiles = source.listFiles();
        for(File f:srcFiles) {
            if(f.isDirectory()) {
                File subf = new File(target,f.getName());
                if(!subf.exists()) {
                    subf.mkdirs();
                }
                initBuild(f,subf);
            } else {
                try {
                    Path srcD = Paths.get(f.getAbsolutePath());
                    Path tgtD = Paths.get(target.getAbsolutePath(),f.getName());
                    Files.copy(srcD,tgtD,StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Build process complete!");
                } catch(IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    
}