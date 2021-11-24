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

package me.moros.staffchat.command;

import java.util.regex.Matcher;

import me.moros.staffchat.StaffChat;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.checkerframework.checker.nullness.qual.NonNull;

public class StaffChatCommand extends Command {
  private final StaffChat plugin;

  public StaffChatCommand(@NonNull StaffChat plugin) {
    super("me/moros/staffchat", "staffchat.send", "sc");
    this.plugin = plugin;
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (args.length <= 0) {
      return;
    }
    if (sender instanceof ProxiedPlayer player) {
      plugin.audiences().filter(this::canView)
        .sendMessage(Identity.identity(player.getUniqueId()), formatMessage(player, String.join(" ", args)));
    }
  }

  private Component formatMessage(CommandSender sender, String message) {
    String text = plugin.configManager().config().getString("format", "&6[&bStaffChat&6] &b%player%: &6%message%")
      .replaceFirst("%player%", sender.getName()).replaceFirst("%message%", Matcher.quoteReplacement(message));
    return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
  }

  private boolean canView(CommandSender sender) {
    if (!sender.hasPermission("staffchat.send") && !sender.hasPermission("staffchat.receive")) {
      return false;
    }
    if (plugin.configManager().config().getBoolean("send-to-console", false)) {
      return true;
    }
    return sender instanceof ProxiedPlayer;
  }
}
