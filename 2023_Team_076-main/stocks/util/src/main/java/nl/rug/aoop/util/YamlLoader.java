package nl.rug.aoop.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * A loader object that allows you to load objects from a yaml file.
 * <p>
 * You can use e.g. Path.of("data", "example.yaml") to pass the path of a specific yaml file in the data directory.
 * </p>
 *
 * @see Path
 */
public class YamlLoader {
    /**
     * Mapper object that is used to map the yaml file to an object.
     * Since creating a new ObjectMapper is relatively expensive, this field is static.
     *
     * @see ObjectMapper
     * @see YAMLFactory
     */
    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());
    /**
     * The path to the yaml file (including the file name + extension itself).
     */
    private final Path path;

    /**
     * Creates a new loader for a specified path.
     *
     * @param path The path to the yaml file (including the file name + extension itself).
     */
    public YamlLoader(Path path) {
        this.path = path;
    }

    /**
     * Loads an object from a yaml file.
     *
     * @param clazz The class of the object that should be loaded.
     * @param <T>   The generic type that the method should return.
     * @return An object with the properties loaded from the provided yaml file.
     */
    public <T> T load(Class<T> clazz) throws IOException {
        return MAPPER.readValue(path.toFile(), clazz);
    }
}