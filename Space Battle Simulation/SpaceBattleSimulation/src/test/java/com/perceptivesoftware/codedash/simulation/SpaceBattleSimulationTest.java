package com.perceptivesoftware.codedash.simulation;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.perceptivesoftware.codedash.simulation.SpaceBattleSimulation.Bomber;
import com.perceptivesoftware.codedash.simulation.SpaceBattleSimulation.Cruiser;
import com.perceptivesoftware.codedash.simulation.SpaceBattleSimulation.Destroyer;
import com.perceptivesoftware.codedash.simulation.SpaceBattleSimulation.Dreadnaught;
import com.perceptivesoftware.codedash.simulation.SpaceBattleSimulation.Fighter;
import com.perceptivesoftware.codedash.simulation.SpaceBattleSimulation.PatrolVessel;

public class SpaceBattleSimulationTest {

	@Test
	public void dreadnaughtTest() {
		SpaceBattleSimulation sim = new SpaceBattleSimulation();
		
		SpaceBattleSimulation.Bomber friend = sim.new Bomber();
		SpaceBattleSimulation.PatrolVessel enemy = sim.new PatrolVessel();
		
		SpaceBattleSimulation.engagement(enemy, friend);
		
		System.out.println("Dreadnaught: " + friend.isDestroyed);
		System.out.println("Patrol Vessel: " + enemy.isDestroyed);
		
	}
	
	@Test
	public void battleTestEnemyWins() {
		SpaceBattleSimulation sim = new SpaceBattleSimulation();
		
		List<SpaceBattleSimulation.Ship> friendly = new ArrayList<SpaceBattleSimulation.Ship>();
		List<SpaceBattleSimulation.Ship> enemy = new ArrayList<SpaceBattleSimulation.Ship>();
		
		friendly.add(sim.new Dreadnaught());
		friendly.add(sim.new PatrolVessel());
		
		enemy.add(sim.new Bomber());
		enemy.add(sim.new Bomber());
		enemy.add(sim.new Destroyer());
		
		boolean isVictory = SpaceBattleSimulation.battle(friendly, enemy);
		
		if(isVictory){
			System.out.println("Friendly wins!");
		}
		else{
			System.out.println("Enemy Wins!");
		}
		
	}
	
	@Test
	public void battleTestEnemyLoses() {
		SpaceBattleSimulation sim = new SpaceBattleSimulation();
		
		List<SpaceBattleSimulation.Ship> friendly = new ArrayList<SpaceBattleSimulation.Ship>();
		List<SpaceBattleSimulation.Ship> enemy = new ArrayList<SpaceBattleSimulation.Ship>();
		
		friendly.add(sim.new Dreadnaught());
		friendly.add(sim.new PatrolVessel());
		
		enemy.add(sim.new Destroyer());
		enemy.add(sim.new Bomber());
		enemy.add(sim.new Bomber());
		
		
		boolean isVictory = SpaceBattleSimulation.battle(friendly, enemy);
		
		if(isVictory){
			System.out.println("Friendly wins!");
		}
		else{
			System.out.println("Enemy Wins!");
		}
		
	}
	
	@Test
	public void bomberAttacksDreadnaught(){
		SpaceBattleSimulation sim = new SpaceBattleSimulation();
		//One bomber should do 200,000 dmg per round.
		//A Dreadnaught will do 1,000 dmg against a bomber.
		//A deadnaught with full health will destroy a bomber in 3 rounds
		
		Bomber bomber = sim.new Bomber();
		Dreadnaught dread = sim.new Dreadnaught();
		
		int startingHealth = dread.getHp();
		
		SpaceBattleSimulation.engagement(bomber, dread);
		
		int endingHealth = dread.getHp();
		Assert.assertEquals(600000, (startingHealth - endingHealth));
		
	}
	
	@Test
	public void fighterAttacksDreadnaught(){
		SpaceBattleSimulation sim = new SpaceBattleSimulation();
		//One fighter should do 200 dmg per round.
		//A Dreadnaught will do 1,000 dmg against a fighter.
		//A deadnaught with full health will destroy a fighter in 3 rounds
		
		Fighter fighter = sim.new Fighter();
		Dreadnaught dread = sim.new Dreadnaught();
		
		int startingHealth = dread.getHp();
		
		SpaceBattleSimulation.engagement(fighter, dread);
		
		int endingHealth = dread.getHp();
		Assert.assertEquals(600, (startingHealth - endingHealth));
		
	}
	
