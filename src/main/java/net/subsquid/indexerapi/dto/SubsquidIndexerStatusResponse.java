package net.subsquid.indexerapi.dto;

import org.springframework.core.style.ToStringCreator;

public class SubsquidIndexerStatusResponse {

    private SubsquidIndexerData data;

    public SubsquidIndexerData getData() {
        return data;
    }

    public void setData(SubsquidIndexerData data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return new ToStringCreator(this)
            .append("data", this.data)
            .toString();
    }

}
