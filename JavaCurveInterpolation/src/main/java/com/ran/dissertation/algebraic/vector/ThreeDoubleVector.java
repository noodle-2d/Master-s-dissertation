package com.ran.dissertation.algebraic.vector;

import com.ran.dissertation.algebraic.common.AlgebraicObject;
import java.util.Objects;

public class ThreeDoubleVector implements Comparable<ThreeDoubleVector>, AlgebraicObject<ThreeDoubleVector> {

    private final DoubleVector doubleVector;
    
    public ThreeDoubleVector(double x, double y, double z) {
        this.doubleVector = new DoubleVector(x, y, z);
    }
    
    private ThreeDoubleVector(DoubleVector doubleVector) {
        this.doubleVector = doubleVector;
    }
    
    public double getX() {
        return doubleVector.getCoordinate(0);
    }
    
    public double getY() {
        return doubleVector.getCoordinate(1);
    }
    
    public double getZ() {
        return doubleVector.getCoordinate(2);
    }
    
    private DoubleVector getDoubleVector() {
        return doubleVector;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.doubleVector);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ThreeDoubleVector other = (ThreeDoubleVector) obj;
        return Objects.equals(this.doubleVector, other.doubleVector);
    }

    @Override
    public int compareTo(ThreeDoubleVector other) {
        return this.doubleVector.compareTo(other.getDoubleVector());
    }

    @Override
    public ThreeDoubleVector add(ThreeDoubleVector other) {
        return new ThreeDoubleVector(this.doubleVector.add(other.getDoubleVector()));
    }

    @Override
    public ThreeDoubleVector substract(ThreeDoubleVector other) {
        return new ThreeDoubleVector(this.doubleVector.substract(other.getDoubleVector()));
    }
    
    @Override
    public ThreeDoubleVector multiply(int number) {
        return new ThreeDoubleVector(this.doubleVector.multiply(number));
    }

    @Override
    public ThreeDoubleVector multiply(double number) {
        return new ThreeDoubleVector(this.doubleVector.multiply(number));
    }
    
    @Override
    public ThreeDoubleVector multiply(ThreeDoubleVector other) {
        double x = this.getZ() * other.getY() - this.getY() * other.getZ();
        double y = this.getX() * other.getZ() - this.getZ() * other.getX();
        double z = this.getY() * other.getX() - this.getX() * other.getY();
        return new ThreeDoubleVector(x, y, z);
    }

    @Override
    public double scalarMultiply(ThreeDoubleVector other) {
        return this.doubleVector.scalarMultiply(other.getDoubleVector());
    }

    @Override
    public String toString() {
        return "ThreeDoubleVector{" + "x=" + getX() + "; y=" + getY() + "; z=" + getZ() + '}';
    }
    
}