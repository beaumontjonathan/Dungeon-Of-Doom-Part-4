
public class DoDPlayer {

	private int id;
	private int collectedGold;
	private int x;
	private int y;
	
	public DoDPlayer(int id, int x, int y){
		this.id = id;
		this.x = x;
		this.y = y;
		collectedGold = 0;
	}
	
	public int getID(){
		return id;
	}
	
	public char getIcon(){
		return Character.forDigit(id,10);
	}
	
	public int getCollectedGold(){
		return collectedGold;
	}
	
	public void incrementCollectedGold(){
		collectedGold++;
	}
	
	public int getXCoordinate(){
		return x;
	}
	
	public void setXCoordinate(int newX){
		x = newX;
	}
	
	public int getYCoordinate(){
		return y;
	}
	
	public void setYCoordinate(int newY){
		y = newY;
	}
	
	public boolean ocupiesSameTile(int otherPlayerX, int otherPlayerY){
		if(x == otherPlayerX && y == otherPlayerY){
			return true;
		}
		return false;
	}
}
