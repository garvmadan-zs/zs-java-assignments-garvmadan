
package com.zs.assignment5.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommitAnalysisResult {
    private final Map<String, Integer> totalCommitsByDeveloper;
    private final Map<String, Map<LocalDate, Integer>> commitsByDeveloperByDay;
    private final List<String> developersInactiveForTwoDays;

    public CommitAnalysisResult(
            Map<String, Integer> totalCommitsByDeveloper,
            Map<String, Map<LocalDate, Integer>> commitsByDeveloperByDay,
            List<String> developersInactiveForTwoDays) {
        this.totalCommitsByDeveloper = new LinkedHashMap<>(totalCommitsByDeveloper);
        this.commitsByDeveloperByDay = new LinkedHashMap<>();
        this.developersInactiveForTwoDays = new ArrayList<>(developersInactiveForTwoDays);

        commitsByDeveloperByDay.forEach((developer, dailyCounts) ->
                this.commitsByDeveloperByDay.put(developer, new LinkedHashMap<>(dailyCounts))
        );
    }

    public Map<String, Integer> getTotalCommitsByDeveloper() {
        return totalCommitsByDeveloper;
    }

    public Map<String, Map<LocalDate, Integer>> getCommitsByDeveloperByDay() {
        return commitsByDeveloperByDay;
    }

    public List<String> getDevelopersInactiveForTwoDays() {
        return developersInactiveForTwoDays;
    }
}
