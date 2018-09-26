package com.cobery.dedup.dedupe.service;

import com.cobery.dedup.dedupe.model.Result;

/**
 * Created by mcobery on 9/23/18.
 */
public interface DedupService {
    Result dedupe(byte[] contactUpload);
}
