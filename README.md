# PapiNBTExpansion

Плагин для **Paper**, который регистрирует расширение **PlaceholderAPI** для чтения NBT-тегов из предметов игрока через [Item-NBT-API](https://github.com/tr7zw/Item-NBT-API) (плагин **NBTAPI** на сервере).

## Требования

| Компонент | Версия |
|-----------|--------|
| Java | **21** |
| Paper | **1.21.4** |
| PlaceholderAPI | **2.11.x** (обязателен, `depend`) |
| NBTAPI | Item-NBT-API на сервере (обязателен для работы плейсхолдеров) |

## Плейсхолдер

```text
%nbt_<слот>_<тег>%
```

После идентификатора `nbt_` указываются слот и путь к тегу (через `_` между слотом и началом пути; вложенность внутри тега — точками, как в Item-NBT-API).

### Слоты

| Значение | Описание |
|----------|----------|
| `mainhand` | Основная рука |
| `offhand` | Вторая рука |
| `helmet`, `chestplate`, `leggings`, `boots` | Броня |
| `slot:0` … `slot:35` | Слоты инвентаря (`PlayerInventory#getItem`) |

### Поведение

- Поддерживаемые типы NBT: **BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, STRING** — выводятся строкой.
- Нет тега, пустой слот, неверный слот или формат — пустая строка `""`.

### Примеры

```text
%nbt_mainhand_PICKAXE_POWER%
%nbt_helmet_ARMOR_TIER%
%nbt_slot:14_ITEM_LEVEL%
%nbt_offhand_SOME_STRING_TAG%
%nbt_mainhand_stats.level%
```

## Установка

1. Установите на сервер **PlaceholderAPI** и **NBTAPI** (Item-NBT-API).
2. Положите собранный JAR в папку `plugins/`.
3. Перезапустите сервер или выполните `/papi reload` после полной загрузки.

Проверка: `/papi parse me %nbt_mainhand_<ваш_тег>%` или `/papi list` (должен быть идентификатор `nbt`).

## Сборка

Нужен **JDK 21** (Maven должен использовать его, например `JAVA_HOME` на JDK 21):

```bash
mvn clean package
```

Готовый файл: `target/papi-nbt-expansion-<version>.jar`.

## Лицензия

Укажите лицензию по желанию автору проекта.
