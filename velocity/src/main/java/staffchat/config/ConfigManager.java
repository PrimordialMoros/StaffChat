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

package staffchat.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;

public final class ConfigManager {
  private final HoconConfigurationLoader loader;
  private final Logger logger;

  private CommentedConfigurationNode configRoot;

  public ConfigManager(@NonNull Logger logger, @NonNull String id) {
    this.logger = logger;
    Path path = Paths.get("plugins/" + id, "staffchat.conf");
    loader = HoconConfigurationLoader.builder().setPath(path).build();
    try {
      Files.createDirectories(path.getParent());
      configRoot = loader.load();
      setupDefaults();
    } catch (IOException e) {
      this.logger.warn(e.getMessage(), e);
    }
  }

  public void reload() {
    try {
      configRoot = loader.load();
    } catch (IOException e) {
      this.logger.warn(e.getMessage(), e);
    }
  }

  public void save() {
    try {
      this.logger.info("Saving StaffChat config");
      loader.save(configRoot);
    } catch (IOException e) {
      this.logger.warn(e.getMessage(), e);
    }
  }

  private void setupDefaults() {
    ConfigurationNode node = configRoot.getNode("channels", "staffchat");
    node.getNode("format").getString("&6[&bStaffChat&6] &b%player%: &6%message%");
    node.getNode("send-to-console").getBoolean(false);
    save();
  }

  public @NonNull CommentedConfigurationNode config() {
    return configRoot;
  }
}
