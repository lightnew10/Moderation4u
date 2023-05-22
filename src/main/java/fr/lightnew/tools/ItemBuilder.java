package fr.lightnew.tools;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class ItemBuilder {
    ItemBuilder items;

    public ItemBuilder(ItemBuilder itemBuilder) {
        this.items = itemBuilder;
    }

    public ItemStack ItemBuilder(Material material, int amount, Enchantment[] enchantments, int[] enchantmentLevels, String name, String... lores) {
        return create(material, amount, enchantments, enchantmentLevels, name, lores);
    }

    public static ItemStack create(Material material, int amount, Enchantment[] enchantments, int[] enchantmentLevels, String name, String... lores) {
        if (material == null) {
            return null;
        } else {
            ItemStack itemStack = new ItemStack(material, amount);
            if (enchantments != null && enchantmentLevels != null && enchantments.length == enchantmentLevels.length) {
                for(int i = 0; i < enchantments.length; ++i) {
                    itemStack.addEnchantment(enchantments[i], enchantmentLevels[i]);
                }
            }

            if (name == null && (lores == null || lores.length < 1)) {
                return itemStack;
            } else {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (name != null) {
                    itemMeta.setDisplayName(name);
                }

                if (lores != null) {
                    itemMeta.setLore(Arrays.asList(lores));
                }

                itemStack.setItemMeta(itemMeta);
                return itemStack;
            }
        }
    }

    public static ItemStack create(Material material, int amount, Enchantment[] enchantments, int[] enchantmentLevels, String name, List<String> lores) {
        if (material == null) {
            return null;
        } else {
            ItemStack itemStack = new ItemStack(material, amount);
            if (enchantments != null && enchantmentLevels != null && enchantments.length == enchantmentLevels.length) {
                for(int i = 0; i < enchantments.length; ++i) {
                    itemStack.addEnchantment(enchantments[i], enchantmentLevels[i]);
                }
            }

            if (name == null && (lores == null || lores.size() < 1)) {
                return itemStack;
            } else {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (name != null) {
                    itemMeta.setDisplayName(name);
                }

                if (lores != null) {
                    itemMeta.setLore(lores);
                }

                itemStack.setItemMeta(itemMeta);
                return itemStack;
            }
        }
    }

    public static ItemStack create(Material material, int amount, String name, String... lores) {
        return create(material, amount, (Enchantment[])null, (int[])null, name, lores);
    }

    public static ItemStack create(Material material, int amount, String name, List<String> lores) {
        return create(material, amount, (Enchantment[])null, (int[])null, name, lores);
    }

    public static ItemStack create(Material material, int amount, String name) {
        return create(material, amount, (Enchantment[])null, (int[])null, name, (String[])null);
    }

    public static ItemStack skull(int amount, String name, String owner, String... lores) {
        if (name != null && lores != null) {
            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, amount, (short)3);
            SkullMeta itemMeta = (SkullMeta)itemStack.getItemMeta();
            itemMeta.setDisplayName(name);
            itemMeta.setLore(Arrays.asList(lores));
            itemMeta.setOwner(owner);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        } else {
            return null;
        }
    }

    public static ItemStack skullCustom(int amount, String name, String value, String... lores) {
        if (name != null && lores != null) {
            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, amount, (short)3);
            SkullMeta itemMeta = (SkullMeta)itemStack.getItemMeta();
            if (value != null) {
                GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
                profile.getProperties().put("textures", new Property("textures", new String(value)));
                Field profileField = null;

                try {
                    profileField = itemMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(itemMeta, profile);
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException var9) {
                    var9.printStackTrace();
                }
            }

            itemMeta.setDisplayName(name);
            itemMeta.setLore(Arrays.asList(lores));
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        } else {
            return null;
        }
    }
}
