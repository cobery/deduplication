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
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Default implementation of the @see DedupService.
 */
@Slf4j
@Named
public class DedupServiceImpl implements DedupService {

    private final Comparator<Contact> contactComparator;

    public DedupServiceImpl(Comparator<Contact> comparator) {
        this.contactComparator = comparator;
    }

    @Override
    public Result dedupe(byte[] contactUpload) {
        Result result = readContacts(contactUpload);

        return result;
    }

    private Result readContacts(byte[] contactUpload) {
        // turn each row into a contact
        Result result = new Result();

        Multimap<Contact, Contact> groups = TreeMultimap.create(contactComparator, Ordering.arbitrary());

        String contactString = new String(contactUpload);

        Stream<String> contactStream = Pattern.compile("\\r?\\n").splitAsStream(contactString).skip(1);

        contactStream.map(Contact::new).forEachOrdered(contact -> {
            groups.put(contact, contact);
        });

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

}
