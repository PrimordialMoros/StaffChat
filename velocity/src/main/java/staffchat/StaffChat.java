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

package staffchat;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ProxyServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import staffchat.command.StaffChatCommand;
import staffchat.command.StaffChatReloadCommand;
import staffchat.config.ConfigManager;

@Plugin(
  id = "staffchat", name = "StaffChat", version = "2.0.0-SNAPSHOT",
  url = "https://github.com/PrimordialMoros/StaffChat", description = "Staff channel plugin.", authors = {"Moros"}
)
public class StaffChat {
  private final ProxyServer server;

  private final ConfigManager configManager;

  @Inject
  public StaffChat(@NonNull ProxyServer server, @NonNull Logger logger, @NonNull PluginDescription description) {
    this.server = server;
    this.configManager = new ConfigManager(logger, description.getId());
    new StaffChatCommand(this);
    new StaffChatReloadCommand(this);
  }

  public @NonNull ProxyServer proxy() {
    return server;
  }

  public @NonNull ConfigManager configManager() {
    return configManager;
  }
}
