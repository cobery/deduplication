package com.cobery.dedup.dedupe.controller;

import com.cobery.dedup.dedupe.service.DedupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Accepts the file upload of the contacts.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class FileUploadController {
    private final DedupService dedupeService;

    public FileUploadController(DedupService dedupeService) {
        this.dedupeService = dedupeService;
    }

    @RequestMapping(value = "/contacts", method = RequestMethod.POST)
    public ResponseEntity<?> uploadContacts(@RequestParam("file") MultipartFile contactsFile) {
        HttpStatus returnStatus = HttpStatus.OK;
        try {
            dedupeService.dedupe(contactsFile.getBytes());
        } catch (IOException e) {
            returnStatus = HttpStatus.BAD_REQUEST;
            log.error("Error getting bytes from the file upload.", e);
        }
        return new ResponseEntity<>(returnStatus);
    }
}
