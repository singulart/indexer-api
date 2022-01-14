package net.subsquid.indexerapi.dto;

import java.util.ArrayList;
import java.util.List;

public class IndexersRegistry {

    private List<Indexer> archives = new ArrayList<>();

    public List<Indexer> getArchives() {
        return archives;
    }
}
