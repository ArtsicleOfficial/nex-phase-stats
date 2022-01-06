package com.example;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import static net.runelite.api.ItemID.TWISTED_BOW;

@Slf4j
@PluginDescriptor(
	name = "Example"
)
public class ExamplePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ExampleConfig config;
	@Inject
	private OverlayManager manager;

	public StatsOverlay overlay;

	public PhaseStats[] stats = {new PhaseStats(0),new PhaseStats(1),new PhaseStats(2), new PhaseStats(3), new PhaseStats(4)};
	public int currentPhase = 0;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
		overlay = new StatsOverlay(this,stats);
		manager.add(overlay);
		for(PhaseStats stat : stats) {
			stat.hits = 0;
			stat.misses = 0;
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
		manager.remove(overlay);
	}

	//umbra -> cruor -> glacies
	/*@Subscribe
	public void onActorDeath(ActorDeath actorDeath) {
		String actorName = actorDeath.getActor().getName().toLowerCase();
		if(actorName.startsWith("umbra")) {
			currentPhase = 1;
		}  else if(actorName.startsWith("cruor")) {
			currentPhase = 2;
		} else if(actorName.startsWith("glacies")) {
			currentPhase = 3;
		} else if(actorName.startsWith("nex")) {

		}
	}*/

	@Subscribe
	public void onGameTick(GameTick gameTick) {
		overlay.currentPhase = currentPhase;
		overlay.stats = stats;
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
		if(client.getItemContainer(InventoryID.EQUIPMENT).getItem(EquipmentInventorySlot.WEAPON.getSlotIdx()).getId() != TWISTED_BOW) {
			return;
		}
		if(!hitsplatApplied.getActor().getName().toLowerCase().startsWith("nex")) {
			return;
		}
		if(!hitsplatApplied.getHitsplat().isMine()) {
			return;
		}
		if(hitsplatApplied.getHitsplat().getHitsplatType() == Hitsplat.HitsplatType.DAMAGE_ME) {
			stats[currentPhase].hits++;
		} else if(hitsplatApplied.getHitsplat().getHitsplatType() == Hitsplat.HitsplatType.BLOCK_ME) {
			stats[currentPhase].misses++;
		}


	}
	/*@Subscribe
	public void onNpcChanged(NpcChanged changed) {
		if(changed.getNpc().getName().toLowerCase().startsWith("nex")) {
			currentPhase++;
			log.debug("Phase Change to" + (currentPhase));
		}
	}*/
	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned) {
		if(npcDespawned.getNpc().getHealthRatio() * 100 / npcDespawned.getNpc().getHealthScale() > 10) {
			return;
		}
		String actorName = npcDespawned.getNpc().getName().toLowerCase();
		/*if(actorName.startsWith("fumus")) {
			currentPhase = 1;
		} else if(actorName.startsWith("umbra")) {
			currentPhase = 2;
		}  else if(actorName.startsWith("cruor")) {
			currentPhase = 3;
		} else if(actorName.startsWith("glacies")) {
			currentPhase = 4;
		} else if(actorName.startsWith("nex")) {
			currentPhase = 0;
		}*/
		if(npcDespawned.getNpc().getId() == 11283) { //FUMUS
			currentPhase = 1;
		} else if(npcDespawned.getNpc().getId() == 11284) { //UMBRA
			currentPhase = 2;
		}  else if(npcDespawned.getNpc().getId() == 11285) { //CRUOR
			currentPhase = 3;
		} else if(npcDespawned.getNpc().getId() == 11286) { //GLACIES
			currentPhase = 4;
		} else if(actorName.startsWith("nex")) {
			currentPhase = 0;
		}
		log.debug("Phase is now: " + currentPhase);
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned) {
		if(npcSpawned.getNpc().getName().toLowerCase().startsWith("nex")) {
			log.debug("Nex spawned");
			currentPhase = 0;
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
	}

	@Provides
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}
}
