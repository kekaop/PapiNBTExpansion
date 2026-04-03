## PapiNBTExpansion

Небольшое **расширение для PlaceholderAPI**, которое читает NBT‑теги из предметов игрока через [Item-NBT-API](https://github.com/tr7zw/Item-NBT-API) (плагин **NBTAPI** на сервере).

### Требования

| Компонент | Версия |
|-----------|--------|
| Java | **21** |
| Paper | **1.21.4** |
| PlaceholderAPI | **2.11.x** (обязателен) |
| NBTAPI | Item-NBT-API (плагин `NBTAPI` установлен на сервере) |

### Формат плейсхолдера

```text
%nbt_<слот>_<тег>%
```

После идентификатора `nbt_` указываются слот и путь к тегу (через `_` между слотом и началом пути; вложенность внутри тега — точками, как в Item-NBT-API).

#### Слоты

| Значение | Описание |
|----------|----------|
| `mainhand` | Основная рука |
| `offhand` | Вторая рука |
| `helmet`, `chestplate`, `leggings`, `boots` | Броня |
| `slot:0` … `slot:35` | Слоты инвентаря (`PlayerInventory#getItem`) |

#### Поведение

- Поддерживаемые типы NBT: **BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, STRING** — выводятся строкой.
- Нет тега, пустой слот, неверный слот или формат — пустая строка `""`.

#### Примеры

```text
%nbt_mainhand_PICKAXE_POWER%
%nbt_helmet_ARMOR_TIER%
%nbt_slot:14_ITEM_LEVEL%
%nbt_offhand_SOME_STRING_TAG%
%nbt_mainhand_stats.level%
```

### Установка как внешнего расширения PlaceholderAPI

1. Установите на сервер плагины **PlaceholderAPI** и **NBTAPI** (Item-NBT-API).
2. Соберите JAR (`mvn clean package` с JDK 21).
3. Поместите `papi-nbt-expansion-<version>.jar` **не** в `plugins/`, а в:
   - `plugins/PlaceholderAPI/expansions/`
4. Выполните `/papi reload` или перезапустите сервер.

Проверить работу можно, например, так:

```text
/papi parse me %nbt_mainhand_<ваш_тег>%
```

Идентификатор расширения: `nbt`.

### Сборка

Нужен **JDK 21** (Maven должен использовать его, например `JAVA_HOME` на JDK 21):

```bash
mvn clean package
```

Готовый файл: `target/papi-nbt-expansion-<version>.jar`.
