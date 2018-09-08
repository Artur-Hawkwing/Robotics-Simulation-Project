package mygame;

public class Runner 
{
    private static Main app;
    public static void main(String[] args)
    {
        app = new Main();
        app.runGame();
    }
    
    public static Main getMain()
    {
        return app;
    }
}