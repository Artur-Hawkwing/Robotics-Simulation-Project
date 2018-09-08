package mygame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

enum View {GAMEVIEW, MENU, LOADING};
public class GUI implements ImageButtonListener
{
    //----------Standard----------//
    private JFrame frame;
    private Canvas gameScreen;
    private Main APP;
    private JPanel container, menu;
    
    //Menu
    private Image mainMenu;
    private ImageButton buffaloWingsRedImage, buffaloWingsBlueImage, roboPuffsRedImage, roboPuffsBlueImage;
    private ImageButton singlePractice, singleTimed, multiPractice, multiCompetition;
    private ImageLabel redTeam, blueTeam;
    
    //JButtons
    private final int SPACING;
    
    //Dimensions
    private int widthNum;
    private int heightNum;
    private Dimension screenSize;
    
    //Buttons
    public final int singlePlayerPractice = 0;
    public final int multiPlayerPractice = 1;
    public final int singlePlayerTimed = 2;
    public final int multiPlayerCompetition = 3;
    public final int buffaloWingsTeam = 4;
    public final int roboPuffsTeam = 5;
    public final int buffaloWingsTeamRed = 6;
    public final int roboPuffsTeamRed = 7;
    public final int buffaloWingsTeamBlue = 8;
    public final int roboPuffsTeamBlue = 9;
    private int mode, robot0 = -1, robot1 = -1;
    
    public GUI(Dimension screenSize, Canvas canvas, Main APP)
    {
        //Initialize instance data
        this.screenSize = screenSize;
        this.widthNum = screenSize.width;
        this.heightNum = screenSize.height;
        gameScreen = canvas;
        this.APP = APP;
        
        SPACING = heightNum / 54;
        
        //Initialize Menus with action compenents
        initFrame();
        initMenu();
        initContainer();
    }
    
    public JFrame getFrame()
    {        
        frame.add(menu);
        return frame;
    }
    
    public Canvas getCanvas()
    {
        return gameScreen;
    }
    
    private void initFrame()
    {
        frame = new JFrame("Velocity Vortex");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLUE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
    }
    
    private void initMenu()
    {
        int x = 500;
        int y = 100;
        
        try
        {
            singlePractice = new ImageButton(ImageIO.read(getClass().getResource("/Interface/singlePlayerPractice.png")), singlePlayerPractice, this, x, y);
            singleTimed = new ImageButton(ImageIO.read(getClass().getResource("/Interface/singlePlayerTimed.png")), singlePlayerTimed, this, x, y);
            multiPractice = new ImageButton(ImageIO.read(getClass().getResource("/Interface/multiPlayerCompetition.png")), multiPlayerCompetition, this, x, y);
            multiCompetition = new ImageButton(ImageIO.read(getClass().getResource("/Interface/multiPlayerPractice.png")), multiPlayerPractice, this, x, y);
            mainMenu = ImageIO.read(getClass().getResource("/Interface/Main.jpg"));
        }
        catch(IOException e){}

        menu = new ImagePanel(mainMenu, screenSize);
        menu.setLayout(null);
        
        singlePractice.setFocusable(false);
        singleTimed.setFocusable(false);
        multiPractice.setFocusable(false);
        multiCompetition.setFocusable(false);
        
        singlePractice.setBounds(SPACING, 8 * SPACING, x, y);
        singleTimed.setBounds(SPACING, 16 * SPACING, x, y);
        multiPractice.setBounds(SPACING, 24 * SPACING, x, y);
        multiCompetition.setBounds(SPACING, 32 * SPACING, x, y);
        
        menu.add(singlePractice);
        menu.add(singleTimed);
        menu.add(multiPractice);
        menu.add(multiCompetition);
    }
    
    private void initContainer()
    {
        container = new JPanel();
        container.setLayout(null);
        container.setBackground(Color.YELLOW);
        initLayout();
    }
    
    private void initLayout()
    {
        int screenWidth = (int) (widthNum / 1.15f);
        int screenHeight = (int) (heightNum / 1.25);
        gameScreen.setBounds(0, 0, widthNum, heightNum);
        gameScreen.setBackground(Color.BLACK);
        container.add(gameScreen);
    }
    
