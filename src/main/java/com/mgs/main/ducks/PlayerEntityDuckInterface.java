package com.mgs.main.ducks;

import com.mgs.main.games.MiniGame;
import com.mgs.main.games.players.Party;
import com.mgs.main.games.players.Team;
import com.mgs.main.gui.Gui;

public interface PlayerEntityDuckInterface {
	
	Team mgs$getTeam();
	
	void mgs$setTeam(Team team);
	
	MiniGame mgs$getMiniGame();
	
	void mgs$setMiniGame(MiniGame miniGame);
	
	Party mgs$getParty();
	
	void mgs$remove();
	
	Gui mgs$getCurrentGui();
	
	void mgs$setCurrentGui(Gui gui);
	
}
