package org.redsxi.mc.cgcem.mixin;

import com.mojang.logging.LogUtils;
import mtr.data.Station;
import mtr.data.TicketSystem;
import mtr.mappings.Text;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.redsxi.mc.cgcem.block_entity.BlockEntityTicketBarrierBase;
import org.redsxi.mc.cgcem.util.PosUtil;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = TicketSystem.class, remap = false)
public class TicketSystemMixin {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static int invokeDecodeZone(int entryZone) {
        throw new AssertionError("Mixin doesn't work");
    }

    private static boolean invokeIsConcessionary(PlayerEntity entity) {
        throw new AssertionError("Mixin doesn't work");
    }

    /**
     * @author RedsXi
     * @reason For cost multiplying
     */
    private static boolean onExit(Station station, PlayerEntity player, ScoreboardPlayerScore balanceScore, ScoreboardPlayerScore entryZoneScore, boolean remindIfNoRecord) {
        BlockPos barrierPos = new BlockPos(PosUtil.INSTANCE.roundVec(player.getPos()));
        World world = player.getWorld();
        BlockEntity entity = world.getBlockEntity(barrierPos);
        double costMultiplier = 1;
        if(entity instanceof BlockEntityTicketBarrierBase) {
            costMultiplier = ((BlockEntityTicketBarrierBase) entity).getCostMultiplier();
        } else {
            LOGGER.warn("Barrier haven't a property called CostMultiplier. Seems weird");
        }
        int entryZone = entryZoneScore.getScore();
        int fare = 2 + Math.abs(station.zone - invokeDecodeZone(entryZone));
        int finalFare = entryZone != 0 ? (int)Math.round((invokeIsConcessionary(player) ? (int) Math.ceil((double) ((float) fare / 2.0F)) : fare) * costMultiplier) : 500;
        if (entryZone == 0 && remindIfNoRecord) {
            player.sendMessage(Text.translatable("gui.mtr.already_exited"), true);
            return false;
        } else {
            entryZoneScore.setScore(0);
            balanceScore.incrementScore(-finalFare);
            player.sendMessage(Text.translatable("gui.mtr.exit_barrier", new Object[]{String.format("%s (%s)", station.name.replace('|', ' '), station.zone), finalFare, balanceScore.getScore()}), true);
            return true;
        }
    }
}
