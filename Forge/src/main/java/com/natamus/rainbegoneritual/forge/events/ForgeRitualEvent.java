package com.natamus.rainbegoneritual.forge.events;

import com.natamus.rainbegoneritual.events.RitualEvent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ForgeRitualEvent {
	@SubscribeEvent
	public void onClick(PlayerInteractEvent.RightClickBlock e) {
		RitualEvent.onClick(e.getLevel(), e.getEntity(), e.getHand(), e.getPos(), e.getHitVec());
	}
	
	@SubscribeEvent
	public void onExplosionDamage(LivingHurtEvent e) {
		Entity entity = e.getEntity();
		if (RitualEvent.onExplosionDamage(entity.level(), entity, e.getSource(), e.getAmount()) == 0.0F) {
			e.setCanceled(true);
		}
	}
}
