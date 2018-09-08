package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.ListIterator;

public class BuffaloWings extends Robot 
{
    private ArrayList<Spatial> particles = new ArrayList<Spatial>();
    
    public BuffaloWings(Node rootNode, BulletAppState bulletAppState,
            AssetManager assetManager, InputManager inputManager, RobotType robotType, ControllerSet controllerSet, Main APP)
    {
        super(rootNode, bulletAppState, assetManager, inputManager, robotType, controllerSet, APP);
        robotModelPath = "Models/buffaloWingsv1a1t1b1anim1yay3final9bot1v4/buffaloWingsv1a1t1b1anim1yay3final9bot1v4.j3o";
        initListeners();
        initKeys();
        initRobot();
    }
    
    private int robotNum = controllerSet == ControllerSet.CONTROLLER_ONE ? 1 : 3;
    private int robotSecondaryNum = 1 + robotNum;
    private void initListeners()
    {
        analogListener = new AnalogListener()
        {
            public void onAnalog(String name, float value, float tpf)
            {
                //Movement: z is forward, positive x is left
                if(name.equals("upL" + robotNum))
                {
                    upL1Time = System.currentTimeMillis();
                    upL1Val = value;
                    upL1 = true;
                    downL1 = false;
                }
                if(name.equals("downL" + robotNum))
                {
                    downL1Time = System.currentTimeMillis();
                    downL1Val = value;
                    downL1 = true;
                    upL1 = false;
                }
                if(name.equals("upR" + robotNum))
                {
                    upR1Time = System.currentTimeMillis();
                    upR1Val = value;
                    upR1 = true;
                    downR1 = false;
                }
                if(name.equals("downR" + robotNum))
                {
                    downR1Time = System.currentTimeMillis();
                    downR1Val = value;
                    downR1 = true;
                    upR1 = false;
                }
            }
        };

        actionListener = new ActionListener() 
        {
            public void onAction(String name, boolean keyPressed, float tpf) 
            {
                if(name.equals("LB" + robotSecondaryNum))
                {
                    LB2 = keyPressed;
                    if(LB2)
                    {
                        channel.setAnim("Shoot", 0.50f);
                        channel.setLoopMode(LoopMode.Loop);
                        animStartTime = System.currentTimeMillis();
                    }
                    else
                    {
                        channel.setAnim("Idle", 0.50f);
                    }
                }
                if(name.equals("RB" + robotSecondaryNum))
                {
                    RB2 = keyPressed;
                    if(RB2)
                    {    
                        channel.setAnim("Collect", 0.50f);
                        channel.setLoopMode(LoopMode.Loop);
                        animStartTime = System.currentTimeMillis();
                    }
                    else
                    {
                        channel.setAnim("Idle", 0.50f);
                    }
                }
                if(name.equals("Y" + robotSecondaryNum) || name.equals("A" + robotSecondaryNum))
                {
                    Y2 = keyPressed;
                    A2 = keyPressed;
                }
                if(name.equals("RB" + robotNum) && keyPressed)
                {
                    APP.changeView();
                }
            }
        };
    }

    private void initKeys()
    {        
        //Controllers
        int min = controllerSet == ControllerSet.CONTROLLER_ONE ? 1 : 3;
        int max = controllerSet == ControllerSet.CONTROLLER_ONE ? 2 : 4;
        
        for(int i = min; i <= max; i++)
        {
            int h = i - 1;
            inputManager.addMapping("A" + i, new JoyButtonTrigger(h, 0));
            inputManager.addMapping("B" + i, new JoyButtonTrigger(h, 1));
            inputManager.addMapping("X" + i, new JoyButtonTrigger(h, 2));
            inputManager.addMapping("Y" + i, new JoyButtonTrigger(h, 3));
            inputManager.addMapping("LB" + i, new JoyButtonTrigger(h, 4));
            inputManager.addMapping("RB" + i, new JoyButtonTrigger(h, 5));
            
            inputManager.addMapping("upL" + i, new JoyAxisTrigger(h, 0, true));//upL
            inputManager.addMapping("downL" + i, new JoyAxisTrigger(h, 0, false));//downL
            inputManager.addMapping("leftL" + i, new JoyAxisTrigger(h, 1, true));//leftL
            inputManager.addMapping("rightL" + i, new JoyAxisTrigger(h, 1, false));//rightL
            inputManager.addMapping("upR" + i, new JoyAxisTrigger(h, 2, true));//upR
            inputManager.addMapping("downR" + i, new JoyAxisTrigger(h, 2, false));//downR
            inputManager.addMapping("leftR" + i, new JoyAxisTrigger(h, 3, true));//leftR
            inputManager.addMapping("RightR" + i, new JoyAxisTrigger(h, 3, false));//rightR
            
            inputManager.addListener(actionListener, "A" + i);
            inputManager.addListener(actionListener, "B" + i);
            inputManager.addListener(actionListener, "X" + i);
            inputManager.addListener(actionListener, "Y" + i);
            inputManager.addListener(actionListener, "LB" + i);
            inputManager.addListener(actionListener, "RB" + i);

            inputManager.addListener(analogListener, "upL" + i);
            inputManager.addListener(analogListener, "downL" + i);
            inputManager.addListener(analogListener, "leftL" + i);
            inputManager.addListener(analogListener, "rightL" + i);
            inputManager.addListener(analogListener, "upR" + i);
            inputManager.addListener(analogListener, "downR" + i);
            inputManager.addListener(analogListener, "leftR" + i);
            inputManager.addListener(analogListener, "rightR" + i);
        }
    }
        
