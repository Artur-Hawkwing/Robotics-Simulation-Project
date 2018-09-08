package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;

/* Controls:
   Controller 2
   RB -> collect
   RT -> collect using analog signal
   LB -> shoot
   Y -> A -> Beacons

   Controller 1
   drive*/

public class Main extends SimpleApplication implements AnimEventListener, PhysicsCollisionListener
{
    //Pysics and Space
    private BulletAppState bulletAppState;
    
    //Beacons
    private Spatial beacon1, beacon2, beacon3, beacon4;
    private CollisionShape beacon1Shape, beacon2Shape, beacon3Shape, beacon4Shape;
    private RigidBodyControl beacon1Body, beacon2Body, beacon3Body, beacon4Body;
    private int b1Color, b2Color, b3Color, b4Color;
    private Vector3f beacon1Loc = new Vector3f(5f, 1.0f, 12f);
    private Vector3f beacon2Loc = new Vector3f(-3f, 1.0f, 12f);
    private Vector3f beacon3Loc = new Vector3f(12f, 1.0f, 5f);
    private Vector3f beacon4Loc = new Vector3f(12f, 1.0f, -3f);
    
    //Materials
    private Material red, blue;
    
    //Random
    private Random r = new Random();
    
    //View
    private boolean view2; //Used to change perspective in game
    private GUI gui;
    
    //Game setup
    private int mode = -1;
    private ArrayList<Integer> robotNumList = new ArrayList<Integer>();
    private int robot0, robot1;
    private Robot[] robotList;
    
    //Sounds
    private AudioNode hornAudio;
    private boolean startPlayed, endGamePlayed, finalPlayed;
    
    //Timing
    private float time;
    
    //Listeners
    private ActionListener actionListener = new ActionListener() 
    {
        public void onAction(String name, boolean keyPressed, float tpf) 
        {
            if(name.equals("Close"))
            {
                Runtime.getRuntime().halt(0);
            }
            else if(name.equals("Regen"))
            {
                initParticles(1,1);
            }
        }
    };

    public void runGame()
    {
        Runnable r = new Runnable()
        {
            public void run()
            {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                
                //settings
                setShowSettings(false);
                setDisplayFps(false);
                setDisplayStatView(false);
                AppSettings settings = new AppSettings(true);
                settings.setWidth(640);
                settings.setHeight(480);
                settings.setResolution(1600, 900);
                settings.setUseJoysticks(true);
                setPauseOnLostFocus(true);
                setSettings(settings);
                
                //GUI
                createCanvas();
                JmeCanvasContext ctx = (JmeCanvasContext) getContext();
                
                //GUI
                gui = new GUI(screenSize, ctx.getCanvas(), Runner.getMain());
                JFrame frame = gui.getFrame();
                frame.pack();
                frame.setVisible(true);
            }
        };
        EventQueue.invokeLater(r);
    }
    
    public void runCanvas(int mode, int robot0, int robot1)
    {
        this.mode = mode;
        this.robot0 = robot0;
        this.robot1 = robot1;
        robotNumList.add(robot0);
        robotNumList.add(robot1);
        startCanvas();
    }

    @Override
    public void simpleInitApp() 
    {
        //Robot data
        robotList = new Robot[robot1 == -1 ? 1 : 2];
        
        //Set up user-controlled motion
        flyCam.setDragToRotate(true);
               
        //allow physiscs
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        
        //Initialization methods
        initMaterials();
        initField();
        initRobots();
        initKeys();
        initAudio();

        //Sky
        viewPort.setBackgroundColor(ColorRGBA.LightGray);
        
        //Lighting
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(1f, 1f, 1f).normalizeLocal());
        rootNode.addLight(dl);
        DirectionalLight dl1 = new DirectionalLight();
        dl1.setDirection(new Vector3f(-1f, -1f, -1f).normalizeLocal());
        rootNode.addLight(dl1);
        
