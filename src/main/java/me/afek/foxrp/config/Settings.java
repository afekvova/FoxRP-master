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

    @Create
    public TICKET_GIVE_COMMAND TICKET_GIVE_COMMAND;

    @Create
    public TICKET_INFO_COMMAND TICKET_INFO_COMMAND;

    @Create
    public TICKET_REMOVE_COMMAND TICKET_REMOVE_COMMAND;
    @Create
    public TICKET_PLAYER_COMMAND TICKET_PLAYER_COMMAND;

    public static class TICKET_GIVE_COMMAND {
        public String USE = "%prefix%/ticket give [ник] [алмазы] [срок] [причина] - выдать штраф игроку";
        @Comment("Максимальное количество времени, которое можно установить для штрафа")
        public int MAX_TIME_TICKET = 5;
        @Comment("Минимальное количество времени, которое можно установить для штрафа")
        public int MIN_TIME_TICKET = 1;
        public String TIME_ERROR = "%prefix%Вы не можете установить такой количество времени! Макс. - 5, мин. - 1";
        @Comment("Длина id штрафа")
        public int TICKET_ID_LENGTH = 8;
        @Comment({"Placeholders:", "%ticketId% - id штрафа", "%player% - игрок"})
        public String SUCCESS = "%prefix%Вы успешно выдали штраф (&7%ticketId%&f) игроку &6%player%";
        @Comment("Отправить ли сообщение игроку")
        public boolean SEND_PLAYER_MESSAGE = true;
        @Comment({"Placeholders:", "%ticketId% - id штрафа"})
        public String PLAYER_MESSAGE = "%prefix%Вам выдали штраф (&7%ticketId%&f)";
    }

    public static class TICKET_REMOVE_COMMAND {
        public String USE = "%prefix%/ticket remove [id штрафа] - удалить штраф";
        public String NOT_EXIST = "%prefix%Такой штраф не существует!";
        public String SUCCESS = "%prefix%Вы успешно удалили штраф (&7%ticketId%&f) игроку &6%player%";
    }

    public static class TICKET_INFO_COMMAND {
        public String USE = "%prefix%/ticket info [id штрафа] - информация о штрафе";
        public String NOT_EXIST = "%prefix%Такой штраф не существует!";
        public List<String> MESSAGE = Arrays.asList("&a__________ %ticketId% __________",
                "&aИгрок: &6%player%",
                "&aАлмазов: &6%diamonds%",
                "&aОсталось времени: &6%leftTime%",
                "&aПричина: &6%reason%");
    }

    public static class TICKET_PLAYER_COMMAND {
        public String USE = "%prefix%/ticket player [Игрок] - узнать информацию о игроке";
        public String NOT_EXIST = "%prefix%У игрока нету штрафов!";
    }

    @Create
    public TICKET_EDIT_COMMAND TICKET_EDIT_COMMAND;

    public static class TICKET_EDIT_COMMAND {
        public String USE = "%prefix%/ticket edit [id штрафа] [ник] [алмазы] [срок] [причина] - редактировать штраф";
        public String NOT_EXIST = "%prefix%Такой штраф не существует!";
        public String SUCCESS = "%prefix%Вы успешно изменили штраф (&7%ticketId%&f) игроку &6%player%";
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


    @Create
    public SQL SQL;

    @Comment("Настройка датабазы")
    public static class SQL {

        @Comment("Тип датабазы. sqlite или mysql")
        public String STORAGE_TYPE = "sqlite";
        @Comment("Через сколько дней удалять игроков из датабазы, которые зашли один раз и больше не заходили. 0 или меньше чтобы отключить")
        public int PURGE_TIME = 14;
        @Comment("Настройки для mysql")
        public String HOSTNAME = "127.0.0.1";
        public int PORT = 3306;
        public String USER = "user";
        public String PASSWORD = "password";
        public String DATABASE = "database";
    }

    public void reload(File file) {
        load(file);
        save(file);
    }
}
