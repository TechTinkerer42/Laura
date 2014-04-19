package imageanalysis;

public class Coordinates {

	private int x;
	private int y;
	
	public Coordinates(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public String toString(){
		return "X is " + x + ", Y is " + y;
	}
	
	public int getDifference(){
		return Math.abs(x-y);
	}
}
