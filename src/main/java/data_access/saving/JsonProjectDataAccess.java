package data_access.saving;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import entity.Project;
import use_case.saving.SaveProjectDataAccessInterface;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

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
     * Saves to the "database.json".
     */
    @Override
    public void save(Project project) throws IOException {
        // Root key is the project name
        File file = new File("test.json");
        mapper.writeValue(file, project);
    }

    // Load the Project object directly
    public Project load(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new IOException("File not found: " + fileName);
        }

        // Read directly into Project class
        return mapper.readValue(file, Project.class);
    }

}