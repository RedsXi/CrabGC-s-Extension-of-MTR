package org.redsxi.mc.cgcem.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.main.Main;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import org.redsxi.mc.cgcem.block_entity.BlockEntityTicketBarrierPayDirect;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetPassCost extends ICommand {
    public LiteralArgumentBuilder<ServerCommandSource> getCommand() {
        return literal("setPassCost").then(argument("TicketBarrierPosition", BlockPosArgumentType.blockPos())
                .then(argument("Cost", IntegerArgumentType.integer(0, 2147483647))
                        .executes(context -> {
                            try {
                                int cost = context.getArgument("Cost", Integer.class);
                                ServerCommandSource source = context.getSource();
                                BlockPos blockPos = BlockPosArgumentType.getBlockPos(context, "TicketBarrierPosition");
                                BlockEntity entity = context.getSource().getWorld().getBlockEntity(blockPos);
                                if (entity instanceof BlockEntityTicketBarrierPayDirect) {
                                    ((BlockEntityTicketBarrierPayDirect) entity).setCost(cost);
                                    source.sendFeedback(new TranslatableText("command.cgcem.set_cost.success", blockPos, cost), true);
                                } else {
                                    source.sendError(new TranslatableText("command.cgcem.set_cost.fail"));
                                    Main a;
                                }
                            } catch (Exception e) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                e.printStackTrace(new PrintStream(baos));
                                String[] rawLines = new String(baos.toByteArray()).split("\r\n");
                                for(String eachLine : rawLines) {
                                    context.getSource().sendError(new LiteralText(eachLine));
                                }
                            }
                            return 1;
                        })));
    }
}
