package com.bharat.polls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


/**
 * Notes:
 * @EntityScan -> used for configuring base packages used by auto-configuration when scanning for entity classes *
 * @PostConstruct -> used for the method that needs to be executed after dependency injection.
 * For more: https://docs.oracle.com/javaee/5/api/javax/annotation/PostConstruct.html *
 */

@SpringBootApplication
@EntityScan(basePackageClasses = {
        PollsApplication.class,
        Jsr310JpaConverters.class
})
public class PollsApplication {

    @PostConstruct
    void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
    public static void main(String[] args) {
        SpringApplication.run(PollsApplication.class, args);
    }

}
