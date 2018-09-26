package com.cobery.dedup.dedupe.config;

import com.cobery.dedup.dedupe.model.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;

/**
 * Define the beans here
 */
@Configuration
public class DedupConfig {

    @Bean
    public Comparator<Contact> comparator() {
        // define the matches here (both equals and fuzzy matches)
        // Easily expandable
        return new Comparator<Contact>() {
            public int compare(Contact contact1, Contact contact2) {
                int retVal = 1;
                if (contact1.equals(contact2)
                        || contact1.getFullNamePhoneMeta().equals(contact2.getFullNamePhoneMeta())
                        || contact1.getFullNameEmailMeta().equals(contact2.getFullNameEmailMeta())) {
                    retVal = 0;
                }
                return retVal;
            }

        };
    }
}
