package jgf.math;

import static java.lang.Math.*;

import java.awt.geom.Point2D;
import jgf.util.HashBuilder;

public final class Vector2D implements Cloneable {
	private double x;
	private double y;

	public Vector2D() {
		x = y = 0;
	}

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D(Point2D point) {
		this.x = point.getX();
		this.y = point.getY();
	}

	public static Vector2D createByMagAngle(double mag, double angle) {
		return new Vector2D(cos(angle) * mag, sin(angle) * mag);
	}

	public Vector2D set(double newX, double newY) {
		x = newX;
		y = newY;
		return this;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getMagnitude() {
		return sqrt(getMagnitudeSqr());
	}

	public double getMagnitudeSqr() {
		return x * x + y * y;
	}

	public Vector2D setMagnitude(double newMagnitude) {
		double relation = newMagnitude / getMagnitude();
		x *= relation;
		y *= relation;
		return this;
	}

	public double getAngle() {
		return atan2(y, x);
	}

	public Vector2D rotateMe(double angle) {
		double s = sin(angle);
		double c = cos(angle);

		double newX = x * c - y * s;
		double newY = x * s + y * c;

		x = newX;
		y = newY;

		return this;
	}

	public Vector2D rotate(double angle) {
		return clone().rotateMe(angle);
	}

	public Vector2D normalizeMe() {
		return divideMe(getMagnitude());
	}

	public Vector2D normalize() {
		return clone().normalizeMe();
	}

	public Vector2D addMe(Vector2D other) {
		x += other.x;
		y += other.y;
		return this;
	}

	public Vector2D add(Vector2D other) {
		return clone().addMe(other);
	}

	public Vector2D subtractMe(Vector2D other) {
		x -= other.x;
		y -= other.y;
		return this;
	}

	public Vector2D subtract(Vector2D other) {
		return clone().subtractMe(other);
	}

	public Vector2D multiplyMe(double c) {
		x *= c;
		y *= c;
		return this;
	}

	public Vector2D multiply(double c) {
		return clone().multiplyMe(c);
	}

	public Vector2D divideMe(double c) {
		double f = 1.0F / c;
		x *= f;
		y *= f;
		return this;
	}

	public Vector2D divide(double c) {
		return clone().divideMe(c);
	}

	public Vector2D negate() {
		return new Vector2D(-x, -y);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Vector2D)) {
			return false;
		}
		Vector2D other = (Vector2D) obj;

		return x == other.x && y == other.y;
	}

	@Override
	public int hashCode() {
		return new HashBuilder(x).add(y).hashCode();
	}

	public double dot(Vector2D other) {
		return x * other.x + y * other.y;
	}

	public double angleBetween(Vector2D other) {
		double angle = other.getAngle() - getAngle();

		if (abs(angle) < PI) {
			return angle;
		}
		return (angle + (angle < 0 ? 2 * PI : -2 * PI));
	}

	public double angleSign(Vector2D other) {
		double dp, angPi;

		dp = dot(other); // dot product
		if (dp >= 1.0) {
			dp = 1.0f;
		}
		if (dp <= -1.0) {
			dp = -1.0f;
		}
		angPi = acos(dp);

		// side test
		if (y * other.x > x * other.y) {
			return -angPi;
		}
		return angPi;
	}

	@Override
	public String toString() {
		return String.format("x: %.3f y: %.3f", x, y);
	}

	public Point2D asPoint() {
		return new Point2D.Double(x, y);
	}

	@Override
	public Vector2D clone() {
		try {
			return (Vector2D) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	public double[] toArray() {
		return new double[] { x, y };
	}
}
