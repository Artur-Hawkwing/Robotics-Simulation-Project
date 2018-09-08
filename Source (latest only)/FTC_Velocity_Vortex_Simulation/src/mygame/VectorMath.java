package mygame;

import com.jme3.math.Vector3f;

public class VectorMath 
{
    public static Vector3f vec(float magnitude, float theta, float y)
    {
        float x = magnitude * (float) Math.sin(theta);
        float z = magnitude * (float) Math.cos(theta);
        return new Vector3f(x, y, z);
    }
    
    public static Vector3f vec(PolarVector pv, float y)
    {
        float x = pv.magnitude() * (float) Math.sin(pv.theta());
        float z = pv.magnitude() * (float) Math.cos(pv.theta());
        return new Vector3f(x, y, z);
    }
    
    public static float magnitude(Vector3f vec)
    {
        return (float) Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);
    }
    
    public static float theta(Vector3f vec)
    {
        float theta = 0f;
        if(vec.z > 0)
        {
            theta = (float) Math.atan(vec.x / vec.z);
        }
        else if(vec.z < 0)
        {
            theta = (float) (Math.atan(vec.x / vec.z) + Math.PI);
        }
        return theta;
    }
    
    public static Vector3f addAngle(Vector3f vec, float theta, float y)
    {
        PolarVector pv = new PolarVector(vec);
        pv.addAngle(theta);
        return VectorMath.vec(pv, y);
    }
}