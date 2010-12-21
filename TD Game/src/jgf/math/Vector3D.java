package jgf.math;

import jgf.util.HashBuilder;

public final class Vector3D
{
    double x, y, z;
    
    public Vector3D() 
    {
        this(0,0,0);
    }
    
    public Vector3D(double x, double y, double z)            
    {
        set(x, y, z);
    }

    public Vector3D(Vector2D other, double _z)     
    {
        set(other, z);
    }

    public Vector3D(Vector2D other)    
    {
        this(other, 0);
    }

    public Vector3D(double xyz[])    
    {
        set(xyz);
    }

    public Vector3D setX(double x)
    {
        this.x = x;
        return this;
    }
    
    public Vector3D setY(double y)
    {
        this.y = y;
        return this;
    }
    
    public Vector3D setZ(double z)
    {
        this.z = z;
        return this;
    }
    
    public Vector3D set(double x, double y, double z)
    {
        setX(x);
        setY(y);
        setZ(z);        
        return this;
    }

    public Vector3D set(double xyz[])
    {
        if (xyz.length < 2)
            throw new IllegalArgumentException("There must be at least 2 dimensions!");
        
        this.x = xyz[0];
        this.y = xyz[1];
        
        if (xyz.length >= 3)
            this.z = xyz[3];
        
        return this;
    }

    public Vector3D set(Vector2D v, double _z)
    {
        x = v.getX();
        y = v.getY();
        z = _z;
        return this;
    }

    public Vector3D addMeX(double x)
    {
        this.x += x;
        return this;
    }

    Vector3D addMeY(double y)
    {
        this.y += y;
        return this;
    }

    public Vector3D addMeZ(double z)
    {
        this.z += z;
        return this;
    }

    public Vector3D addMe(double x, double y, double z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3D addMe(Vector3D other)
    {
        return addMe(other.x, other.y, other.z);
    }
    
    public Vector3D addMe(Vector2D other)
    {
        return addMe(other.getX(), other.getY(), 0);
    }
    
    public Vector3D add(Vector3D other)
    {
        return clone().addMe(other);
    }
    
    public Vector3D add(Vector2D other)
    {
        return clone().addMe(other);
    }    
    
    @Override
    public boolean equals(Object o)
    {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (o.getClass() != this.getClass())
            return false;
        
        Vector3D other = (Vector3D)o;        
        return x == other.x && y == other.y && z == other.z;
    }

    @Override
    public int hashCode() {
        return new HashBuilder(x).add(y).add(z).hashCode();
    }
    
    public boolean equals2D(Vector2D other)
    {
        if (other == null)
            return false;
        
        return x == other.getX() && y == other.getY();
    }

    public Vector3D subtractMe(Vector3D other)
    {
        return addMe(-other.x, -other.y, -other.z);
    }
    
    public Vector3D subtractMe(Vector2D other)
    {
        x -= other.getX();
        y -= other.getY();
        return this;
    }
    
    public Vector3D subtract(Vector3D other)
    {
        return clone().subtractMe(other);
    }

    public Vector3D subtract(Vector2D other)
    {
        return clone().subtractMe(other);
    }

    public Vector3D multiplyMe(double c)
    {
        x *= c;
        y *= c;
        z *= c;
        return this;
    }

    public Vector3D divideMe(double c)
    {
        return multiplyMe(1.0 / c);
    }
    
    public Vector3D multiply(double c) 
    {
        return clone().multiplyMe(c);
    }

    public Vector3D divide(double t)
    {
        return clone().divideMe(t);
    }

    public Vector3D negate()
    {
        return new Vector3D(-x, -y, -z);
    }
    
    public double dot(Vector3D other)
    {
        return x * other.x + y * other.y + z * other.z;
    }

    public double dot(Vector2D other) 
    {
        return x * other.getX() + y * other.getY();
    }
    
    public Vector3D crossMe(Vector3D other)
    {
        double nx, ny;

        nx = y * other.z - z * other.y;
        ny = z * other.x - x * other.z;
        z = x * other.y - y * other.x;
        x = nx;
        y = ny;

        return this;
    }

    public Vector3D cross(Vector3D other)
    {
        return clone().crossMe(other);
    }

    public double getSizeSqr()
    {
        return x * x + y * y + z * z;
    }

    public double getSize()
    {
        return Math.sqrt(getSizeSqr());
    }

    public Vector3D setSize(double size)
    {
        return normalizeMe().multiplyMe(size);
    }

    public Vector3D normalizeMe()
    {
        return this.divideMe(getSize());
    }
    
    public Vector3D normalize()
    {
        return clone().normalizeMe();
    }    

    public Vector3D rotateMeX(double angle)
    {
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        double ny = c * y - s * z;
        double nz = c * z + s * y;

        y = ny;
        z = nz;

        return this;
    }
    
    public Vector3D rotateX(double angle)
    {
        return clone().rotateMeX(angle);
    }

    public Vector3D rotateMeY(double angle)
    {
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        double nx = c * x + s * z;
        double nz = c * z - s * x;

        x = nx;
        z = nz;

        return this;
    }
    
    public Vector3D rotateY(double angle)
    {
        return clone().rotateMeY(angle);
    }    

    public Vector3D rotateMeZ(double angle)
    {
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        double nx = c * x - s * y;
        double ny = c * y + s * x;

        x = nx;
        y = ny;

        return this;
    }
    
    public Vector3D rotateZ(double angle)
    {
        return clone().rotateMeZ(angle);
    }    

    public Vector3D rotateMeAxis(double angle, Vector3D axis)
    {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double k = 1.0F - c;

        double nx = x * (c + k * axis.x * axis.x) +
                   y * (k * axis.x * axis.y - s * axis.z) +
                   z * (k * axis.x * axis.z + s * axis.y);

        double ny = x * (k * axis.x * axis.y + s * axis.z) +
                   y * (c + k * axis.y * axis.y) +
                   z * (k * axis.y * axis.z - s * axis.x);

        double nz = x * (k * axis.x * axis.z - s * axis.y) +
                   y * (k * axis.y * axis.z + s * axis.x) +
                   z * (c + k * axis.z * axis.z);

        x = nx;
        y = ny;
        z = nz;

        return this;
    }
    
    public Vector3D rotateAxis(double angle, Vector3D axis)
    {
        return clone().rotateMeAxis(angle, axis);
    }    

    
    @Override
    protected Vector3D clone() 
    {
        try
        {
            return (Vector3D) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public double[] toArray()
    {
        return new double[] {x, y, z};
    }    
}
