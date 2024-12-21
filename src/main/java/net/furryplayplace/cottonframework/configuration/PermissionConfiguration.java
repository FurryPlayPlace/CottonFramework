/*
---------------------------------------------------------------------------------
File Name : PermissionConfiguration

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 21.12.2024
Last Modified : 21.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.configuration;

import com.mojang.authlib.GameProfile;
import net.furryplayplace.cottonframework.CottonFramework;
import net.furryplayplace.cottonframework.api.configuration.Configuration;
import net.furryplayplace.cottonframework.api.configuration.file.YamlConfiguration;
import net.furryplayplace.cottonframework.api.permissions.v1.Permission;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("All")
public class PermissionConfiguration {
    private final File configFile;
    private YamlConfiguration permissions;
    private Configuration groups;

    public boolean isGroup;
    public String name;
    public String uuid;
    public List<String> parentGroups;

    public List<String> permissionsList;
    public List<String> negPermissions;

    public PermissionConfiguration(File configFile) {
        this.configFile = configFile;
        this.permissions = YamlConfiguration.loadConfiguration(configFile);

        this.isGroup = this.permissions.getBoolean("isGroup", false);
        this.name = this.permissions.getString("name");
        this.uuid = this.permissions.getString("uuid");
        this.parentGroups = this.permissions.getStringList("parentGroups");
        this.permissionsList = this.permissions.getStringList("permissionsList");
        this.negPermissions = this.permissions.getStringList("negPermissions");
    }

    public PermissionConfiguration(ServerPlayerEntity plr) throws IOException {
        this( new File(CottonFramework.getInstance().getPermissionsFolder(), plr.getUuid().toString() + ".yml") );

        this.name = plr.getName().getString();
        this.uuid = plr.getUuid().toString();
        this.isGroup = false;
        this.parentGroups.add("default");
        this.permissionsList.add("example.permission");
        this.write();
    }

    public PermissionConfiguration(String group) throws IOException {
        this( new File(CottonFramework.getInstance().getGroupsFolder(), group + ".yml") );

        this.name = group;
        this.uuid = null;
        this.isGroup = true;
        if (!group.equalsIgnoreCase("default"))
            this.parentGroups.add("default");
        this.permissionsList.add("example.permission");
        this.write();
    }

    public PermissionConfiguration(GameProfile profile) throws IOException {
        this( new File(CottonFramework.getInstance().getPermissionsFolder(), profile.getId().toString() + ".yml") );

        this.name = profile.getName();
        this.uuid = profile.getId().toString();
        this.isGroup = false;
        this.parentGroups.add("default");
        this.permissionsList.add("example.permission");
        this.write();
    }

    public Configuration getPermissions() {
        return permissions;
    }

    public void setPermission(Permission id, boolean value) {
        String str = id.getPermissionAsString();
        this.setPermission(str, value);
    }

    public void setPermission(String str, boolean value) {
        if (!value) {
            if (permissions.contains(str))
                permissionsList.remove(str);
            else negPermissions.add(str);
        } else {
            permissionsList.add(str);
        }
        this.save();
    }

    public boolean hasPermission(String permission) {
        if (negPermissions.contains(permission))
            return false;
        if (permissions.contains(permission))
            return true;

        for (String s : parentGroups)
            if (CottonFramework.groups.get(s).hasPermission(permission))
                return true;

        return false;
    }

    public void save() {
        try {
            write();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write() throws IOException {
        this.permissions.set("name", this.name);
        this.permissions.set("uuid", this.uuid);
        this.permissions.set("isGroup", this.isGroup);
        this.permissions.set("parentGroups", this.parentGroups);
        this.permissions.set("permissions", this.permissionsList);
        this.permissions.set("negPermissions", this.negPermissions);
        this.permissions.save(this.configFile);
    }
}