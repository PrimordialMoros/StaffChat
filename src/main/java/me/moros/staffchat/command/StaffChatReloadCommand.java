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

import me.moros.staffchat.StaffChat;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import org.checkerframework.checker.nullness.qual.NonNull;

public class StaffChatReloadCommand extends Command {
  private final StaffChat plugin;

  public StaffChatReloadCommand(@NonNull StaffChat plugin) {
    super("staffchatreload", "staffchat.reload", "screload");
    this.plugin = plugin;
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    plugin.configManager().load();
    plugin.audiences().sender(sender).sendMessage(Component.text("StaffChat config has been reloaded"));
  }
}
