package com.perceptivesoftware.codedash.simulation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class SpaceBattleSimulation {

	public static void main(String[] args) throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String lineIn = in.readLine();
	    
	    while (lineIn != null){
	    	List<Ship> alliedShips = new ArrayList<Ship>();
    		List<Ship> enemyShips = new ArrayList<Ship>();
    		
	    	while(!lineIn.equals("END BATTLE")){

	    		if(lineIn.equals("ENEMY")){
	    			enemyShips = convertInputToList(in.readLine());
	    		}
	    		
	    		if(lineIn.equals("FRIENDLY")){
	    			alliedShips  = convertInputToList(in.readLine());
	    		}
	    		
	    		lineIn = in.readLine();
	    		
	    	}
	    	
	    	//Sim the battle
	    	boolean isVictory = battle(alliedShips, enemyShips);
	    	
	    	//Print the results
	    	if(isVictory){
	    		System.out.println("VICTORY IS ASSURED");
	    	}
	    	else{
	    		System.out.println("RETREAT");
	    	}

	    	lineIn = in.readLine();
	    }	    
	    in.close();

	}
	
	protected static List<Ship> convertInputToList(String commaDelem){
		List<Ship> listOfShips = new ArrayList<Ship>();
		String[] shipAbbreviations = commaDelem.split(",");
		
		for(String ship : shipAbbreviations){
			listOfShips.add(mapAbbreviationToShip(ship));
		}
		
		return listOfShips;
		
	}
	
	protected static Ship mapAbbreviationToShip(String shipAbbreviation){
		SpaceBattleSimulation sim = new SpaceBattleSimulation();

        switch(shipAbbreviation){
            case "DN":
                return sim.new Dreadnaught();
            case "CR":
                return sim.new Cruiser();
            case "DE":
                return sim.new Destroyer();
            case "PV":
                return sim.new PatrolVessel();
            case "F":
                return sim.new Fighter();
            case "B":
                return sim.new Bomber();
            default:
                return null;
        }

	}
	
	protected static boolean battle(List<Ship> friendly, List<Ship> enemy){
		
		Ship friendlyShip = friendly.remove(0);
		Ship enemyShip = enemy.remove(0);
		
		int friendlyFleetSize = friendly.size() + 1;
		int enemyFleetSize = enemy.size() + 1;
		
		while(friendlyFleetSize > 0 && enemyFleetSize > 0){
			engagement(enemyShip, friendlyShip);

			if(friendlyShip.isDestroyed){
				if(friendly.size() > 0){
					friendlyShip = friendly.remove(0);
				}
				--friendlyFleetSize;
			}
			
			if(enemyShip.isDestroyed){
				if(enemy.size() > 0){
					enemyShip = enemy.remove(0);
				}
				--enemyFleetSize;
			}
			
		}

        return friendlyFleetSize > 0 && enemyFleetSize == 0;
			
		
	}
	
	
	public static void engagement(Ship enemy, Ship friend){
		while(!enemy.isDestroyed && friend.isDestroyed == false){
			//Friendly attacks enemy
			int friendAtkVal = friend.attack((Class<Ship>) enemy.getClass());
			enemy.attacked(friendAtkVal);
			
			//Enemy attacks Friendly
			int enemyAttackVal = enemy.attack((Class<Ship>) friend.getClass());
			friend.attacked(enemyAttackVal);
			
		}
	}
	
	
	public class Ship{
		
		private int hp = 0;
		private int baseDamage = 0;
		public Double multiplier = 1.0;
		public boolean isDestroyed = false;
		
		public boolean attacked(int damage){
			
			hp = hp - damage;
			
			if(hp <= 0){
				isDestroyed = true;
			}
			
			return isDestroyed;
		}
		
		public int attack(Class<Ship> shipType){
			return (int) (baseDamage * multiplier);
		}
		
		public int getHp(){
			return hp;
		}
		
	}
	
	public class Dreadnaught extends Ship{
		private int hp = 750000;
		private int baseDamage = 10000;
		private Double multiplier = 0.1;
		
		public Dreadnaught(){
			super.hp = hp;
			super.baseDamage = baseDamage;
			super.multiplier = multiplier;
		}
		
		@Override
		public int attack(Class<Ship> shipType){
			String canonicalName = shipType.getCanonicalName();
			if(canonicalName.equals(Bomber.class.getCanonicalName()) || 
					canonicalName.equals(Fighter.class.getCanonicalName())){
				return (int) (baseDamage * multiplier);
			}
			
			return baseDamage;	
		}
	}
	
	public class Cruiser extends Ship{
		private int hp = 500000;
		private int baseDamage = 5000;
		private Double multiplier = 0.2;
		
		public Cruiser(){
			super.hp = hp;
			super.baseDamage = baseDamage;
			super.multiplier = multiplier;
		}
		
		@Override
		public int attack(Class<Ship> shipType){
			String canonicalName = shipType.getCanonicalName();
			if(canonicalName.equals(Bomber.class.getCanonicalName()) || 
					canonicalName.equals(Fighter.class.getCanonicalName())){
				return (int) (baseDamage * multiplier);
			}
			
			return baseDamage;	
		}
	}
	
	public class Destroyer extends Ship{
		private int hp = 250000;
		private int baseDamage = 2500;
		private Double multiplier = 1.0;
		
		public Destroyer(){
			super.hp = hp;
			super.baseDamage = baseDamage;
			super.multiplier = multiplier;
		}
	}
	
	public class PatrolVessel extends Ship{
		private int hp = 75000;
		private int baseDamage = 1500;
		private Double multiplier = 2.0;
		
		public PatrolVessel(){
			super.hp = hp;
			super.baseDamage = baseDamage;
			super.multiplier = multiplier;
		}
		
		@Override
		public int attack(Class<Ship> shipType){
			String canonicalName = shipType.getCanonicalName();
			if(canonicalName.equals(Fighter.class.getCanonicalName()) ||
					canonicalName.equals(Bomber.class.getCanonicalName())){
				return (int) (multiplier * baseDamage);
			}
			
			return baseDamage;
		}
	}
	
	public class Fighter extends Ship{
		private int hp = 2500;
		private int baseDamage = 200;
		private Double multiplier = 1.0;
		
		public Fighter(){
			super.hp = hp;
			super.baseDamage = baseDamage;
			super.multiplier = multiplier;
		}
		
		@Override
		public int attack(Class<Ship> shipType){
			String canonicalName = shipType.getCanonicalName();
			if(canonicalName.equals(Bomber.class.getCanonicalName())){
				return 2500;
			}
			
			return baseDamage;
		}
	}
	
	public class Bomber extends Ship {
		private int hp = 2500;
		private int baseDamage = 200;
		private Double multiplier = 500.00;
		private Double dnMultiplier = 1000.00;
		
		public Bomber(){
			super.hp = hp;
			super.baseDamage = baseDamage;
			super.multiplier = multiplier;
		}
		
		@Override
		public int attack(Class<Ship> shipType){
			String canonicalName = shipType.getCanonicalName();
			if(canonicalName.equals(Destroyer.class.getCanonicalName()) || 
					canonicalName.equals(Cruiser.class.getCanonicalName())){
				return (int) (baseDamage * multiplier);
			}
			
			if(canonicalName.equals(Dreadnaught.class.getCanonicalName())){
				return (int) (baseDamage * dnMultiplier);
			}
			
			return baseDamage;	
		}
	}
	

}
