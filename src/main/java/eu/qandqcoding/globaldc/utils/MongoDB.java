package eu.qandqcoding.discordbot.utils;

import com.google.gson.Gson;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDB {

    public static MongoDB instance;
    public Gson gson;
    public MongoClient mongoClient;
    public MongoDatabase database;
    public MongoCollection<Document> collection;

    public MongoDB() {
        instance = this;
        gson = new Gson().newBuilder().create();
        ConnectionString connectionString = new ConnectionString("mongodb+srv://qandq:73Zg58Br76@cluster0.9vknj.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        mongoClient = MongoClients.create(settings);
        System.out.println("Database Connection open");
        database = mongoClient.getDatabase("DiscordBot");
        collection = database.getCollection("Tags");
    }

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }

    public void setCollection(MongoCollection<Document> collection) {
        this.collection = collection;
    }
}
