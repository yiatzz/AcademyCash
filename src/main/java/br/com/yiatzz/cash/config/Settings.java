package br.com.yiatzz.cash.config;

import br.com.yiatzz.cash.config.node.General;
import br.com.yiatzz.cash.config.node.TextConfig;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
public class Settings {

    private final Logger logger;
    private final Path dataFolder;

    private final ConfigurationOptions options = getConfigurationOptions();

    private ObjectMapper<General>.BoundInstance configMapper;
    private ObjectMapper<TextConfig>.BoundInstance textMapper;

    @Inject
    Settings(Logger logger, @ConfigDir(sharedRoot = false) Path dataFolder) {
        this.logger = logger;
        this.dataFolder = dataFolder;

        try {
            configMapper = options.getObjectMapperFactory().getMapper(General.class).bindToNew();
            textMapper = options.getObjectMapperFactory().getMapper(TextConfig.class).bindToNew();
        } catch (ObjectMappingException objMappingExc) {
            logger.error("Invalid plugin structure", objMappingExc);
        }
    }

    private ConfigurationOptions getConfigurationOptions() {
        ConfigurationOptions defaults = ConfigurationOptions.defaults();

        TypeSerializerCollection serializers = defaults.getSerializers().newChild();

        return defaults.setSerializers(serializers);
    }

    public void load() {
        Path configFile = dataFolder.resolve("config.conf");
        Path textFile = dataFolder.resolve("lang.conf");

        try {
            if (Files.notExists(dataFolder)) {
                Files.createDirectories(dataFolder);
            }

            if (Files.notExists(configFile)) {
                Files.createFile(configFile);
            }

            if (Files.notExists(textFile)) {
                Files.createFile(textFile);
            }
        } catch (IOException ioEx) {
            logger.error("Failed to create default config file", ioEx);
        }

        loadMapper(configMapper, configFile, options);
        loadMapper(textMapper, textFile, options
                .setHeader("Compre mais plugins"));
    }

    private <T> void loadMapper(ObjectMapper<T>.BoundInstance mapper, Path file, ConfigurationOptions options) {
        ConfigurationNode rootNode;
        if (mapper != null) {
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(file).build();
            try {
                rootNode = loader.load(options.setShouldCopyDefaults(true));

                mapper.populate(rootNode);

                loader.save(rootNode);
            } catch (ObjectMappingException objMappingExc) {
                logger.error("Error loading the configuration", objMappingExc);
            } catch (IOException ioExc) {
                logger.error("Error saving the default configuration", ioExc);
            }
        }
    }

    public General getGeneral() {
        return configMapper.getInstance();
    }

    public TextConfig getText() {
        return textMapper.getInstance();
    }

    public Path getConfigDir() {
        return dataFolder;
    }
}
