package com.sports.cache.key;

import com.sports.logic.util.Util;

public class StageWinnerKey extends CacheKey {
	private final int competitionId;
	private final int seasonId;
	private final int stage;

	public StageWinnerKey(int competitionId, int seasonId, int stage) {
		this.competitionId = competitionId;
		this.seasonId = seasonId;
		this.stage = stage;
	}

	@Override
	String getSpecificKeyPart() {
		return Util.concatStrings(new String[] {
		        Integer.toString(competitionId), Integer.toString(seasonId),
				Integer.toString(stage)
		}, "|");
	}
}
