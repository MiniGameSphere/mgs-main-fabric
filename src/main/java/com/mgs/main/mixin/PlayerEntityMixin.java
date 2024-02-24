package com.mgs.main.mixin;

import com.mgs.main.ducks.PlayerEntityDuckInterface;
import com.mgs.main.games.MiniGame;
import com.mgs.main.games.PlayerLists;
import com.mgs.main.games.players.Party;
import com.mgs.main.games.players.Team;
import com.mgs.main.gui.Gui;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityDuckInterface {
	@Unique
	Team mgs$team = new Team();
	@Unique
	MiniGame mgs$miniGame = null;
	
	@Unique
	Gui mgs$currentGui = null;
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public Team mgs$getTeam() {
		return mgs$team;
	}
	
	public void mgs$setTeam(Team team) {
		mgs$team = team;
	}
	
	public MiniGame mgs$getMiniGame() {
		return mgs$miniGame;
	}
	
	public void mgs$setMiniGame(MiniGame miniGame) {
		mgs$miniGame = miniGame;
	}
	
	public Party mgs$getParty() {
		return mgs$team.getParty();
	}
	
	public void mgs$remove() {
		mgs$team.removePlayer((PlayerEntity) (Object) this);
	}
	
	public Gui mgs$getCurrentGui() {
		return mgs$currentGui;
	}
	
	public void mgs$setCurrentGui(Gui gui) {
		mgs$currentGui = gui;
	}
	
	@Inject(method="<init>", at = @At("TAIL"))
	public void constructorInject(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
		mgs$team.addPlayer((PlayerEntity) (Object) this);
	}
	
}
