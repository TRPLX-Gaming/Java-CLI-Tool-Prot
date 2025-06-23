package actions;

import java.util.*;
import main.*;

public interface Action {
    void execute();
    void outcome(String args);
    void showGuide();
    static Scanner ask = Main.ask;
}