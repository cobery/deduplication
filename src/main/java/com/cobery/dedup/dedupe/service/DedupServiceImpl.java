package com.cobery.dedup.dedupe.service;

import com.cobery.dedup.dedupe.model.Contact;
import com.cobery.dedup.dedupe.model.Result;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Default implementation of the DedupService.
 */
@Slf4j
@Named
public class DedupServiceImpl implements DedupService {

    @Override
    public Result dedupe(byte[] contactUpload) {
        Result result = readContacts(contactUpload);

        return result;
    }

    private Result readContacts(byte[] contactUpload) {
        // create a temp file
        File contactFile = createTempFile(contactUpload);

        // turn each row into a contact
        Result result = new Result();

        Comparator<Contact> comparator = new Comparator<Contact>() {
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

        Multimap<Contact, Contact> groups = TreeMultimap.create(comparator, Ordering.arbitrary());

        try (Stream<String> stream = Files.lines(Paths.get(contactFile.toURI())).skip(1)) {

            stream.map(Contact::new).forEachOrdered(contact -> {
                groups.put(contact, contact);
            });

        } catch (IOException e) {
            log.error("Error writing to temp file", e);
            throw new RuntimeException(e);
        }

        Map<Contact, Collection<Contact>> groupMap = groups.asMap();
        for(Collection<Contact> group : groupMap.values()) {
            if (group.size() == 1) {
                result.getContacts().addAll(group);
            } else {
                result.getDuplicates().addAll(group);
            }
        }

        return result;
    }

    private File createTempFile(byte[] bytes) {
        String fileName = UUID.randomUUID().toString();

        File tempFile = null;
        try {
            tempFile = File.createTempFile(fileName, ".tmp");
        } catch (IOException e) {
            log.error("Error creating temp file", e);
        }
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(bytes);
            //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try.
            // And this will automatically close the OutputStream
        }catch (IOException e) {
            log.error("Error writing to temp file", e);
        }
        tempFile.deleteOnExit();
        return tempFile;
    }

}
