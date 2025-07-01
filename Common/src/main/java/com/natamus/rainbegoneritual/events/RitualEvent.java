package com.natamus.rainbegoneritual.events;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.WorldFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RitualEvent {
	private static Pair<Date, BlockPos> lastritual = null;
	
	public static boolean onClick(Level level, Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
		if (level.isClientSide) {
			return true;
		}
		
		if (!level.isRaining()) {
			return true;
		}
		
		if (!WorldFunctions.isOverworld(level)) {
			return true;
		}
		
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getItem().equals(Items.FLINT_AND_STEEL)) {
			Block clickblock = level.getBlockState(pos).getBlock();
			if (!(clickblock instanceof RotatedPillarBlock)) {
				return true;
			}
			
			BlockPos firepos = pos.above();
			Block prefireblock = level.getBlockState(firepos).getBlock();
			if (prefireblock.equals(Blocks.AIR)) {
				List<BlockPos> cauldronposses = new ArrayList<BlockPos>();

				for (BlockPos np : BlockPos.betweenClosed(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 1, pos.getY(), pos.getZ() + 1)) {
					if (level.getBlockState(np).getBlock() instanceof AbstractCauldronBlock) {
						cauldronposses.add(np.immutable());
					}
				}
				
				if (cauldronposses.size() < 4) {
					return true;
				}
				
				Vec3 firevec = new Vec3(firepos.getX(), firepos.getY(), firepos.getZ());
				
				lastritual = new Pair<Date, BlockPos>(new Date(), firepos.immutable());
				level.explode(null, level.damageSources().explosion((Explosion)null), null, firevec.x, firevec.y, firevec.z, 3.0f, false, Level.ExplosionInteraction.NONE);
				for (BlockPos cauldronpos : cauldronposses) {
					level.setBlock(cauldronpos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), 3);
				}
				
				level.setBlockAndUpdate(firepos, Blocks.AIR.defaultBlockState());
				level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				
				LevelData info = level.getLevelData();
				info.setRaining(false);
			}
		}
		
		return true;
	}
	
	public static float onExplosionDamage(Level level, Entity entity, DamageSource source, float damageAmount) {
		if (level.isClientSide) {
			return damageAmount;
		}
		
		if (!(entity instanceof Player)) {
			return damageAmount;
		}
		
		if (lastritual == null) {
			return damageAmount;
		}
		
		if (source.is(DamageTypes.EXPLOSION)) {
			Player player = (Player)entity;
			BlockPos ppos = player.blockPosition();
			
			Date now = new Date();
			Date lastdate = lastritual.getFirst();
			long ms = (now.getTime()-lastdate.getTime());
			if (ms > 1000) {
				lastritual = null;
				return damageAmount;
			}
			
			BlockPos ritualpos = lastritual.getSecond();
			if (ppos.closerThan(ritualpos, 10.0)) {
				return 0.0F;
			}
		}
		
		return damageAmount;
	}
}
