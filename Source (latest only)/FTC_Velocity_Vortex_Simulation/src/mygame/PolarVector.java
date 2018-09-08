package mygame;

import com.jme3.math.Vector3f;

public class PolarVector 
{
    private float theta;
    private float magnitude;
    
    //Use radians
    public PolarVector(float magnitude, float theta)
    {
        this.magnitude = magnitude;
        this.theta = theta;
    }
    
    public PolarVector(Vector3f vec)
    {
        magnitude = VectorMath.magnitude(vec);
        theta = VectorMath.theta(vec);
    }
    
    public void addAngle(float a)
    {
        theta += a;
    }
    
    public float theta()
    {
        return theta;
    }
    
    public float magnitude()
    {
        return magnitude;
    }
}