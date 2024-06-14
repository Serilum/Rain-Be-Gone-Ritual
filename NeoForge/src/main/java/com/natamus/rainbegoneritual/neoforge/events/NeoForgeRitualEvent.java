package com.natamus.rainbegoneritual.neoforge.events;

import com.natamus.rainbegoneritual.events.RitualEvent;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class NeoForgeRitualEvent {
	@SubscribeEvent
	public static void onClick(PlayerInteractEvent.RightClickBlock e) {
		RitualEvent.onClick(e.getLevel(), e.getEntity(), e.getHand(), e.getPos(), e.getHitVec());
	}
	
	@SubscribeEvent
	public static void onExplosionDamage(LivingHurtEvent e) {
		Entity entity = e.getEntity();
		if (RitualEvent.onExplosionDamage(entity.level(), entity, e.getSource(), e.getAmount()) == 0.0F) {
			e.setCanceled(true);
		}
	}
}
