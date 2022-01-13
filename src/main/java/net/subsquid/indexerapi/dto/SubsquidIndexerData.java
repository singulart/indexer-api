package net.subsquid.indexerapi.dto;

import org.springframework.core.style.ToStringCreator;

public class SubsquidIndexerData {

    private SubsquidIndexerStatus indexerStatus;

    public SubsquidIndexerStatus getIndexerStatus() {
        return indexerStatus;
    }

    public void setIndexerStatus(SubsquidIndexerStatus indexerStatus) {
        this.indexerStatus = indexerStatus;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
            .append("indexerStatus", this.indexerStatus)
            .toString();
    }
}
