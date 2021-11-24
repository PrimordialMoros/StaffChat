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

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.Tristate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import staffchat.StaffChat;

public final class StaffChatReloadCommand {
  private final StaffChat staffChat;

  public StaffChatReloadCommand(@NonNull StaffChat staffChat) {
    this.staffChat = staffChat;

    LiteralCommandNode<CommandSource> staffChatReloadCommand = LiteralArgumentBuilder
      .<CommandSource>literal("staffchatreload")
      .executes(context -> onCommand(context.getSource())).build();

    BrigadierCommand cmd = new BrigadierCommand(staffChatReloadCommand);

    CommandMeta meta = staffChat.proxy().getCommandManager().metaBuilder(cmd).aliases("screload").build();
    staffChat.proxy().getCommandManager().register(meta, cmd);
  }

  private int onCommand(CommandSource sender) {
    if (sender.getPermissionValue("staffchat.reload") == Tristate.FALSE) {
      sender.sendMessage(Component.text("You do not have permission to perform this command!", NamedTextColor.RED));
      return 0;
    }
    staffChat.configManager().reload();
    sender.sendMessage(Component.text("StaffChat config has been reloaded"));
    return 0;
  }
}
