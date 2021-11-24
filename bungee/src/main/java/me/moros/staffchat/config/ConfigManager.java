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

package me.moros.staffchat.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import me.moros.staffchat.StaffChat;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ConfigManager {
  private final StaffChat plugin;

  private Configuration config;

  public ConfigManager(@NonNull StaffChat plugin) {
    this.plugin = plugin;
    setup();
    load();
  }

  private void setup() {
    File dir = plugin.getDataFolder();
    if (!dir.exists()) {
      dir.mkdir();
    }
    File configFile = new File(dir, "config.yml");
    if (!configFile.exists()) {
      try (InputStream in = plugin.resourceStream("config.yml")) {
        Files.copy(in, configFile.toPath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void load() {
    try {
      config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));
    } catch (IOException e) {
      plugin.logger().warn(e.getMessage());
    }
  }

  public void save() {
    try {
      ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(plugin.getDataFolder(), "config.yml"));
    } catch (IOException e) {
      plugin.logger().warn(e.getMessage());
    }
  }

  public @NonNull Configuration config() {
    return config;
  }
}
