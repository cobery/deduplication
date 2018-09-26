package com.cobery.dedup.dedupe.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * The reduced results in the form of 2 sets.
 */
@Data
public class Result {
    Set<Contact> contacts = Sets.newHashSet();
    List<Contact> duplicates = Lists.newArrayList();
}
