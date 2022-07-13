package me.afek.foxrp.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import me.afek.foxrp.FoxRPPlugin;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.model.Character;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerDataService {

    DataCommon dataCommon;
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    Type type = new TypeToken<Map<String, List<String>>>() {
    }.getType();

    @NonFinal
    File cdFile;

    //TODO: Переписать этот калл
    public PlayerDataService(FoxRPPlugin plugin, DataCommon dataCommon) {
        this.dataCommon = dataCommon;
        this.createJsonFile(plugin);

        if (cdFile.length() == 0)
            return;


        Map<String, List<String>> tempMap;
        try {
            tempMap = gson.fromJson(new String(Files.readAllBytes(cdFile.toPath())), type);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        tempMap.forEach((name, value) -> {
            List<Character> heroData = value.stream().map(hero -> {
                String[] split = hero.split(":");
                return new Character(split[0], split[1], split[2]);
            }).collect(Collectors.toList());

            this.dataCommon.addPlayerCharacteres(name.toLowerCase(), heroData);
        });
    }

    public void savePlayerData() {
        Map<String, List<String>> tempMap = new HashMap<>();
        for (Map.Entry<String, List<Character>> stringListEntry : this.dataCommon.getCharacterPlayerData().entrySet())
            tempMap.put(stringListEntry.getKey(), stringListEntry.getValue().stream().map(hero -> hero.getName() + ":" + hero.getValue() + ":" + hero.getSignature()).collect(Collectors.toList()));

        try {
            Files.write(cdFile.toPath(), gson.toJson(tempMap).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createJsonFile(FoxRPPlugin plugin) {
        File customConfigFile = new File(plugin.getDataFolder(), "data.json");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            try {
                customConfigFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.cdFile = customConfigFile;
    }
}