package me.kqn.storeunits.Utils;

import com.google.gson.*;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;


public class ItemStackSerializer implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        NBTContainer nbtContainer=new NBTContainer(((JsonObject)json).get("StoreSpace_item").getAsString());
        return NBTItem.convertNBTtoItem(nbtContainer);
    }

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonElement=new JsonObject();
        jsonElement.addProperty("StoreSpace_item",NBTItem.convertItemtoNBT(src).toString());
        return jsonElement;
    }
}