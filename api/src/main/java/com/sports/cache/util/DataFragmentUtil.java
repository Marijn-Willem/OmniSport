package com.sports.cache.util;

import com.sports.cache.data.DataFragment;
import com.sports.cache.data.OutputData;
import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CacheFragmentKey;
import com.sports.db.execute.DatabaseExecutor;
import com.sports.logic.async.ThreadUtil;
import com.sports.logic.async.ThreadWorker;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class DataFragmentUtil {
    private static final int nestingLevelOffset = 2;

    public static <T extends DataFragment> T getFilledDataFragment(T dataFragment, CacheDataKey cacheDataKey, Statement stat)
        throws SQLException {
        dataFragment.fill(cacheDataKey, stat);
        return dataFragment;
    }

    public static <T extends DataFragment> void fillDataFragments(List<T> dataFragments, CacheDataKey cacheDataKey) {
        ThreadWorker[] threadWorkers = new ThreadWorker[dataFragments.size()];

        for (int i = 0; i < dataFragments.size(); i++) {
            int j = i;
            threadWorkers[j] = () ->
                new DatabaseExecutor() {
                    @Override
                    public void doWork(Statement stat) throws SQLException {
                        getFilledDataFragment(dataFragments.get(j), cacheDataKey,  stat);
                    }
                }.execute();
        }

        ThreadUtil.executeAsync(Arrays.asList(threadWorkers), true, 20);
    }

    public static <T extends OutputData> T getFilledOutputData(T outputData, CacheFragmentKey cacheKey) {
        T cachedOutputData = CacheUtil.get(outputData.getCacheDataKey());

        if (cachedOutputData == null) {
            new FilledOutputDataExecutor(outputData).execute();
            if (outputData.isValidOutput())
                CacheUtil.setOutputData(outputData, cacheKey);

            return outputData;
        }

        return cachedOutputData;
    }

    public static int getLevelForNestedFragment(int nestingLevel) {
        return nestingLevel + nestingLevelOffset;
    }

    public static int getLevelForNestedList(int nestingLevel) {
        return nestingLevel + 2 * nestingLevelOffset;
    }

    private static class FilledOutputDataExecutor extends DatabaseExecutor {
        private final OutputData outputData;

        public FilledOutputDataExecutor(OutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void doWork(Statement stat) throws SQLException {
            outputData.fill(stat);
        }
    }
}
