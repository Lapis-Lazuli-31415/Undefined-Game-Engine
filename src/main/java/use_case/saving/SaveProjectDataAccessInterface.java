package use_case.saving;

import entity.Project;
import java.io.IOException;

public interface SaveProjectDataAccessInterface {
    void save(Project project) throws IOException;
}
