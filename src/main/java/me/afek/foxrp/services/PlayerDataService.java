package me.afek.foxrp.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import me.afek.foxrp.FoxRPPlugin;
import me.afek.foxrp.model.Character;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerDataService {

    FoxRPPlugin plugin;
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @NonFinal
    File cdFile;

    // TODO: Переписать этот калл, сделать через ormlite
    public PlayerDataService(FoxRPPlugin plugin) {
        this.plugin = plugin;

        this.createJsonFile(plugin);

        if (cdFile.length() == 0) return;

        Map<String, List<String>> tempMap;
        try {
            tempMap = gson.fromJson(new String(Files.readAllBytes(cdFile.toPath())), new TypeToken<Map<String, List<String>>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        tempMap.forEach((name, value) -> {
            List<Character> heroData = value.stream().map(hero -> {
                val split = hero.split(":");
                return new Character(split[0], split[1], split[2]);
            }).collect(Collectors.toList());

            plugin.getCharacterRepository().addPlayerCharacters(name.toLowerCase(), heroData);
        });
    }

    public void savePlayerData() {
        val tempMap = new HashMap<String, List<String>>();
        for (val stringListEntry : plugin.getCharacterRepository().getCharacterPlayerData().entrySet())
            tempMap.put(stringListEntry.getKey(), stringListEntry.getValue().stream().map(hero -> hero.getName() + ":" + hero.getValue() + ":" + hero.getSignature()).collect(Collectors.toList()));

        try {
            Files.writeString(cdFile.toPath(), gson.toJson(tempMap), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createJsonFile(final FoxRPPlugin plugin) {
        val customConfigFile = new File(plugin.getDataFolder(), "data.json");
        if (!customConfigFile.exists()) {
            customConfigFile.mkdirs();

            try {
                customConfigFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.cdFile = customConfigFile;
    }
}