    private void updateMenu(int menuScreen)
    {
        int x = 500;
        int y = 100;
        menu.removeAll();
        if(menuScreen == singlePlayerPractice || menuScreen == singlePlayerTimed)
        {
            try
            {
                buffaloWingsBlueImage = new ImageButton(ImageIO.read(getClass().getResource("/Interface/buffaloWingsBlue.png")), buffaloWingsTeam, this, x, y);
                roboPuffsRedImage = new ImageButton(ImageIO.read(getClass().getResource("/Interface/roboPuffsRed.png")), roboPuffsTeam, this, x, y);
            }
            catch(IOException e){}

            buffaloWingsBlueImage.setFocusable(false);
            roboPuffsRedImage.setFocusable(false);

            buffaloWingsBlueImage.setBounds(SPACING, 8 * SPACING, x, y);
            roboPuffsRedImage.setBounds(SPACING, 16 * SPACING, x, y);

            menu.add(buffaloWingsBlueImage);
            menu.add(roboPuffsRedImage);
        }
        else
        {
            try
            {
                buffaloWingsBlueImage = new ImageButton(ImageIO.read(getClass().getResource("/Interface/buffaloWingsBlue.png")), buffaloWingsTeamBlue, this, x, y);
                roboPuffsBlueImage = new ImageButton(ImageIO.read(getClass().getResource("/Interface/roboPuffsBlue.png")), roboPuffsTeamBlue, this, x, y);
                buffaloWingsRedImage = new ImageButton(ImageIO.read(getClass().getResource("/Interface/buffaloWingsRed.png")), buffaloWingsTeamRed, this, x, y);
                roboPuffsRedImage = new ImageButton(ImageIO.read(getClass().getResource("/Interface/roboPuffsRed.png")), roboPuffsTeamRed, this, x, y);
                
                redTeam = new ImageLabel(ImageIO.read(getClass().getResource("/Interface/RedTeam.png")), x, y);
                blueTeam = new ImageLabel(ImageIO.read(getClass().getResource("/Interface/BlueTeam.png")), x, y);
            }
            catch(IOException e){}
             
            buffaloWingsBlueImage.setFocusable(false);
            roboPuffsBlueImage.setFocusable(false);
            buffaloWingsRedImage.setFocusable(false);
            roboPuffsRedImage.setFocusable(false);
            redTeam.setFocusable(false);
            blueTeam.setFocusable(false);
            
            blueTeam.setBounds(70 * SPACING, 8 * SPACING, x, y);
            buffaloWingsBlueImage.setBounds(70 * SPACING, 16 * SPACING, x, y);
            roboPuffsBlueImage.setBounds(70 * SPACING, 24 * SPACING, x, y);
            redTeam.setBounds(SPACING, 8 * SPACING, x, y);
            buffaloWingsRedImage.setBounds(SPACING, 16 * SPACING, x, y);
            roboPuffsRedImage.setBounds(SPACING, 24 * SPACING, x, y);

            menu.add(buffaloWingsBlueImage);
            menu.add(roboPuffsBlueImage);
            menu.add(buffaloWingsRedImage);
            menu.add(roboPuffsRedImage);
            menu.add(redTeam);
            menu.add(blueTeam);
        }
        frame.repaint();
    }

    public void OnImageButton(int id) 
    {
        if(mode == multiPlayerPractice || mode == multiPlayerCompetition)
        {
            System.out.println(true);
            if(id == buffaloWingsTeamRed || id == roboPuffsTeamRed)
            {
                if(robot0 == -1)
                {
                    robot0 = id == buffaloWingsTeamRed ? buffaloWingsTeam : roboPuffsTeam;
                }
            }
            if(id == buffaloWingsTeamBlue || id == roboPuffsTeamBlue)
            {
                if(robot1 == -1)
                {
                    robot1 = id == buffaloWingsTeamBlue ? buffaloWingsTeam : roboPuffsTeam;
                }
            }
            
            if(robot0 != -1 && robot1 != -1)
            {
                frame.remove(menu);
                frame.add(gameScreen);
                APP.runCanvas(mode, robot0, robot1);
                frame.repaint();
            }
        }
        else //single player
        {
            if(id == buffaloWingsTeam || id == roboPuffsTeam)
            {
                robot0 = id;
                robot1 = -1;
                frame.remove(menu);
                frame.add(gameScreen);
                APP.runCanvas(mode, robot0, robot1);
                frame.repaint();
            }
        }
        if(id == singlePlayerPractice || id == multiPlayerPractice || id == singlePlayerTimed || id == multiPlayerCompetition)
        {
            mode = id;
            updateMenu(id);
        }
        //gameScreen.requestFocus();
    }
}