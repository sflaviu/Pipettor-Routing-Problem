package model;

public class HeightRange {

    private int heightLow;
    private int heightHigh;

    public HeightRange(int low,int high)
    {
        this.heightLow=low;
        this.heightHigh=high;
    }
    public void decreaseLow() {
        heightLow--;
    }
    public void increaseHigh() {
        heightHigh++;
    }

    public int getHeightLow() {
        return heightLow;
    }

    public void setHeightLow(int heightLow) {
        this.heightLow = heightLow;
    }

    public int getHeightHigh() {
        return heightHigh;
    }

    public void setHeightHigh(int heightHigh) {
        this.heightHigh = heightHigh;
    }

    public boolean contains(int val)
    {
        if(this.heightLow<=val && val<=this.heightHigh)
            return true;
        return false;
    }
}
