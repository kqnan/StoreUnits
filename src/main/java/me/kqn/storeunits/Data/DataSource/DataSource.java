package me.kqn.storeunits.Data.DataSource;

import me.kqn.storeunits.Data.PlayerData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 基于json的序列化方式
 * */
public interface DataSource {
    public static byte[] Gzip(String json)  {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(json.getBytes(StandardCharsets.UTF_8));
            gzip.finish();
            gzip.close();
            byte[] ret = bos.toByteArray();
            bos.close();
            return ret;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String Gunzip(byte[] data){
        try {


            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            GZIPInputStream gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[1024];
            int num = -1;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                bos.write(buf, 0, num);
            }
            gzip.close();
            bis.close();
            byte[] ret = bos.toByteArray();
            bos.flush();
            bos.close();
            return new String(ret,StandardCharsets.UTF_8);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public PlayerData readToPlayerData(UUID uuid);
    public void write(PlayerData playerData,UUID uuid);
    public void onDisable();
}
