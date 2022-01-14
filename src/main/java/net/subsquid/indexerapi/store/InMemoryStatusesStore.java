package net.subsquid.indexerapi.store;

import java.util.ArrayList;
import java.util.List;
import net.subsquid.indexerapi.dto.SubsquidIndexerStatus;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStatusesStore {

    private final List<SubsquidIndexerStatus> statusesStore = new ArrayList<>();

    public List<SubsquidIndexerStatus> getStatusesStore() {
        return statusesStore;
    }
}
