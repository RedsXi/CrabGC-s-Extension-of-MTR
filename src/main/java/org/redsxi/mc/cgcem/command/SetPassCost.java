package org.redsxi.mc.cgcem.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.BiConsumer;
import org.redsxi.mc.cgcem.block_entity.BlockEntityTicketBarrierPayDirect;
import org.redsxi.mc.cgcem.util.AreaUtil;
import org.redsxi.mc.cgcem.util.BlockPosUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetPassCost extends ICommand {
    public LiteralArgumentBuilder<ServerCommandSource> getCommand() {
        return literal("setPassCost").then(
                argument("TicketBarrierPosition", BlockPosArgumentType.blockPos())
                    .then(argument("Cost", IntegerArgumentType.integer(0, 2147483647))
                        .executes(context -> {
                            try {
                                setPassCost(
                                        context.getSource().getWorld(),
                                        BlockPosArgumentType.getBlockPos(context, "TicketBarrierPosition"),
                                        IntegerArgumentType.getInteger(context, "Cost"),
                                        context.getSource()::sendFeedback,
                                        context.getSource()::sendError,
                                        true
                                );
                            } catch (Exception ignored) {

                            }
                            return 1;
                        }))
                    .then(argument("TicketBarrierPosition2", BlockPosArgumentType.blockPos())
                        .executes(context -> {
                            try {
                                int count = AreaUtil.area3d(
                                        BlockPosArgumentType.getBlockPos(context, "TicketBarrierPosition"),
                                        BlockPosArgumentType.getBlockPos(context, "TicketBarrierPosition2"),
                                        (pos) -> setPassCost(
                                                context.getSource().getWorld(),
                                                pos,
                                                IntegerArgumentType.getInteger(context, "Cost"),
                                                context.getSource()::sendFeedback,
                                                context.getSource()::sendError,
                                                false
                                        )
                                );
                                if(count == 0) {
                                    context.getSource().sendError(new TranslatableText("command.cgcem.set_cost.area.none"));
                                } else {
                                    context.getSource().sendFeedback(new TranslatableText("command.cgcem.set_cost.area.success"), true);
                                }
                            } catch (Exception ignored) {
                                context.getSource().sendError(new TranslatableText("command.cgcem.exception"));
                            }
                            return 1;
                        })));
    }

    private boolean setPassCost(World world, BlockPos pos, int cost, BiConsumer<Text, Boolean> feedbackFunc, Consumer<Text> errorFunc, boolean showFeedback) {
        try {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof BlockEntityTicketBarrierPayDirect) {
                ((BlockEntityTicketBarrierPayDirect) entity).setCost(cost);
                if(showFeedback) feedbackFunc.accept(new TranslatableText("command.cgcem.set_cost.success", BlockPosUtils.Companion.toString(pos), cost), true);
                return true;
            } else {
                if(showFeedback) errorFunc.accept(new TranslatableText("command.cgcem.set_cost.fail"));
                return false;
            }
        } catch (Exception e) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(baos));
            String[] rawLines = baos.toString(StandardCharsets.UTF_8).split("\r\n");
            for(String eachLine : rawLines) {
                errorFunc.accept(new LiteralText(eachLine));
            }
            throw new IllegalStateException(e);
        }
    }
}
