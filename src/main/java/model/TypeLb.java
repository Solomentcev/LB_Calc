package model;

public enum TypeLb {
    TYPE1(100,5,40),
    TYPE2(100,20,40),
    TYPE3(160,20,40),
    TYPE4(60,5,0);
    private int deltaWidth;
    private int shelfThick;
    private int serviceZoneWidth;
    TypeLb(int deltaWidth, int shelfThick, int serviceZoneWidth) {
        this.deltaWidth=deltaWidth;
        this.shelfThick=shelfThick;
        this.serviceZoneWidth=serviceZoneWidth;
    }
    public int getServiceZoneWidth() {
        return serviceZoneWidth;
    }
    public int getDeltaWidth() {
        return deltaWidth;
    }
    public int getShelfThick() {
        return shelfThick;
    }
}
