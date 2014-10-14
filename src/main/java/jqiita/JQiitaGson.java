package jqiita;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.OffsetDateTime;

public class JQiitaGson {
    public static Gson gson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                .registerTypeAdapter(OffsetDateTime.class, new QiitaClient.OffsetDateTimeConverter())
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }
}
