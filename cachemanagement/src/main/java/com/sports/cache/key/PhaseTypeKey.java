package com.sports.cache.key;

public class PhaseTypeKey extends CacheFragmentKey {
    private final int phaseTypeId;

    public PhaseTypeKey(int phaseTypeId) {
        this.phaseTypeId = phaseTypeId;
    }

    @Override
    String getSpecificKeyPart() {
        return Integer.toString(phaseTypeId);
    }
}
