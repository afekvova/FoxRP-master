package me.afek.foxrp.config;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Settings extends Config {

    @Ignore
    public static final Settings IMP = new Settings();
    @Ignore
    @Final
    public final String HELP = "afek.ru, t.me/afekvova, vk.com/afekvova, github.com/afekvova";

    @Comment({" -------------------------------------------------- #",
            "   Copyright © Dizzenzio studio, 2020-2022          #",
            "   Сделать заказ: vk.com/dizzenziostudio            #",
            " -------------------------------------------------- #"})
    public String PREFIX = "&f[&6FoxRP&f] ";
    public String PERMISSION_MSG = "%prefix%У вас недостаточно прав для этого действия!";

    @Create
    public MESSAGES MESSAGES;
    @Create
    public MENU_SETTINGS MENU_SETTINGS;

    public static class MESSAGES {
        public String COOLDOWN_MESSAGE = "%prefix%Вы не можете выбрать персонажа! Подождите %time%";
        public String SUCCESS_DELETE_CHARACTER = "%prefix%Вы успешно удалили персонажа!";
        public String SUCCESS_ENTER_CHARACTER = "%prefix%Вы успешно установили персонажа &6%nick%";

        @Create
        public CREATE_CHARACTER CREATE_CHARACTER;

        public static class CREATE_CHARACTER {
            public String START_CRATE = "%prefix%Напишите в чат ник персонажа:";
            public String STOP_CREATE = "%prefix%Если хотите отменить добавление, напишите 'отменить'!";
            public String STOP_CREATE_WORD = "отменить";
            public String STOP_CREATE_SUCCESS = "%prefix%Вы отменили добавление нового персонажа!";
            public String INVALID_NICK = "%prefix%Вы ввели ник неправильно! Попробуйте ещё раз!";
            public String ENTER_NICK_SUCCESS = "%prefix%Вы успешно ввели ник!";
            public String ENTER_URL = "%prefix%Введите ссылку:";
            public String INVALID_URL = "%prefix%Вы ввели ссылку неправильно! Попробуйте ещё раз!";
            public String CREATE_CHARACTER_SUCCESS = "%prefix%Вы успешно добавили нового персонажа!";
        }
    }

    @Comment({"Настройка менюшки", "", "Как происходит настройка?", "MATERIAL настраивается очень легко:", "1) Можно просто написать материал из игры, список: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html", "2) Можно поставить голову: тут нужно прописать basehead-значение", "  Пример: basehead-eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM5MDZkNjg4ZTY1ODAyNTY5ZDk3MDViNTc5YmNlNTZlZGM4NmVhNWMzNmJkZDZkNmZjMzU1MTZhNzdkNCJ9fX0=", "3) Айди + дата: Пример 5:1"})
    public static class MENU_SETTINGS {

        @Comment("Название инвентаря выбора персонажа")
        public String DISPLAYNAME = "Выберите персонажа";
        @Comment("Название инвентаря выбора персонажа для удаления")
        public String DISPLAYNAME_DELETE = "Выберите персонажа для удаления";

        @Create
        public CREATE_CHARACTER_ICON CREATE_CHARACTER_ICON;
        @Create
        public CLEAR_CHARACTER_ICON CLEAR_CHARACTER_ICON;
        @Create
        public DELETE_CHARACTER_ICON DELETE_CHARACTER_ICON;
        @Create
        public NEXT_PAGE_ICON NEXT_PAGE_ICON;
        @Create
        public PREVIOUS_PAGE_ICON PREVIOUS_PAGE_ICON;
        @Create
        public PAGE_ICON PAGE_ICON;
        @Create
        public CHARACTER_ICON CHARACTER_ICON;

        @Comment("Настройка кнопки создания персонажа")
        public static class CREATE_CHARACTER_ICON {
            public String MATERIAL = "basehead-eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM5MDZkNjg4ZTY1ODAyNTY5ZDk3MDViNTc5YmNlNTZlZGM4NmVhNWMzNmJkZDZkNmZjMzU1MTZhNzdkNCJ9fX0=";
            public String DISPLAYNAME = "&aСоздать персонажа";
            public List<String> LORE = Arrays.asList("", "&7Нажмите, чтобы создать своего персонажа!");
        }

        @Comment("Настройка кнопки отключения персонажа")
        public static class CLEAR_CHARACTER_ICON {
            public String MATERIAL = "BARRIER";
            public String DISPLAYNAME = "&eОтключить персонажа";
            public List<String> LORE = Arrays.asList("", "&7Нажмите, чтобы отключить персонажа!");
        }

        @Comment("Настройка кнопки удаления персонажа")
        public static class DELETE_CHARACTER_ICON {
            public String MATERIAL = "basehead-eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmI3OGZhNWRlZmU3MmRlYmNkOWM3NmFiOWY0ZTExNDI1MDQ3OWJiOWI0NGY0Mjg4N2JiZjZmNzM4NjEyYiJ9fX0=";
            public String DISPLAYNAME = "&cУдалить персонажа";
            public List<String> LORE = Arrays.asList("", "&7Нажмите, чтобы удалить персонажа!");
        }

        @Comment("Настройка кнопки след. страница")
        public static class NEXT_PAGE_ICON {
            public String MATERIAL = "ARROW";
            public String DISPLAYNAME = "&aСлед. страница";
            public List<String> LORE = Arrays.asList("", "&7Нажмите, чтобы перейти на след. страницу!");
        }

        @Comment("Настройка кнопки пред. страница")
        public static class PREVIOUS_PAGE_ICON {
            public String MATERIAL = "ARROW";
            public String DISPLAYNAME = "&cПред. страница";
            public List<String> LORE = Arrays.asList("", "&7Нажмите, чтобы перейти на пред. страницу!");
        }

        @Comment("Настройка кнопки страницы")
        public static class PAGE_ICON {
            public String MATERIAL = "BOOK";
            public String DISPLAYNAME = "&cСтраница номер %number%";
            public List<String> LORE = Arrays.asList("");
        }

        @Comment("Настройка головы")
        public static class CHARACTER_ICON {
            public String DISPLAYNAME = "&c%name%";
            public List<String> LORE = Arrays.asList("", "&aНажмите, чтобы выбрать данного персонажа!");
        }
    }

    public void reload(File file) {
        load(file);
        save(file);
    }
}