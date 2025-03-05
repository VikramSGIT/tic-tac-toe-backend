package com.dedshot.game.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class SerializerUtil {
    public String serialize(Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
        } catch (IOException e) {
            log.error("Error while writing object to byte stream. {}", e.getMessage());
            e.printStackTrace();
            return null;
        }

        byte[] serializedBytes = baos.toByteArray();
        return new String(serializedBytes);
    }

    public Object deserialize(String object) {
        try {
            byte[] bytes = object.getBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                return ois.readObject();
            }
        } catch (IOException e) {
            log.error("Error while converting bytes to object. {}", e.getMessage());
            log.debug("Object: {}", object);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.error("Error while reading object from stream. {}", e.getMessage());
            log.debug("Object: {}", object);
            e.printStackTrace();
        }
        return null;
    }
}
