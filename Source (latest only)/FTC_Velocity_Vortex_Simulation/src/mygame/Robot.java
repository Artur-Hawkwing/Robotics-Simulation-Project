package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

enum RobotType{BUFFALO_WINGS, ROBOPUFFS};
enum ControllerSet{CONTROLLER_ONE, CONTROLLER_TWO};

public abstract class Robot implements AnimEventListener
{
    //Basic Necesities
    protected AnimControl control;
    protected AnimChannel channel;
    protected CharacterControl robot;
    protected Node robotModel;
    protected Node rootNode;
    protected BulletAppState bulletAppState;
    protected AssetManager assetManager;
    protected InputManager inputManager;
    protected String robotModelPath;
    protected RobotType robotType;
    protected ControllerSet controllerSet;
    protected int[] controllerNums;
    protected String name = "";
    protected static int LOC = 0;
    protected Main APP;
    
    //Listeners
    protected ActionListener actionListener;
    protected AnalogListener analogListener;
    
    //Movement and Animation
    protected boolean LB2, RB2, Y2, A2, X2;
    protected boolean upL1, downL1, upR1, downR1;
    protected long upL1Time, downL1Time, upR1Time, downR1Time;
    protected float upL1Val, downL1Val, upR1Val, downR1Val;
    protected Vector3f dir;
    protected float rot;
    protected long animStartTime = 0l;
    protected long beaconChangeTime = 0l;
    protected int robotColor = 1;
    
    public Robot(Node rootNode, BulletAppState bulletAppState, AssetManager assetManager, InputManager inputManager, RobotType robotType, ControllerSet controllerSet, Main APP)
    {
        this.rootNode = rootNode;
        this.bulletAppState = bulletAppState;
        this.assetManager = assetManager;
        this.inputManager = inputManager;
        this.controllerSet = controllerSet;
        this.robotType = robotType;
        this.APP = APP;
        
        LOC++;
    }
    
    public abstract void update(float tpf);
    public abstract void collision(PhysicsCollisionEvent event);
    public Node getRobotModel()
    {
        return robotModel;
    }
}