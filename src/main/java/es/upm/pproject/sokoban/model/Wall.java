package es.upm.pproject.sokoban.model;

import es.upm.pproject.sokoban.interfaces.Square;

public class Wall implements Square {
	 private final Position position;

	 public Wall(Position position) {
	        this.position = position;
	    }
	 
	 public boolean move(char way, int turn) {
		 return false;
	 }



	public Position getPosition() {
		return position;
	}
}
