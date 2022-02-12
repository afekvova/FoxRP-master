package me.afek.foxrp.config;

import java.io.File;

public class Settings extends Config {

    @Ignore
    public static final Settings IMP = new Settings();

    @Comment({" -------------------------------------------------- #",
            "   Copyright © Dizzenzio studio, 2020-2022          #",
            "   Сделать заказ: vk.com/dizzenziostudio            #",
            " -------------------------------------------------- #"})

    @Ignore
    @Final
    public final String HELP = "afek.ru, t.me/afekvova, vk.com/afekvova, github.com/afekvova";

    public String PREFIX = "&f[&6FoxRP&f] ";
    public String PERMISSION_MSG = "%prefix%У вас недостаточно прав для этого действия!";

    public void reload(File file) {
        load(file);
        save(file);
    }
}
