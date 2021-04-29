package com.dmv.footballheadz.season.impl;

import com.dmv.footballheadz.util.SeasonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class SeasonValidation {

    public boolean isValidYear(String year) {
        return StringUtils.isNotBlank(year) && year.length() == 4
                && StringUtils.isNumeric(year);
    }

    public boolean isValidId(Season season) {
        if (StringUtils.isBlank(season.getId())){
            return false;
        }
        String[] id = season.getId().split("\\|");
        return id.length == 2 && id[0].equals(season.getYear())
                && id[1].equals(season.getTeamName());
    }

    public boolean isValidRank(String rank) {
        return StringUtils.isNotBlank(rank) && StringUtils.isNumeric(rank)
                && Integer.parseInt(rank) >= 1 && Integer.parseInt(rank) <= SeasonUtils.LEAGUE_SIZE;
    }
}
