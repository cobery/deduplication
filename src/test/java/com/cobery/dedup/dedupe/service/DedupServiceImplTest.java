package com.cobery.dedup.dedupe.service;

import com.cobery.dedup.dedupe.model.Contact;
import com.cobery.dedup.dedupe.model.Result;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for DedupServiceImpl.
 */

public class DedupServiceImplTest {

    private static final int CONTACT_ID = 1;
    private static final String CONTACT_FIRST_NAME = "Donalt";
    private static final String CONTACT_LAST_NAME = "Canter";
    private static final String CONTACT_COMPANY = "Gottlieb Group";
    private static final String CONTACT_EMAIL = "dcanter0@nydailynews.com";
    private static final String CONTACT_ADDRESS1 = "9 Homewood Alley";
    private static final String CONTACT_ADDRESS2 = "";
    private static final String CONTACT_ZIP = "50335";
    private static final String CONTACT_CITY = "Des Moines";
    private static final String CONTACT_STATE_FULL = "Iowa";
    private static final String CONTACT_STATE_ABBREVIATION = "IA";
    private static final String CONTACT_PHONE = "515-601-4495";

    private static final String[] CONTACTS = {
        "15,Jacquelyn,Ilchenko,Goodwin Inc,jilchenkoe@wisc.edu,9160 Cherokee Avenue,,18763,Wilkes Barre,Pennsylvania,PA,570-384-8352",
        "15,Jacqueline,Ilchenko,Goodwin Inc,jilchenkoe@wisc.edu,9160 Cherokee Avenue,,18763,Wilkes Barre,Pennsylvania,PA,570-384-8352",
        "4,Kale,Gipp,Klein Group,kgipp3@360.cn,4985 Menomonie Drive,,94975,Petaluma,California,CA,707-840-2551",
        "4,Kale,Gipp,The Klein Group,kgipp3@360.cn,4985 Menomonie Drive,,94975,Petaluma,California,CA,707-840-2551",
        "4,Kale,Gipp,The Klein Group,kgipp3@360.cn,,,,,,,707-840-2551",
        "5,Analise,Doorbar,Stamm-Pagac,adoorbar4@mit.edu,2340 Dennis Center,Apt 11,33180,Miami,Florida,FL,305-604-6702",
        "6,Lewes,Stainland,Gulgowski-Kutch,lstainland5@devhub.com,99470 Caliangt Trail,Apt 12,28410,Wilmington,North Carolina,NC,910-151-9797",
        "7,Evelin,Amburgy,Hettinger Group,eamburgy6@adobe.com,683 Forest Dale Trail,,55114,Saint Paul,Minnesota,MN,612-487-2958"
    };

    private byte[] contactBytes;

    private DedupService dedupService;

    @Before
    public void setup() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter, true);
        for (String contact: CONTACTS) {
            writer.println(contact);
        }
        contactBytes = stringWriter.toString().getBytes();

        Comparator<Contact> contactComparator = new Comparator<Contact>() {
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

        dedupService = new DedupServiceImpl(contactComparator);
    }

    @Test
    public void willDedupe() throws Exception {
        Result result = dedupService.dedupe(contactBytes);

        List<Contact> contactId4 = Lists.newArrayList();
        Arrays.stream(CONTACTS).map(Contact::new).forEachOrdered(contact -> {
            if (contact.getId() == 4) {
                contactId4.add(contact);
            }
        });


        assertTrue("Expect all contacts with id 4 in the duplicates in result",
                result.getDuplicates().containsAll(contactId4));
    }

}
