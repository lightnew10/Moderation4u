package fr.lightnew.tools;

import java.util.UUID;

public class PlayerUUIDClazz {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public UUID getId() {
        StringBuffer send = new StringBuffer(id);
        send.insert(8, "-");
        send.insert(13, "-");
        send.insert(18, "-");
        send.insert(23, "-");
        return UUID.fromString(String.valueOf(send));
    }
}
