package org.redsxi.mc.cgcem;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static mtr.data.TicketSystem.BALANCE_OBJECTIVE;

public class PassThroughManager {
    public static boolean passThrough(
                                    World world,
                                   BlockPos pos,
                                   PlayerEntity player,
                                   SoundEvent entrySound,
                                   SoundEvent failSound,
                                   int fair
    ) {
        addObjectivesIfMissing(world);

        final ScoreboardPlayerScore balanceScore = getPlayerScore(world, player, BALANCE_OBJECTIVE);

        if(balanceScore.getScore() >= fair) {
            balanceScore.incrementScore(-fair);
            player.sendMessage(new TranslatableText("gui.cgcem.enter_barrier", fair), true);
            world.playSound(null, pos, entrySound, SoundCategory.BLOCKS, 1, 1);
            return true;
        } else {
            player.sendMessage(new TranslatableText("gui.mtr.insufficient_balance", balanceScore.getScore()), true);
            if (failSound != null) {
                world.playSound(null, pos, failSound, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return false;
        }
    }

    public static void addObjectivesIfMissing(World world) {
        try {
            world.getScoreboard().addObjective(BALANCE_OBJECTIVE, ScoreboardCriterion.DUMMY, new LiteralText("Balance"), ScoreboardCriterion.RenderType.INTEGER);
        } catch (Exception ignored) {
        }
    }

    public static ScoreboardPlayerScore getPlayerScore(World world, PlayerEntity player, String objectiveName) {
        return world.getScoreboard().getPlayerScore(player.getGameProfile().getName(), world.getScoreboard().getNullableObjective(objectiveName));
    }
}
