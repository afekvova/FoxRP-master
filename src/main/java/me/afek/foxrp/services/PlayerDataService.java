package me.afek.foxrp.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.afek.foxrp.FoxRPPlugin;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.objects.HeroData;

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

public class PlayerDataService {

    private final DataCommon dataCommon;
    private final Gson gson = new GsonBuilder().create();
    private File cdFile;

    public PlayerDataService(FoxRPPlugin plugin, DataCommon dataCommon) {
        this.createJsonFile(plugin);
        this.dataCommon = dataCommon;

        if (cdFile.length() == 0)
            return;

        Type type = new TypeToken<Map<String, List<String>>>() {
        }.getType();
        Map<String, List<String>> tempMap;
        try {
            tempMap = gson.fromJson(new String(Files.readAllBytes(cdFile.toPath())), type);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        tempMap.forEach((name, value) -> {
            List<HeroData> heroData = value.stream().map(hero -> {
                String[] split = hero.split(":");
                return new HeroData(split[0], split[1], split[2]);
            }).collect(Collectors.toList());
            this.dataCommon.addPlayerHeroes(name.toLowerCase(), heroData);
        });
    }

    public void savePlayerData() {
        Map<String, List<String>> tempMap = new HashMap<>();
        for (Map.Entry<String, List<HeroData>> stringListEntry : this.dataCommon.getPlayerData().entrySet())
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