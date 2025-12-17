package com.eeeab.eeeabsmobs.sever.commands;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.message.ShowHintMessage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.NetworkDirection;

public class CombatHintHudCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(
                Commands.literal("popup")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("entityType", ResourceArgument.resource(buildContext, Registries.ENTITY_TYPE))
                                .suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    Holder.Reference<EntityType<?>> entityTypeHolder = ResourceArgument.getSummonableEntityType(context, "entityType");
                                    ResourceLocation entityType = entityTypeHolder.key().location();
                                    sendHint(player, entityType, 0);
                                    context.getSource().sendSuccess(() -> Component.literal("success"), false);
                                    return 1;
                                })
                                .then(Commands.argument("level", IntegerArgumentType.integer(0, 2))
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            Holder.Reference<EntityType<?>> entityTypeHolder = ResourceArgument.getSummonableEntityType(context, "entityType");
                                            ResourceLocation entityType = entityTypeHolder.key().location();
                                            int level = IntegerArgumentType.getInteger(context, "level");
                                            sendHint(player, entityType, level);
                                            context.getSource().sendSuccess(() -> Component.literal("success"), false);
                                            return 1;
                                        })
                                )
                        )
        );
    }

    private static void sendHint(ServerPlayer player, ResourceLocation location, int level) {
        EEEABMobs.NETWORK.sendTo(new ShowHintMessage(location, level, true), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
