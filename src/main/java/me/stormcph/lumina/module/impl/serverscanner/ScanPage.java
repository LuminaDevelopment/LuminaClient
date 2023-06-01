package me.stormcph.lumina.module.impl.serverscanner;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.TextSetting;
import org.bson.Document;

import java.util.Date;

public class ScanPage extends Module {
    public TextSetting value = new TextSetting("Value", "0");

    public ScanPage() {
        super("Page", "Skips forward to get different results", Category.SERVER_SCANNER);
        addSettings(value);
        removeKeybind();
    }
}
