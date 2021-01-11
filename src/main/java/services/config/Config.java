package services.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
//    public static String CONFIG_LOCATION=Config.class.getClassLoader()
//            .getResource("config.properties").getFile();
    public static Properties getProperties() {
        Properties properties=new Properties();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream resourceStream = loader.getResourceAsStream("config.properties");
            //properties.load(new FileReader(CONFIG_LOCATION));
            properties.load(resourceStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config properties");
        }
    }
}
