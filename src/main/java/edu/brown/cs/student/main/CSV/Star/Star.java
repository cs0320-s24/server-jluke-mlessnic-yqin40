package edu.brown.cs.student.main.CSV.Star;

public class Star {
    public String StarID;
    public String ProperName;
    public String X;
    public String Y;
    public String Z;

    public void setX(String x) {
        X = x;
    }

    public void setY(String y) {
        Y = y;
    }

    public void setZ(String z) {
        Z = z;
    }

    public void setStarID(String starID) {
        StarID = starID;
    }

    public void setProperName(String properName) {
        ProperName = properName;
    }

    @Override
    public String toString() {
        return "Star{"
                + "StarID='"
                + StarID
                + '\''
                + ", ProperName='"
                + ProperName
                + '\''
                + ", X='"
                + X
                + '\''
                + ", Y='"
                + Y
                + '\''
                + ", Z='"
                + Z
                + '\''
                + '}';
    }
}