package actions;

public class GreetAction implements Action {
    @Override
    public void execute() {
        String name = Action.ask.next();
        if(name != null) {
            System.out.println("Hi "+name);
        } else {
            System.out.println("Hello");
        }
    }
    
    @Override
    public void showGuide() {
        System.out.println("Greet CLI cmd");
        System.out.println("Example: greet <name>");
        System.out.println("Command: greet"+"\n"+"args:name -> can take any value");
        System.out.println("Returns feedback to the user");
        System.out.println("\n");
    }
    
    @Override
    public void outcome(String opt) {}
}