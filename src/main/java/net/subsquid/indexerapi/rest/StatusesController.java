package net.subsquid.indexerapi.rest;

import java.util.List;
import net.subsquid.indexerapi.dto.SubsquidIndexerStatus;
import net.subsquid.indexerapi.store.InMemoryStatusesStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusesController {

    private final InMemoryStatusesStore store;

    @Autowired
    public StatusesController(InMemoryStatusesStore store) {
        this.store = store;
    }

    @GetMapping("/statuses")
    public List<SubsquidIndexerStatus> getStatuses() {
        return store.getStatusesStore();
    }
}
