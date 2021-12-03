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

package me.moros.staffchat;

import java.io.InputStream;
import java.util.Collection;

import me.moros.staffchat.command.StaffChatCommand;
import me.moros.staffchat.command.StaffChatReloadCommand;
import me.moros.staffchat.config.ConfigManager;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaffChat extends Plugin implements Listener {
  private Logger logger;

  private String author;
  private String version;

  private ConfigManager configManager;
  private BungeeAudiences audiences;

  @Override
  public void onEnable() {
    logger = LoggerFactory.getLogger(getClass().getSimpleName());

    author = getDescription().getAuthor();
    version = getDescription().getVersion();
    configManager = new ConfigManager(this);

    audiences = BungeeAudiences.create(this);

    getProxy().getPluginManager().registerCommand(this, new StaffChatReloadCommand(this));
    for (String s : configManager.config().getSection("channels").getKeys()) {
      Collection<String> aliases = configManager.config().getStringList("channels." + s + ".aliases");
      getProxy().getPluginManager().registerCommand(this, new StaffChatCommand(this, s, aliases.toArray(new String[0])));
    }
    configManager.save();
  }

  @Override
  public void onDisable() {
    if (audiences != null) {
      audiences.close();
      audiences = null;
    }
  }

  public @NonNull InputStream resourceStream(@NonNull String name) {
    return getResourceAsStream(name);
  }

  public @NonNull String author() {
    return author;
  }

  public @NonNull String version() {
    return version;
  }

  public @NonNull Logger logger() {
    return logger;
  }

  public @NonNull ConfigManager configManager() {
    return configManager;
  }

  public @NonNull BungeeAudiences audiences() {
    if (audiences == null) {
      throw new IllegalStateException("Cannot retrieve audience provider while plugin is not enabled");
    }
    return audiences;
  }
}
