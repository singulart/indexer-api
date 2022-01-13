package net.subsquid.indexerapi.store;

import java.util.ArrayList;
import net.subsquid.indexerapi.dto.SubsquidIndexerStatus;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStatusesStore {

    private final ArrayList<SubsquidIndexerStatus> statusesStore = new ArrayList<>();

    public ArrayList<SubsquidIndexerStatus> getStatusesStore() {
        return statusesStore;
    }
}