        //Place Camera
        cam.setLocation(new Vector3f(-16, 8, 7));
        cam.setRotation(new Quaternion(0.1f, 0.8f, -0.2f, 0.5f));
    }
    
    private void initField()
    {
        //Field
        Spatial field = assetManager.loadModel("Models/ftcFieldv3e3/ftcFieldv3e3.j3o");
        field.setName("field");
        field.setLocalScale(1f, 1f, 1f);
        CollisionShape fieldShape = CollisionShapeFactory.createMeshShape((Node) field);
        RigidBodyControl fieldBody = new RigidBodyControl(fieldShape, 0);
        field.addControl(fieldBody);
        rootNode.attachChild(field);
        fieldBody.setPhysicsLocation(new Vector3f(0,0,0)); 
        bulletAppState.getPhysicsSpace().add(fieldBody);
        
        //Red Corner Vortex
        Spatial cornerVortexRed = assetManager.loadModel("Models/RedCornerVortex/RedCornerVortex.j3o");
        cornerVortexRed.setName("cornerVortexRed");
        cornerVortexRed.setLocalScale(1f, 1f, 1f);
        CollisionShape cornerVortexRedShape = CollisionShapeFactory.createMeshShape((Node) cornerVortexRed);
        RigidBodyControl cornerVortexRedBody = new RigidBodyControl(cornerVortexRedShape, 0);
        cornerVortexRed.addControl(cornerVortexRedBody);
        rootNode.attachChild(cornerVortexRed);
        cornerVortexRedBody.setPhysicsLocation(new Vector3f(-10.4f, .8f, 10.4f)); 
        bulletAppState.getPhysicsSpace().add(cornerVortexRedBody);
        
        //Blue Corner Vortex
        Spatial cornerVortexBlue = assetManager.loadModel("Models/BlueCornerVortex/BlueCornerVortex.j3o");
        cornerVortexBlue.setName("cornerVortexBlue");
        cornerVortexBlue.setLocalScale(1f, 1f, 1f);
        CollisionShape cornerVortexBlueShape = CollisionShapeFactory.createMeshShape((Node) cornerVortexBlue);
        RigidBodyControl cornerVortexBlueBody = new RigidBodyControl(cornerVortexBlueShape, 0);
        cornerVortexBlue.addControl(cornerVortexBlueBody);
        rootNode.attachChild(cornerVortexBlue);
        cornerVortexBlueBody.setPhysicsLocation(new Vector3f(10.4f, .8f, -10.4f)); 
        bulletAppState.getPhysicsSpace().add(cornerVortexBlueBody);
        
        //Center Vortex Stand
        Spatial centerVortexStand = assetManager.loadModel("Models/CenterVortexStandV1/CenterVortexStandV1.j3o");
        centerVortexStand.setName("centerVortexStand");
        centerVortexStand.setLocalScale(.8f, .8f, .8f);
        CollisionShape centerVortexStandShape = CollisionShapeFactory.createMeshShape((Node) centerVortexStand);
        RigidBodyControl centerVortexStandBody = new RigidBodyControl(centerVortexStandShape, 0);
        centerVortexStand.addControl(centerVortexStandBody);
        rootNode.attachChild(centerVortexStand);
        centerVortexStandBody.setPhysicsLocation(new Vector3f(0f, .2f, 0f)); 
        bulletAppState.getPhysicsSpace().add(centerVortexStandBody);
        
        //Blue Vortex
        Spatial blueVortex = assetManager.loadModel("Models/BlueVortex/BlueVortex.j3o");
        blueVortex.setName("blueVortex");
        blueVortex.setLocalScale(.8f, .8f, .8f);
        CollisionShape blueVortexShape = CollisionShapeFactory.createMeshShape((Node) blueVortex);
        RigidBodyControl blueVortexBody = new RigidBodyControl(blueVortexShape, 0);
        blueVortex.addControl(blueVortexBody);
        rootNode.attachChild(blueVortex);
        blueVortexBody.setPhysicsLocation(new Vector3f(1.8f, 6.1f, -1.90f));
        bulletAppState.getPhysicsSpace().add(blueVortexBody);
        
        //Red Vortex
        Spatial redVortex = assetManager.loadModel("Models/RedVortex/RedVortex.j3o");
        redVortex.setName("redVortex");
        redVortex.setLocalScale(.8f, .8f, .8f);
        CollisionShape redVortexShape = CollisionShapeFactory.createMeshShape((Node) redVortex);
        RigidBodyControl redVortexBody = new RigidBodyControl(redVortexShape, 0);
        redVortex.addControl(redVortexBody);
        rootNode.attachChild(redVortex);
        redVortexBody.setPhysicsLocation(new Vector3f(-1.8f, 6.1f, 1.90f));
        bulletAppState.getPhysicsSpace().add(redVortexBody);
        
        //Cap Balls
        Geometry capBallRed = new Geometry("capBallRed", new Sphere(32, 32, 1f, true, false));
        capBallRed.setMaterial(red);
        rootNode.attachChild(capBallRed);
        capBallRed.setLocalTranslation(new Vector3f(-1.2f, 1.75f, 1.2f));
        RigidBodyControl capBallRedBody = new RigidBodyControl(.1f);
        capBallRed.addControl(capBallRedBody);
        bulletAppState.getPhysicsSpace().add(capBallRedBody);
        
        Geometry capBallBlue = new Geometry("capBallBlue", new Sphere(32, 32, 1f, true, false));
        capBallBlue.setMaterial(blue);
        rootNode.attachChild(capBallBlue);
        capBallBlue.setLocalTranslation(new Vector3f(1.2f, 1.75f, -1.2f));
        RigidBodyControl capBallBlueBody = new RigidBodyControl(.1f);
        capBallBlue.addControl(capBallBlueBody);
        bulletAppState.getPhysicsSpace().add(capBallBlueBody);
        
        initBeacons();
        initParticles(3, 3);
        
    }
    
    private void initBeacons()
    {
        createBeacon(1, 0);
        createBeacon(2, 0);
        createBeacon(3, 0);
        createBeacon(4, 0);
    }
    
    private void createBeacon(int num, int color)
    {
        if((num < 1 || num > 4) || (color < 0 || color > 2))
        {
            return;
        }
        else
        {
            String beaconPath = "";
            if(color == 0)
            {
                beaconPath = "Models/Beacon1/Beacon1.j3o";
            }
            else if(color == 1) //Red
            {
                beaconPath = "Models/BeaconBlue/BeaconBlue.j3o";
            }
            else //Blue
            {
                beaconPath = "Models/BeaconRed/BeaconRed.j3o";
            }
            if(num == 1)
            {
                b1Color = color;
                beacon1 = assetManager.loadModel(beaconPath);
                beacon1.setName("beacon1");
                beacon1.setLocalScale(1f, 1f, 1f);
                beacon1.rotate(0, (float) (-Math.PI / 2), 0);
                beacon1Shape = CollisionShapeFactory.createMeshShape((Node) beacon1);
                beacon1Body = new RigidBodyControl(beacon1Shape, 0);
                beacon1.addControl(beacon1Body);
                rootNode.attachChild(beacon1);
                beacon1Body.setPhysicsLocation(beacon1Loc);
                bulletAppState.getPhysicsSpace().add(beacon1Body);
            }
            else if(num == 2)
            {
                b2Color = color;
                beacon2 = assetManager.loadModel(beaconPath);
                beacon2.setName("beacon2");
                beacon2.setLocalScale(1f, 1f, 1f);
                beacon2.rotate(0, (float) (-Math.PI / 2), 0);
                beacon2Shape = CollisionShapeFactory.createMeshShape((Node) beacon2);
                beacon2Body = new RigidBodyControl(beacon2Shape, 0);
                beacon2.addControl(beacon2Body);
                rootNode.attachChild(beacon2);
                beacon2Body.setPhysicsLocation(beacon2Loc);
                bulletAppState.getPhysicsSpace().add(beacon2Body);
            }
            else if(num == 3)
            {
                b3Color = color;
                beacon3 = assetManager.loadModel(beaconPath);
                beacon3.setName("beacon3");
                beacon3.setLocalScale(1f, 1f, 1f);
                beacon3Shape = CollisionShapeFactory.createMeshShape((Node) beacon3);
                beacon3Body = new RigidBodyControl(beacon3Shape, 0);
                beacon3.addControl(beacon3Body);
                rootNode.attachChild(beacon3);
                beacon3Body.setPhysicsLocation(beacon3Loc);
                bulletAppState.getPhysicsSpace().add(beacon3Body);
            }
            else
            {
                b4Color = color;
                beacon4 = assetManager.loadModel(beaconPath);
                beacon4.setName("beacon4");
                beacon4.setLocalScale(1f, 1f, 1f);
                beacon4Shape = CollisionShapeFactory.createMeshShape((Node) beacon4);
                beacon4Body = new RigidBodyControl(beacon4Shape, 0);
                beacon4.addControl(beacon4Body);
                rootNode.attachChild(beacon4);
                beacon4Body.setPhysicsLocation(beacon4Loc);
                bulletAppState.getPhysicsSpace().add(beacon4Body);
            }
        }
    }
    
    public void reloadBeacon(int num, int color)
    {
        if((num < 1 || num > 4) || (color < 0 || color > 2))
        {
            return;
        }
        else
        {
            if(num == 1)
            {
                bulletAppState.getPhysicsSpace().removeAll(beacon1);
                rootNode.detachChild(beacon1);
            }
            else if(num == 2)
            {
                bulletAppState.getPhysicsSpace().removeAll(beacon2);
                rootNode.detachChild(beacon2);
            }
            else if(num == 3)
            {
                bulletAppState.getPhysicsSpace().removeAll(beacon3);
                rootNode.detachChild(beacon3);
            }
            else
            {
                bulletAppState.getPhysicsSpace().removeAll(beacon4);
                rootNode.detachChild(beacon4);
            }
            createBeacon(num, color);
        }
    }
    
    private void initMaterials()
    {
        red = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
        blue = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
    
        red.setBoolean("UseMaterialColors",true); 
        red.setColor("Diffuse", ColorRGBA.Red); 
        red.setColor("Ambient", ColorRGBA.Red);
        red.setColor("Specular", ColorRGBA.Red);
        
        blue.setBoolean("UseMaterialColors",true); 
        blue.setColor("Diffuse", ColorRGBA.Blue); 
        blue.setColor("Ambient", ColorRGBA.Blue);
        blue.setColor("Specular", ColorRGBA.Blue);
    }
    
    private void initParticles(int numRed, int numBlue)
    {
        for(int i = 0; i < numRed; i++)
        {
            Geometry particleRed = new Geometry("particleRed", new Sphere(32, 32, .2f, true, false));
            particleRed.setMaterial(red);
            rootNode.attachChild(particleRed);
            particleRed.setLocalTranslation(randomLoc(10));
            RigidBodyControl particleRedBody = new RigidBodyControl(.01f);
            particleRed.addControl(particleRedBody);
            bulletAppState.getPhysicsSpace().add(particleRedBody);
        }
        for(int i = 0; i < numBlue; i++)
        {
            Geometry particleBlue = new Geometry("particleBlue", new Sphere(32, 32, .2f, true, false));
            particleBlue.setMaterial(blue);
            rootNode.attachChild(particleBlue);
            particleBlue.setLocalTranslation(randomLoc(10));
            RigidBodyControl particleBlueBody = new RigidBodyControl(.01f);
            particleBlue.addControl(particleBlueBody);
            bulletAppState.getPhysicsSpace().add(particleBlueBody);
        }
    }

    private Vector3f randomLoc(int radius)
    {
        float d = r.nextFloat() * radius;
        float a = r.nextFloat() * (float) (2 * Math.PI);
        PolarVector pv = new PolarVector(d, a);
        return VectorMath.vec(pv, 2);
    }

    private void initRobots()
    {
        if(robot0 == gui.buffaloWingsTeam)
        {
            robotList[0] = new BuffaloWings(rootNode, bulletAppState, assetManager, inputManager, RobotType.BUFFALO_WINGS, ControllerSet.CONTROLLER_ONE, this);
        }
        else
        {
            robotList[0] = new RoboPuffs(rootNode, bulletAppState, assetManager, inputManager, RobotType.ROBOPUFFS, ControllerSet.CONTROLLER_ONE, this, false, 0);
        }
        if(robot1 != -1)
        {    
            if(robot1 == gui.buffaloWingsTeam)
            {
                robotList[1] = new BuffaloWings(rootNode, bulletAppState, assetManager, inputManager, RobotType.BUFFALO_WINGS, ControllerSet.CONTROLLER_TWO, this);
            }
            else
            {
                robotList[1] = new RoboPuffs(rootNode, bulletAppState, assetManager, inputManager, RobotType.ROBOPUFFS, ControllerSet.CONTROLLER_TWO, this, false, 1);
            }
        }
    }
    
    private void initKeys() 
    {
        //Escape
        inputManager.addMapping("Close", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(actionListener, "Close");
        
        inputManager.addMapping("Regen", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, "Regen");
    }
    
    private void initAudio()
    {
        hornAudio = new AudioNode(assetManager, "Sounds/Horn.wav", false);
        hornAudio.setPositional(false);
        hornAudio.setLooping(false);
        hornAudio.setVolume(2);
        rootNode.attachChild(hornAudio);
    }
    
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {}
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {}
    
    @Override
    public void collision(PhysicsCollisionEvent event) 
    {
        Spatial collidedA = event.getNodeA();
        Spatial collidedB = event.getNodeB();
        
        try
        {
            for(Robot r : robotList)
            {
                if(collidedA.getName().equals(r.getRobotModel().getName()) || collidedB.getName().equals(r.getRobotModel().getName()))
                {
                    r.collision(event);
                }
            }
        }
        catch(Exception e){}
        
        if(collidedA.getName().startsWith("cap") && collidedB.getName().endsWith("Vortex"))
        {
            for(Robot r : robotList)
            {
                if(r instanceof RoboPuffs)
                {
                    if(((RoboPuffs) r).getCapBall() == collidedA)
                    {
                        ((RoboPuffs) r).disconnectCapBall();
                    }
                }
            }
            collidedA.getControl(RigidBodyControl.class).setPhysicsLocation(collidedB.getLocalTranslation().add(new Vector3f(0, 1.75f, 0)));
            bulletAppState.getPhysicsSpace().removeAll(collidedA);
        }
        if(collidedB.getName().startsWith("cap") && collidedA.getName().endsWith("Vortex"))
        {
            for(Robot r : robotList)
            {
                if(r instanceof RoboPuffs)
                {
                    if(((RoboPuffs) r).getCapBall() == collidedB)
                    {
                        ((RoboPuffs) r).disconnectCapBall();
                    }
                }
            }
            collidedB.getControl(RigidBodyControl.class).setPhysicsLocation(collidedA.getLocalTranslation().add(new Vector3f(0, 1.75f, 0)));
            bulletAppState.getPhysicsSpace().removeAll(collidedB);
        }
    }
    
    @Override
    public void simpleUpdate(float tpf)
    {   
       //Timing and Sounds
       time += tpf;
       
       if(!startPlayed && time > 0)
       {
           hornAudio.playInstance();
           startPlayed = true;
       }
       if((mode == gui.multiPlayerCompetition || mode == gui.singlePlayerTimed))
       {
           if(!endGamePlayed && time > 120)
           {
               hornAudio.playInstance();
               endGamePlayed = true;
           }
           if(!finalPlayed && time > 150)
           {
               hornAudio.playInstance();
               finalPlayed = true;
           }
       }

       for(Robot r : robotList)
       {
           if(r != null)
           {
               r.update(tpf);
           }
       }
       
       //Lock Camera Location
       if(!view2)
       {
           cam.setLocation(new Vector3f(-25, 12, 9));
           cam.setRotation(new Quaternion(0.1f, 0.8f, -0.2f, 0.5f));
       }
       else
       {
           cam.setLocation(new Vector3f(-21.9f, 12f, -8.1f));
           cam.setRotation(new Quaternion(0.16f, 0.54f, -0.15f, 0.81f));
       }
    }
    
    public void setRobot(int ID, RoboPuffs r)
    {
        bulletAppState.getPhysicsSpace().removeAll(robotList[ID].getRobotModel());
        rootNode.detachChild(robotList[ID].getRobotModel());
        robotList[ID] = r;
    }
    
    public void changeView()
    {
        view2 = !view2;
    }
    
    public int getb1Color()
    {
        return b1Color;
    }
    public int getb2Color()
    {
        return b2Color;
    }
    public int getb3Color()
    {
        return b3Color;
    }
    public int getb4Color()
    {
        return b4Color;
    }
}