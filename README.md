## PapiNBTExpansion

A small **PlaceholderAPI expansion** that reads NBT tags from player items using [Item-NBT-API](https://github.com/tr7zw/Item-NBT-API) (the **NBTAPI** plugin on the server).

### Requirements

| Component | Version |
|-----------|---------|
| Java | **21** |
| Paper | **1.21.4** |
| PlaceholderAPI | **2.11.x** (required) |
| NBTAPI | Item-NBT-API (the `NBTAPI` plugin must be installed on the server) |

### Placeholder Format
```text
%nbt_<slot>_<tag>%
```

After the `nbt_` identifier, specify the slot and the tag path (separated by `_` between the slot and the start of the path; nesting within the tag uses dots, as in Item-NBT-API).

#### Slots

| Value | Description |
|-------|-------------|
| `mainhand` | Main hand |
| `offhand` | Off hand |
| `helmet`, `chestplate`, `leggings`, `boots` | Armor slots |
| `slot:0` ... `slot:35` | Inventory slots (`PlayerInventory#getItem`) |

#### Behavior

- Supported NBT types: **BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, STRING** — returned as a string.
- Missing tag, empty slot, invalid slot or invalid format — returns an empty string `""`.

#### Examples
```text
%nbt_mainhand_PICKAXE_POWER%
%nbt_helmet_ARMOR_TIER%
%nbt_slot:14_ITEM_LEVEL%
%nbt_offhand_SOME_STRING_TAG%
%nbt_mainhand_stats.level%
```

### Installation as an External PlaceholderAPI Expansion

1. Install **PlaceholderAPI** and **NBTAPI** (Item-NBT-API) on your server.
2. Build the JAR (`mvn clean package` with JDK 21).
3. Place `papi-nbt-expansion-<version>.jar` **not** in `plugins/`, but in:
   - `plugins/PlaceholderAPI/expansions/`
4. Run `/papi reload` or restart the server.

You can verify it works like this:
```text
/papi parse me %nbt_mainhand_<your_tag>%
```

Expansion identifier: `nbt`.

### Building

**JDK 21** is required (Maven must use it, e.g. set `JAVA_HOME` to JDK 21):
```bash
mvn clean package
```

Output file: `target/papi-nbt-expansion-<version>.jar`.
