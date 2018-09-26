package com.cobery.dedup.dedupe.service;

import com.cobery.dedup.dedupe.model.Result;

/**
 * Parse duplicate contacts from a list of contacts that has been uploaded.
 */
public interface DedupService {
    /**
     * The bytes of the uploaded file are received
     * and converted to a file for parsing out the duplicates.
     * @param contactUpload
     * @return
     */
    Result dedupe(byte[] contactUpload);
}