    private void initRobot()
    {
        name = "RobotBuffaloWings" + LOC;
        robotModel = (Node) assetManager.loadModel(robotModelPath);
        robotModel.setLocalScale(.667f);
        robotModel.setLocalTranslation(controllerSet == ControllerSet.CONTROLLER_ONE ? new Vector3f(-9, 1.5f, 4) : new Vector3f(4, 1.5f, -9));
        robotModel.setName(name);
        rootNode.attachChild(robotModel);

        //Robot controls
        robot = new CharacterControl(new CapsuleCollisionShape(.4f, .1f, 0), .05f);
        robotModel.addControl(robot);
        bulletAppState.getPhysicsSpace().add(robot);
        robot.setFallSpeed(30);
        robot.setGravity(30);

        //Robot animations
        control = ((Node) robotModel.getChild("Armature")).getChild("Buffalo Wings").getControl(AnimControl.class);
        control.addListener(this);
        channel = control.createChannel();
        channel.setAnim("Idle", 0.50f);
        channel.setLoopMode(LoopMode.DontLoop);
    }
    
    public void update(float tpf)
    {
        //Particle Interactions
        if(particles.size() > 0)
        {
            //Placement
            ListIterator<Spatial> it = particles.listIterator();
            while(it.hasNext())
            {
                Spatial particle = it.next();
                Vector3f robotLoc = robotModel.getLocalTranslation();
                Vector3f particleLoc = new Vector3f(robotLoc.x, robotLoc.y + 1f, robotLoc.z).add(robot.getViewDirection().mult(.5f));
                particle.getControl(RigidBodyControl.class).setPhysicsLocation(particleLoc);
                
                //Shooting
                if(channel.getAnimationName().equals("Shoot"))
                {
                    Vector3f xzDir = robot.getViewDirection();
                    xzDir = xzDir.mult(-1);
                    Vector3f yDir = new Vector3f(0, 12f, 0);
                    Vector3f shootDir = (xzDir.mult(2).add(yDir));//.mult(6);
                    particle.getControl(RigidBodyControl.class).setLinearVelocity(shootDir);
                    it.remove();
                }
            }
        }
        
        //End animation if too much time has passed
        int animCuttoff = 100;
        if(System.currentTimeMillis() - animStartTime > animCuttoff && 
                (channel.getAnimationName().equals("Forwards") || channel.getAnimationName().equals("Backwards") ||
                channel.getAnimationName().equals("Left") || channel.getAnimationName().equals("Right") || 
                channel.getAnimationName().equals("Left_Forwards") || channel.getAnimationName().equals("Right_Forwards") ||
                channel.getAnimationName().equals("Left_Backwards") || channel.getAnimationName().equals("Right_Backwards")))
        {
            channel.setAnim("Idle", 0.50f);
        }

        //Turn off movement flags if too much time has passed
        int cuttoff = 10;
        long time = System.currentTimeMillis();
        if(time - upL1Time > cuttoff)
        {
            upL1 = false;
            upL1Val = 0;
        }
        if(time - downL1Time > cuttoff)
        {
            downL1 = false;
            downL1Val = 0;
        }
        if(time - upR1Time > cuttoff)
        {
            upR1 = false;
            upR1Val = 0;
        }
        if(time - downR1Time > cuttoff)
        {
            downR1 = false;
            downR1Val = 0;
        }

        //movement
        dir = Vector3f.ZERO;
        rot = 0f;
        float diminish = 2;
        
        if(upL1 && upR1)
        {
            dir = new Vector3f(upL1Val, 0, upR1Val);
            animStartTime = System.currentTimeMillis();
            if(!channel.getAnimationName().equals("Forwards") && !channel.getAnimationName().equals("Collect"))
            {
                channel.setAnim("Forwards", 0.50f);
                channel.setLoopMode(LoopMode.Loop);
            }
        }
        else if(downL1 && downR1)
        {
            dir = new Vector3f(-downL1Val, 0, -downR1Val);
            animStartTime = System.currentTimeMillis();
            if(!channel.getAnimationName().equals("Backwards") && !channel.getAnimationName().equals("Collect"))
            {
                channel.setAnim("Backwards", 0.50f);
                channel.setLoopMode(LoopMode.Loop);
                animStartTime = System.currentTimeMillis();
            }
        }
        else if(upL1 && downR1)
        {
            rot = -(upL1Val + downR1Val);
            animStartTime = System.currentTimeMillis();
            if(!channel.getAnimationName().equals("Left") && !channel.getAnimationName().equals("Collect"))
            {
                channel.setAnim("Left", 0.50f);
                channel.setLoopMode(LoopMode.Loop);
                animStartTime = System.currentTimeMillis();
            }
        }
        else if(downL1 && upR1)
        {
            rot = downL1Val + upR1Val;
            animStartTime = System.currentTimeMillis();
            if(!channel.getAnimationName().equals("Right") && !channel.getAnimationName().equals("Collect"))
            {
                channel.setAnim("Right", 0.50f);
                channel.setLoopMode(LoopMode.Loop);
                animStartTime = System.currentTimeMillis();
            }
        }
        else if(upL1)
        {
            rot = -upL1Val / diminish;
            animStartTime = System.currentTimeMillis();
            if(!channel.getAnimationName().equals("Left_Forwards") && !channel.getAnimationName().equals("Collect"))
            {
                channel.setAnim("Left_Forwards", 0.50f);
                channel.setLoopMode(LoopMode.Loop);
                animStartTime = System.currentTimeMillis();
            }
        }
        else if(downR1)
        {
            rot = -downR1Val / diminish;
            animStartTime = System.currentTimeMillis();
            if(!channel.getAnimationName().equals("Right_Backwards") && !channel.getAnimationName().equals("Collect"))
            {
                channel.setAnim("Right_Backwards", 0.50f);
                channel.setLoopMode(LoopMode.Loop);
                animStartTime = System.currentTimeMillis();
            }
        }
        else if(downL1)
        {
            rot = downL1Val / diminish;
            animStartTime = System.currentTimeMillis();
            if(!channel.getAnimationName().equals("Left_Backwards") && !channel.getAnimationName().equals("Collect"))
            {
                channel.setAnim("Left_Backwards", 0.50f);
                channel.setLoopMode(LoopMode.Loop);
                animStartTime = System.currentTimeMillis();
            }
        }
        else if(upR1)
        {
            rot = upR1Val / diminish;
            animStartTime = System.currentTimeMillis();
            if(!channel.getAnimationName().equals("Right_Forwards") && !channel.getAnimationName().equals("Collect"))
            {
                channel.setAnim("Right_Forwards", 0.50f);
                channel.setLoopMode(LoopMode.Loop);
                animStartTime = System.currentTimeMillis();
            }
        }

        Vector3f lookAt = null;
        if(rot != 0 || true)
        {
            lookAt = VectorMath.addAngle(robot.getViewDirection(), rot, 0);
            robot.setViewDirection(lookAt);
        }
        Vector3f move = robot.getPhysicsLocation().add(robot.getViewDirection().mult(dir).mult(2));
        robot.setPhysicsLocation(move);
    }
    
