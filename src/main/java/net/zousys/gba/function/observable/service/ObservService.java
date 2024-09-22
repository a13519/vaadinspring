package net.zousys.gba.function.observable.service;

import net.zousys.gba.function.observable.dto.ObservDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ObservService {

    public List<ObservDTO> retrieveObservs(String name) {
        return new ArrayList<>();
    }

    public int countEntities() {
        // Return the total count of entities
        return (int) 0;
    }

    public int countEntities(String name) {
        // Return the total count of entities
        return (int) 0;
    }
}
