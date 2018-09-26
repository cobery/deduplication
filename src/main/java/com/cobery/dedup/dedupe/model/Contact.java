package com.cobery.dedup.dedupe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ComparisonChain;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.language.Metaphone;

/**
 * Represents a row of data in an import.
 * Currently the row order is:
 * id,first_name,last_name,company,email,address1,address2,zip,city,state_long,state,phone
 * Example data
 * 1,Donalt,Canter,Gottlieb Group,dcanter0@nydailynews.com,9 Homewood Alley,,50335,Des Moines,Iowa,IA,515-601-4495
 * In the future, Apache POI could be used to perform the mapping.
 */
@Getter
@ToString
@EqualsAndHashCode
public class Contact implements Comparable<Contact>{
    private static Metaphone METAPHONE =  new Metaphone();

    public Contact(String row) {
        String[] columns = row.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        this.id = Integer.valueOf(columns[0]);
        this.firstName = columns.length > 1 ? columns[1] : "";
        this.firstNameMeta = firstName.length() > 0 ? METAPHONE.metaphone(firstName) : "";
        this.lastName = columns.length > 2 ? columns[2] : "";
        this.lastNameMeta = lastName.length() > 0 ? METAPHONE.metaphone(lastName) : "";
        this.company = columns.length > 3 ? columns[3] : "";
        this.companyMeta = company.length() > 0 ? METAPHONE.metaphone(company) : "";
        this.email = columns.length > 4 ? columns[4] : "";
        this.emailMeta = email.length() > 0 ? METAPHONE.metaphone(email) : "";
        this.address1 = columns.length > 5 ? columns[5] : "";
        this.address2 = columns.length > 6 ? columns[6] : "";
        this.zip = columns.length > 7 ? columns[7] : "";
        this.city = columns.length > 8 ? columns[8] : "";
        this.stateFullName = columns.length > 9 ? columns[9] : "";
        this.stateAbbreviation = columns.length > 10 ? columns[10] : "";
        this.phone = columns.length > 11 ? columns[11] : "";
        this.fullNamePhoneMeta = METAPHONE.metaphone(this.firstName + this.lastName + this.phone);
        this.fullNameEmailMeta = METAPHONE.metaphone(this.firstName + this.lastName + this.email);
    }

    @EqualsAndHashCode.Exclude
    private final int id;

    private final String firstName;

    @JsonIgnore
    private final String firstNameMeta;

    private final String lastName;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private final String lastNameMeta;

    @EqualsAndHashCode.Exclude
    private final String company;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private final String companyMeta;

    private final String email;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private final String emailMeta;

    private final String address1;

    @EqualsAndHashCode.Exclude
    private final String address2;

    private final String zip;

    private final String city;

    private final String stateFullName;

    private final String stateAbbreviation;

    private final String phone;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private final String fullNamePhoneMeta;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private final String fullNameEmailMeta;


    @Override
    public int compareTo(Contact contact) {
        // only compares if prior compare in the chain is zero, so stops once there is a difference
        return ComparisonChain.start()
                .compare(lastName, contact.getLastName())
                .compare(firstName, contact.getFirstName())
                .compare(email, contact.getEmail())
                .result();
    }
}
