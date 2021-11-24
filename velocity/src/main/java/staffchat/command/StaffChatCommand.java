/*
 *   Copyright 2020-2021 Moros <https://github.com/PrimordialMoros>
 *
 *    This file is part of StaffChat.
 *
 *    StaffChat is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    StaffChat is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with StaffChat.  If not, see <https://www.gnu.org/licenses/>.
 */

package staffchat.command;

import java.util.regex.Matcher;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import staffchat.StaffChat;

public final class StaffChatCommand {
  private final StaffChat staffChat;

  public StaffChatCommand(@NonNull StaffChat staffChat) {
    this.staffChat = staffChat;

    LiteralCommandNode<CommandSource> staffChatCommand = LiteralArgumentBuilder
      .<CommandSource>literal("staffchat")
      .executes(context -> {
        context.getSource().sendMessage(Component.text("Usage: /staffchat <msg>", NamedTextColor.GOLD));
        return 0;
      }).build();

    ArgumentCommandNode<CommandSource, String> messageNode = RequiredArgumentBuilder
      .<CommandSource, String>argument("message", StringArgumentType.greedyString())
      .executes(context -> onCommand(context.getSource(), context.getArgument("message", String.class))).build();

    staffChatCommand.addChild(messageNode);
    BrigadierCommand cmd = new BrigadierCommand(staffChatCommand);

    CommandMeta meta = staffChat.proxy().getCommandManager().metaBuilder(cmd).aliases("sc").build();
    staffChat.proxy().getCommandManager().register(meta, cmd);
  }

  private int onCommand(CommandSource sender, String message) {
    if (sender.getPermissionValue("staffchat.send") == Tristate.FALSE) {
      sender.sendMessage(Component.text("You do not have permission to perform this command!", NamedTextColor.RED));
      return 0;
    }
    if (sender instanceof Player player) {
      Component componentMessage = formatMessage(player, message);
      if (staffChat.configManager().config().getNode("send-to-console").getBoolean(false)) {
        staffChat.proxy().getConsoleCommandSource().sendMessage(componentMessage);
      }
      staffChat.proxy().getAllPlayers().stream()
        .filter(this::canView).collect(Audience.toAudience()).sendMessage(player, componentMessage);
    }
    return 0;
  }

  private Component formatMessage(Player sender, String message) {
    String text = staffChat.configManager().config().getNode("format")
      .getString("&6[&bStaffChat&6] &b%player%: &6%message%")
      .replaceFirst("%player%", sender.getUsername())
      .replaceFirst("%message%", Matcher.quoteReplacement(message));
    return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
  }

  private boolean canView(CommandSource user) {
    return user.getPermissionValue("staffchat.send") != Tristate.FALSE ||
      user.getPermissionValue("staffchat.receive") != Tristate.FALSE;
  }
}
