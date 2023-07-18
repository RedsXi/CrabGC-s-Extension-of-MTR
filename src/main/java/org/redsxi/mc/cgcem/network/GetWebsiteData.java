package org.redsxi.mc.cgcem.network;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;

public class GetWebsiteData {
    public static NbtCompound getWebsiteData() {
        try {
            URL url = new URL("https://redsxi.github.io/crabgc-s-extension-of-mtr/data");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();

            if(connection.getResponseCode() == 200) {
                NbtCompound dataCompound = NbtIo.read(new DataInputStream(is));
                is.close();

                return dataCompound;
            } else {
                return new NbtCompound();
            }
        } catch (Exception e) {
            return new NbtCompound();
        }
    }
}