	@Test
	public void bomberAttacksCruiser(){
		SpaceBattleSimulation sim = new SpaceBattleSimulation();
		//One bomber should do 100,000 dmg per round.
		//A Cruiser will do 1,000 dmg against a bomber.
		//A Cruiser with full health will destroy a bomber in 3 rounds
		
		Bomber bomber = sim.new Bomber();
		Cruiser cruiser = sim.new Cruiser();
		
		int startingHealth = cruiser.getHp();
		
		SpaceBattleSimulation.engagement(bomber, cruiser);
		
		int endingHealth = cruiser.getHp();
		Assert.assertEquals(300000, (startingHealth - endingHealth));
		
	}
	
	@Test
	public void fighterAttacksCruiser(){
		SpaceBattleSimulation sim = new SpaceBattleSimulation();
		//One fighter should do 200 dmg per round.
		//A Cruiser will do 1,000 dmg against a fighter.
		//A Cruiser with full health will destroy a fighter in 3 rounds
		
		Fighter fighter = sim.new Fighter();
		Cruiser cruiser = sim.new Cruiser();
		
		int startingHealth = cruiser.getHp();
		
		SpaceBattleSimulation.engagement(fighter, cruiser);
		
		int endingHealth = cruiser.getHp();
		Assert.assertEquals(600, (startingHealth - endingHealth));
		
	}
	
	@Test
	public void bomberAttacksDestroyer(){
		SpaceBattleSimulation sim = new SpaceBattleSimulation();
		//One bomber should do 100,000 dmg per round.
		//A Destroyer will do 2,500 dmg against a bomber.
		//A Destroyer with full health will destroy a bomber in 1 round
		
		Bomber bomber = sim.new Bomber();
		Destroyer destroyer = sim.new Destroyer();
		
		int startingHealth = destroyer.getHp();
		
		SpaceBattleSimulation.engagement(bomber, destroyer);
		
		int endingHealth = destroyer.getHp();
		Assert.assertEquals(100000, (startingHealth - endingHealth));
		
	}
	
	@Test
	public void bomberAttacksPatrolVessel(){
		SpaceBattleSimulation sim = new SpaceBattleSimulation();
		//One bomber should do 200 dmg per round.
		//A Patrol Vessel will do 2,500 dmg against a bomber.
		//A Patrol Vessel with full health will destroy a bomber in 1 round
		
		Bomber bomber = sim.new Bomber();
		PatrolVessel patrol = sim.new PatrolVessel();
		
		int startingHealth = patrol.getHp();
		
		SpaceBattleSimulation.engagement(bomber, patrol);
		
		int endingHealth = patrol.getHp();
		Assert.assertEquals(200, (startingHealth - endingHealth));
	}
	
	@Test
	public void fighterAttacksPatrolVessel(){
		SpaceBattleSimulation sim = new SpaceBattleSimulation();
		//One fighter should do 200 dmg per round.
		//A Patrol Vessel will do 2,500 dmg against a fighter.
		//A Patrol Vessel with full health will destroy a fighter in 1 round
		
		Fighter fighter = sim.new Fighter();
		PatrolVessel patrol = sim.new PatrolVessel();
		
		int startingHealth = patrol.getHp();
		
		SpaceBattleSimulation.engagement(fighter, patrol);
		
		int endingHealth = patrol.getHp();
		Assert.assertEquals(200, (startingHealth - endingHealth));
	}
	
	@Test
	public void fighterAttacksBomber(){
		SpaceBattleSimulation sim = new SpaceBattleSimulation();
		//One fighter should do 200 dmg per round.
		//A Patrol Vessel will do 2,500 dmg against a fighter.
		//A Patrol Vessel with full health will destroy a fighter in 1 round
		
		Fighter fighter = sim.new Fighter();
		Bomber bomber = sim.new Bomber();
		
		int startingHealth = fighter.getHp();
		
		SpaceBattleSimulation.engagement(fighter, bomber);
		
		int endingHealth = fighter.getHp();
		Assert.assertEquals(200, (startingHealth - endingHealth));
	}
	
}
