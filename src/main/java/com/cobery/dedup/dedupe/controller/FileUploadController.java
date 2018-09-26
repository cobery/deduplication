package com.cobery.dedup.dedupe.controller;

import com.cobery.dedup.dedupe.model.Contact;
import com.cobery.dedup.dedupe.model.Result;
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
    public ResponseEntity<Result> uploadContacts(@RequestParam("file") MultipartFile contactsFile) {
        HttpStatus returnStatus = HttpStatus.OK;
        Result result = new Result();
        try {
            result = dedupeService.dedupe(contactsFile.getBytes());
        } catch (IOException e) {
            returnStatus = HttpStatus.BAD_REQUEST;
            log.error("Error getting bytes from the file upload.", e);
        }

        log.info("******************************************");
        log.info("Potential Duplicates ( Count {} )", result.getDuplicates().size());
        for(Contact contact: result.getDuplicates()) {
            log.info(contact.toString());
        }
        log.info("******************************************");
        log.info("No Duplicates ( Count {} )", result.getContacts().size());
        for(Contact contact: result.getContacts()) {
            log.info(contact.toString());
        }
        log.info("******************************************");

        return ResponseEntity
                .status(returnStatus)
                .body(result);
    }
}
