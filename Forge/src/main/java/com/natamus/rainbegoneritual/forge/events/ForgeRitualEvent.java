package com.natamus.rainbegoneritual.forge.events;

import com.natamus.rainbegoneritual.events.RitualEvent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

import java.lang.invoke.MethodHandles;

public class ForgeRitualEvent {
	public static void registerEventsInBus() {
		BusGroup.DEFAULT.register(MethodHandles.lookup(), ForgeRitualEvent.class);
	}

	@SubscribeEvent
	public static void onClick(PlayerInteractEvent.RightClickBlock e) {
		RitualEvent.onClick(e.getLevel(), e.getEntity(), e.getHand(), e.getPos(), e.getHitVec());
	}
	
	@SubscribeEvent
	public static boolean onExplosionDamage(LivingHurtEvent e) {
		Entity entity = e.getEntity();
		if (RitualEvent.onExplosionDamage(entity.level(), entity, e.getSource(), e.getAmount()) == 0.0F) {
			return true;
		}
		return false;
	}
}
