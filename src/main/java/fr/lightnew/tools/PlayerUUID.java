package fr.lightnew.tools;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class PlayerUUID {

    public static UUID uuidWithName(String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStream input = url.openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder builder = new StringBuilder();
            reader.lines().forEach(builder::append);
            String line = builder.toString();

            Gson gson = new Gson();
            PlayerUUIDClazz obj = gson.fromJson(line, PlayerUUIDClazz.class);
            return obj.getId();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String nameWithUUID(UUID name) {
        try {
            URL url = new URL("https://api.ashcon.app/mojang/v2/user/" + name.toString().replace("-", ""));
            InputStream input = url.openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder builder = new StringBuilder();
            reader.lines().forEach(builder::append);
            String line = builder.toString();

            Gson gson = new Gson();
            PlayerUUIDClazz obj = gson.fromJson(line, PlayerUUIDClazz.class);
            return obj.getName();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
