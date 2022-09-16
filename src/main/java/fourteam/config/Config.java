package fourteam.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import fourteam.console.console;

public class Config {
  private static Properties prop;

  public static String ServiceName;

  public static String getProperty(String name) {
    if (prop == null) {
      load();
    }
    return prop.getProperty(name);
  }

  public static void load() {
    load("./config.properties");
  }

  public static void load(String PathConfig) {
    console.warning("[", Config.class.getSimpleName(), "]", "Trying to load properties from file '" + PathConfig + "'");
    prop = new Properties();
    InputStream input = null;
    try {
      input = new FileInputStream(PathConfig);
      prop.load(input);
      if (prop.getProperty("ServiceName") == null) {
        console.error("[", Config.class.getSimpleName(), "]", "'ServiceName' Param not found in '" + PathConfig + "'");
        System.exit(0);
      }
      ServiceName = prop.getProperty("ServiceName");
      console.succes("[", Config.class.getSimpleName(), "]", "Properties load succesfull!");
    } catch (IOException ex) {
      console.error("[", Config.class.getSimpleName(), "]", "'" + PathConfig + "' File not found");
      System.exit(0);
    }
  }
}
