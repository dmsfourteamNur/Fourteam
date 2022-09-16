package fourteam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class JSON {

  static String formatDate = "yyyy-MM-dd";
  static String formatDateTime = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  static class LocalDateSerializer implements JsonSerializer<LocalDate> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatDate);

    @Override
    public JsonElement serialize(
      LocalDate localDate,
      Type srcType,
      JsonSerializationContext context
    ) {
      return new JsonPrimitive(formatter.format(localDate));
    }
  }

  static class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatDateTime);

    @Override
    public JsonElement serialize(
      LocalDateTime localDateTime,
      Type srcType,
      JsonSerializationContext context
    ) {
      return new JsonPrimitive(formatter.format(localDateTime));
    }
  }

  static class LocalDateDeserializer implements JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(
      JsonElement json,
      Type typeOfT,
      JsonDeserializationContext context
    ) throws JsonParseException {
      return LocalDate.parse(
        json.getAsString(),
        DateTimeFormatter.ofPattern(formatDate).withLocale(Locale.getDefault())
      );
    }
  }

  static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(
      JsonElement json,
      Type typeOfT,
      JsonDeserializationContext context
    ) throws JsonParseException {
      return LocalDateTime.parse(
        json.getAsString(),
        DateTimeFormatter.ofPattern(formatDateTime).withLocale(Locale.getDefault())
      );
    }
  }

  public static Gson getInstance() {
    return new GsonBuilder()
      .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
      .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
      .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
      .create();
    // return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new
    // JsonDeserializer<LocalDateTime>() {
    // @Override
    // public LocalDateTime deserialize(JsonElement json, Type type,
    // JsonDeserializationContext jsonDeserializationContext) throws
    // JsonParseException {
    // Instant instant =
    // Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
    // return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    // }
    // }).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
  }
  // public static void main(String[] args) {
  //     Aeronave a = new Aeronave("Hola");
  //     a.eventCreado();
  //     String json = getInstance().toJson(a, Aeronave.class);
  //     System.out.println(json);

  // }
}
