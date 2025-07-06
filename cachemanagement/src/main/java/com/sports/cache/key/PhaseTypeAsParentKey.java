package com.sports.cache.key;

public class PhaseTypeAsParentKey extends CacheFragmentKey {
    private final int phaseTypeId;

    public PhaseTypeAsParentKey(int phaseTypeId) {
        this.phaseTypeId = phaseTypeId;
    }

    @Override
    String getSpecificKeyPart() {
        return Integer.toString(phaseTypeId);
    }
}
