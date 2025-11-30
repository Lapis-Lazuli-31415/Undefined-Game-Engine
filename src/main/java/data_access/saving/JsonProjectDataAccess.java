package data_access.saving;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import entity.Project;
import use_case.saving.SaveProjectDataAccessInterface;

import java.io.File;
import java.io.IOException;

public class JsonProjectDataAccess implements SaveProjectDataAccessInterface {

    private final ObjectMapper mapper;

    /**
     * Constructs a new JsonProjectDataAccess object.
     * Initializes the underlying ObjectMapper with specific configurations:
     *      - SerializationFeature.INDENT_OUTPUT - Enabled for pretty-printing.
     *     -  PropertyNamingStrategies.SNAKE_CASE - Enforced for JSON keys.
     */
    public JsonProjectDataAccess() {
        this.mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);// enable pretty printing (for indentation)
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);// force snake_case
    }

    /**
     * Serializes and saves the Project object to a JSON file.
     *
     * The project is wrapped in a root Map where the key is the project's name.
     * Saves to the "database.json" (this is not actually saving to the actual file in the code).
     */
    @Override
    public void save(Project project) throws IOException {
        File file = new File("database.json");
        mapper.writeValue(file, project);
    }

    /**
     * Loads a Project object from a specified JSON file.
     * This method reads the contents of the file and uses the configured
     * ObjectMapper to deserialize the JSON directly into a Project entity.
     *
     * @param fileName The path to the JSON file to load (e.g., "database.json").
     * @return The fully deserialized Project object.
     * @throws IOException If the file does not exist, is inaccessible, or if the
     * JSON parsing/deserialization fails (e.g., malformed JSON).
     */
    public Project load(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new IOException("File not found: " + fileName);
        }

        // Read directly into Project class
        return mapper.readValue(file, Project.class);
    }

}