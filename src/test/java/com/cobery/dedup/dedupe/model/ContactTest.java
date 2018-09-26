package com.cobery.dedup.dedupe.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for Contact.
 */

public class ContactTest {

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

    private static List<String> ROW_DATA = Arrays.asList(
            String.valueOf(CONTACT_ID),
            CONTACT_FIRST_NAME,
            CONTACT_LAST_NAME,
            CONTACT_COMPANY,
            CONTACT_EMAIL,
            CONTACT_ADDRESS1,
            CONTACT_ADDRESS2,
            CONTACT_ZIP,
            CONTACT_CITY,
            CONTACT_STATE_FULL,
            CONTACT_STATE_ABBREVIATION,
            CONTACT_PHONE
    );

    private String row;

    private String[] rows = {
        "15,Jacquelyn,Ilchenko,Goodwin Inc,jilchenkoe@wisc.edu,9160 Cherokee Avenue,,18763,Wilkes Barre,Pennsylvania,PA,570-384-8352",
        "15,Jacqueline,Ilchenko,Goodwin Inc,jilchenkoe@wisc.edu,9160 Cherokee Avenue,,18763,Wilkes Barre,Pennsylvania,PA,570-384-8352",
        "4,Kale,Gipp,Klein Group,kgipp3@360.cn,4985 Menomonie Drive,,94975,Petaluma,California,CA,707-840-2551",
        "4,Kale,Gipp,The Klein Group,kgipp3@360.cn,4985 Menomonie Drive,,94975,Petaluma,California,CA,707-840-2551",
        "4,Kale,Gipp,The Klein Group,kgipp3@360.cn,,,,,,,707-840-2551",
        "5,Analise,Doorbar,Stamm-Pagac,adoorbar4@mit.edu,2340 Dennis Center,Apt 11,33180,Miami,Florida,FL,305-604-6702",
        "6,Lewes,Stainland,Gulgowski-Kutch,lstainland5@devhub.com,99470 Caliangt Trail,Apt 12,28410,Wilmington,North Carolina,NC,910-151-9797",
        "7,Evelin,Amburgy,Hettinger Group,eamburgy6@adobe.com,683 Forest Dale Trail,,55114,Saint Paul,Minnesota,MN,612-487-2958"
    };

    @Before
    public void setup() {
        row = String.join(",", ROW_DATA);
    }

    @Test
    public void willCreateContact() {
        Contact contact = new Contact(row);
        assertEquals("Match id", CONTACT_ID, contact.getId());
        assertEquals("Match first name", CONTACT_FIRST_NAME, contact.getFirstName());
        assertEquals("Match last name", CONTACT_LAST_NAME, contact.getLastName());
        assertEquals("Match company", CONTACT_COMPANY, contact.getCompany());
        assertEquals("Match email", CONTACT_EMAIL, contact.getEmail());
        assertEquals("Match address1", CONTACT_ADDRESS1, contact.getAddress1());
        assertEquals("Match address2", CONTACT_ADDRESS2, contact.getAddress2());
        assertEquals("Match zip code", CONTACT_ZIP, contact.getZip());
        assertEquals("Match city", CONTACT_CITY, contact.getCity());
        assertEquals("Match state name", CONTACT_STATE_FULL, contact.getStateFullName());
        assertEquals("Match state abbreviation", CONTACT_STATE_ABBREVIATION, contact.getStateAbbreviation());
        assertEquals("Match phone", CONTACT_PHONE, contact.getPhone());
    }

    @Test
    public void willCreateContactMinusLastColumn() {
        String shortRow = row.substring(0, row.lastIndexOf(","));
        Contact contact = new Contact(shortRow);
        assertEquals("Match id", CONTACT_ID, contact.getId());
        assertEquals("Match first name", CONTACT_FIRST_NAME, contact.getFirstName());
        assertEquals("Match last name", CONTACT_LAST_NAME, contact.getLastName());
        assertEquals("Match company", CONTACT_COMPANY, contact.getCompany());
        assertEquals("Match email", CONTACT_EMAIL, contact.getEmail());
        assertEquals("Match address1", CONTACT_ADDRESS1, contact.getAddress1());
        assertEquals("Match address2", CONTACT_ADDRESS2, contact.getAddress2());
        assertEquals("Match zip code", CONTACT_ZIP, contact.getZip());
        assertEquals("Match city", CONTACT_CITY, contact.getCity());
        assertEquals("Match state name", CONTACT_STATE_FULL, contact.getStateFullName());
        assertEquals("Match state abbreviation", CONTACT_STATE_ABBREVIATION, contact.getStateAbbreviation());
        assertEquals("Match phone", "", contact.getPhone());
    }

    @Test
    public void willCreateContactWithEmptyLastColumn() {
        String shortRow = row.substring(0, row.lastIndexOf(",") + 1);
        Contact contact = new Contact(shortRow);
        assertEquals("Match id", CONTACT_ID, contact.getId());
        assertEquals("Match first name", CONTACT_FIRST_NAME, contact.getFirstName());
        assertEquals("Match last name", CONTACT_LAST_NAME, contact.getLastName());
        assertEquals("Match company", CONTACT_COMPANY, contact.getCompany());
        assertEquals("Match email", CONTACT_EMAIL, contact.getEmail());
        assertEquals("Match address1", CONTACT_ADDRESS1, contact.getAddress1());
        assertEquals("Match address2", CONTACT_ADDRESS2, contact.getAddress2());
        assertEquals("Match zip code", CONTACT_ZIP, contact.getZip());
        assertEquals("Match city", CONTACT_CITY, contact.getCity());
        assertEquals("Match state name", CONTACT_STATE_FULL, contact.getStateFullName());
        assertEquals("Match state abbreviation", CONTACT_STATE_ABBREVIATION, contact.getStateAbbreviation());
        assertEquals("Match phone", "", contact.getPhone());
    }

    @Test
    public void willMatchContactFirstNamesWithDifferentSpellingFirstName() {
        Contact jackie1 = new Contact(rows[0]);
        Contact jackie2 = new Contact(rows[1]);
        assertEquals("Match firstNameMeta", jackie1.getFirstNameMeta(), jackie2.getFirstNameMeta());

    }

    @Test
    public void willMatchContactsWithDifferentSpellingFirstName() {
        Contact jackie1 = new Contact(rows[0]);
        Contact jackie2 = new Contact(rows[1]);
        assertEquals("Match contacts", jackie1, jackie2);
    }

    @Test
    public void willGroup() throws Exception {
        List<Contact> contacts = Lists.newArrayList();
        List<Contact> contactId4 = Lists.newArrayList();
        Arrays.stream(rows).map(Contact::new).forEachOrdered(contact -> {
            if (contact.getId() == 4) {
                contactId4.add(contact);
            }

            contacts.add(contact);
        });

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
        for(Contact contact : contacts) {
            groups.put(contact, contact);
        }

        assertEquals("Expect the same number in the multimap group",
                contactId4.size(), groups.get(contactId4.get(contactId4.size() - 1)).size());
    }

}
