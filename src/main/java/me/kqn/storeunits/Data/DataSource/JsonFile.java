package me.kqn.storeunits.Data.DataSource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import me.kqn.storeunits.Data.PlayerData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class JsonFile implements DataSource {
    String folder="plugins\\StoreUnits\\data";

    /**
     * 若读取到错误数据则返回空的playerdata
     * */
    @Override
    public PlayerData readToPlayerData(UUID uuid) {
        File fileFolder=new File(folder);
        if(!fileFolder.exists())fileFolder.mkdir();//文件夹不存在就创建
        File[] datafiles=fileFolder.listFiles();
        if(datafiles==null)return new PlayerData(uuid);
        for (File datafile : datafiles) {
            if(datafile.getName().replace(".json","").equals(uuid.toString())){
                try {
                    FileInputStream fis=new FileInputStream(datafile);
                    byte[] bytes=new byte[fis.available()];
                    fis.read(bytes);
                    String json;
                    json=DataSource.Gunzip(bytes);

                    Gson gson=new Gson();
                    JsonArray jsondata= gson.fromJson(json,JsonArray.class);
                    fis.close();
                    return PlayerData.fromJson(jsondata);
                }catch (Exception e){
                    e.printStackTrace();

                }
            }
        }
        return new PlayerData(uuid);
    }

    @Override
    public void write(PlayerData playerData,UUID uuid) {
        File filefolder=new File(folder);
        if(!filefolder.exists())filefolder.mkdir();
        File datafile=new File(folder+"\\"+uuid.toString()+".json");
        if(!datafile.exists()) {
            try {
                datafile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            String json=PlayerData.toJson(playerData).toString();

            byte[] bytes=DataSource.Gzip(json);
            FileOutputStream fos=new FileOutputStream(datafile);
            fos.write(bytes);
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onDisable(){
       Map<UUID,PlayerData> dataMap= PlayerData.getData();
        for (UUID uuid : dataMap.keySet()) {
            write(dataMap.get(uuid),uuid);
        }
    }

}
