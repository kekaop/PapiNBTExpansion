package dev.orven.papinbt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTType;
import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public final class NbtExpansion extends PlaceholderExpansion {

    private static final Pattern NAMED_SLOT =
            Pattern.compile("^(mainhand|offhand|helmet|chestplate|leggings|boots)_(.+)$");
    private static final Pattern INDEX_SLOT = Pattern.compile("^slot:(\\d+)_(.+)$");

    @Override
    public @NotNull String getIdentifier() {
        return "nbt";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Orven";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @NotNull String getRequiredPlugin() {
        // External expansion depends on NBTAPI plugin being present
        return "NBTAPI";
    }

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin(getRequiredPlugin()) != null;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null || params.isEmpty()) {
            return "";
        }

        String slotKey;
        String tagPath;
        Matcher m = INDEX_SLOT.matcher(params);
        if (m.matches()) {
            int index;
            try {
                index = Integer.parseInt(m.group(1));
            } catch (NumberFormatException ignored) {
                return "";
            }
            if (index < 0 || index > 35) {
                return "";
            }
            slotKey = "slot:" + index;
            tagPath = m.group(2);
        } else {
            m = NAMED_SLOT.matcher(params);
            if (!m.matches()) {
                return "";
            }
            slotKey = m.group(1);
            tagPath = m.group(2);
        }

        if (tagPath.isEmpty()) {
            return "";
        }

        ItemStack stack = resolveItem(player.getInventory(), slotKey);
        if (stack == null || stack.getType().isAir()) {
            return "";
        }

        return NBT.get(stack, (ReadableItemNBT nbt) -> readSupportedTagAsString(nbt, tagPath));
    }

    private static @Nullable ItemStack resolveItem(PlayerInventory inv, String slotKey) {
        return switch (slotKey) {
            case "mainhand" -> inv.getItemInMainHand();
            case "offhand" -> inv.getItemInOffHand();
            case "helmet" -> inv.getHelmet();
            case "chestplate" -> inv.getChestplate();
            case "leggings" -> inv.getLeggings();
            case "boots" -> inv.getBoots();
            default -> {
                if (slotKey.startsWith("slot:")) {
                    int i = Integer.parseInt(slotKey.substring("slot:".length()));
                    yield inv.getItem(i);
                }
                yield null;
            }
        };
    }

    private static String readSupportedTagAsString(ReadableNBT root, String fullPath) {
        int sep = indexOfLastPathSeparator(fullPath);
        ReadableNBT parent;
        String leafKey;
        if (sep < 0) {
            parent = root;
            leafKey = unescapeLeafKey(fullPath);
        } else {
            String compoundPath = fullPath.substring(0, sep);
            leafKey = unescapeLeafKey(fullPath.substring(sep + 1));
            parent = root.resolveCompound(compoundPath);
            if (parent == null) {
                return "";
            }
        }

        if (!parent.hasTag(leafKey)) {
            return "";
        }

        NBTType type = parent.getType(leafKey);
        if (type == null) {
            return "";
        }

        return switch (type) {
            case NBTTagByte -> String.valueOf(parent.getByte(leafKey));
            case NBTTagShort -> String.valueOf(parent.getShort(leafKey));
            case NBTTagInt -> String.valueOf(parent.getInteger(leafKey));
            case NBTTagLong -> String.valueOf(parent.getLong(leafKey));
            case NBTTagFloat -> String.valueOf(parent.getFloat(leafKey));
            case NBTTagDouble -> String.valueOf(parent.getDouble(leafKey));
            case NBTTagString -> parent.getString(leafKey);
            default -> "";
        };
    }

    /**
     * Last '.' that separates compound path from leaf key, ignoring dots escaped as '\.' per Item-NBT-API.
     */
    private static int indexOfLastPathSeparator(String path) {
        for (int i = path.length() - 1; i >= 0; i--) {
            if (path.charAt(i) == '.') {
                int backslashes = 0;
                for (int j = i - 1; j >= 0 && path.charAt(j) == '\\'; j--) {
                    backslashes++;
                }
                if (backslashes % 2 == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static String unescapeLeafKey(String leaf) {
        return leaf.replace("\\.", ".");
    }
}
