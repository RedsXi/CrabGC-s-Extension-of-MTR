package org.redsxi.mc.cgcem.network;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;

public class GetWebsiteData {

    private static final Logger LOGGER = LoggerFactory.getLogger("GetWebsiteData");
    public static NbtCompound getWebsiteData() {
        try {
            URL url = new URL("https://redsxi.github.io/CrabGC-s-Extension-of-MTR/data");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();

            if(connection.getResponseCode() == 200) {
                NbtCompound dataCompound = NbtIo.read(new DataInputStream(is));
                is.close();

                LOGGER.info("Successfully loaded WebsiteData");

                return dataCompound;
            } else {
                LOGGER.error("Cannot get WebsiteData because server returned status code " + connection.getResponseCode());

                return new NbtCompound();
            }
        } catch (Exception e) {
            LOGGER.error("Catched error while getting WebsiteData.", e);

            return new NbtCompound();
        }
    }
}
