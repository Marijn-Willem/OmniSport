package com.sports.cache.key;

public class PhaseTypeAsParentKey extends CacheKey {
    private final int phaseTypeId;

    public PhaseTypeAsParentKey(int phaseTypeId) {
        this.phaseTypeId = phaseTypeId;
    }

    @Override
    String getSpecificKeyPart() {
        return Integer.toString(phaseTypeId);
    }
}
