package Model;

public class Coordinates {

    private int x;
    private int y;

    public Coordinates(){}

    public Coordinates (int x,int y){
        setCoordinates(x,y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