    public void collision(PhysicsCollisionEvent event) 
    {
        Spatial collidedA = event.getNodeA();
        Spatial collidedB = event.getNodeB();
        
        if(collidedA.getName().startsWith("particle"))
        {
            if(channel.getAnimationName().equals("Collect"))
            {
                particles.add(collidedA);
            }
        }
        if(collidedB.getName().startsWith("particle"))
        {
            if(channel.getAnimationName().equals("Collect"))
            {
                particles.add(collidedB);
            }
        }
        
        if(collidedA.getName().startsWith("cap"))
        {
            Vector3f direction = collidedB.getControl(CharacterControl.class).getViewDirection();
            collidedA.getControl(RigidBodyControl.class).setLinearVelocity(direction.mult(5));
        }
        if(collidedB.getName().startsWith("cap"))
        {
            Vector3f direction = collidedA.getControl(CharacterControl.class).getViewDirection();
            collidedB.getControl(RigidBodyControl.class).setLinearVelocity(direction.mult(5));
        }
        
        if(System.currentTimeMillis() - beaconChangeTime > 1000)
        {
            int b1Color = APP.getb1Color();
            int b2Color = APP.getb2Color();
            int b3Color = APP.getb3Color();
            int b4Color = APP.getb4Color();
            
            if(collidedA.getName().startsWith("Robot") && collidedB.getName().startsWith("beacon") && (Y2 || A2))
            {
                if(collidedB.getName().equals("beacon1"))
                {
                    b1Color = b1Color == 0 ? robotColor : b1Color == 1 ? 2 : 1;
                    APP.reloadBeacon(1, b1Color);
                }
                if(collidedB.getName().equals("beacon2"))
                {
                    b2Color = b2Color == 0 ? robotColor : b2Color == 1 ? 2 : 1;
                    APP.reloadBeacon(2, b2Color);
                }
                if(collidedB.getName().equals("beacon3"))
                {
                    b3Color = b3Color == 0 ? robotColor : b3Color == 1 ? 2 : 1;
                    APP.reloadBeacon(3, b3Color);
                }
                if(collidedB.getName().equals("beacon4"))
                {
                    b4Color = b4Color == 0 ? robotColor : b4Color == 1 ? 2 : 1;
                    APP.reloadBeacon(4, b4Color);
                }
            }
        }
    }
    
    public void collect(Spatial particle)
    {
        particles.add(particle);
    }
        
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {}
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {}
    
}