package com.duo.examples.health;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author pythias
 * @since 2019-06-06
 */
@RestController
public class SleepController {
    @RequestMapping(method = RequestMethod.GET, value = {"/sleep", "/sleep/{millis}"})
    public String sleep(@PathVariable(name = "millis", required = false) final Optional<Long> millis) {
        try {
            if (millis.isPresent()) {
                Thread.sleep(millis.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Hello World!";
    }
}
