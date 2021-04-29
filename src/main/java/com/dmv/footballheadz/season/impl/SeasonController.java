package com.dmv.footballheadz.season.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin
@RestController
@RequestMapping("/v1")
public class SeasonController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SeasonService service;

    @Autowired
    private SeasonValidation seasonValidation;

    @RequestMapping(path = "/season", method = RequestMethod.GET)
    public ResponseEntity<List<Season>> list() {
        log.trace("Entering list()");
        List<Season> seasons = service.list();
        if (seasons.isEmpty()) {
            return new ResponseEntity<>(NO_CONTENT);
        }
        return new ResponseEntity<>(seasons, OK);
    }

    @RequestMapping(path = "/season/", method = RequestMethod.GET)
    public ResponseEntity<List<Season>> listOfYear(@RequestParam(value="year") String year) {
        log.trace("Entering listOfYear() for {}", year);
        if (!seasonValidation.isValidYear(year)){
            return new ResponseEntity<>(BAD_REQUEST);
        }
        List<Season> seasons = service.listByYear(year);
        if (seasons.isEmpty()) {
            return new ResponseEntity<>(NO_CONTENT);
        }
        return new ResponseEntity<>(seasons, OK);
    }

    @RequestMapping(path = "/season/{id}", method = RequestMethod.GET)
    public ResponseEntity<Season> read(@PathVariable String id) {
        log.trace("Entering read() with {}", id);
        return service.read(id)
                .map(season -> new ResponseEntity<>(season, OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @RequestMapping(path = "/season", method = RequestMethod.POST)
    public ResponseEntity<Season> create(@RequestBody @Valid Season season) {
        log.trace("Entering create() with {}", season);
        return service.create(season)
                .map(newSeasonData -> new ResponseEntity<>(newSeasonData, CREATED))
                .orElse(new ResponseEntity<>(CONFLICT));
    }

    @RequestMapping(path = "/season/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Season> put(@PathVariable String id, @RequestBody Season season) {
        log.trace("Entering put() with {}, {}", id, season);
        return service.replace(season.withId(id))
                .map(newSeasonData -> new ResponseEntity<>(newSeasonData, OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @RequestMapping(path = "/season/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Season> patch(@PathVariable String id, @RequestBody Season season) {
        log.trace("Entering patch() with {}, {}", id, season);
        return service.update(season.withId(id))
                .map(newSeasonData -> new ResponseEntity<>(newSeasonData, OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @RequestMapping(path = "/season/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.trace("Entering delete() with {}", id);
        return service.delete(id) ?
                new ResponseEntity<>(NO_CONTENT) :
                new ResponseEntity<>(NOT_FOUND);
    }
}
