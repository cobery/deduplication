package com.cobery.dedup.dedupe.controller;

import com.cobery.dedup.dedupe.model.Contact;
import com.cobery.dedup.dedupe.model.Result;
import com.cobery.dedup.dedupe.service.DedupService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test FileUploadController.
 */
public class FileUploadControllerTest {

    private String CONTACTS = "id,first_name,last_name,company,email,address1,address2,zip,city,state_long,state,phone\n" +
            "1,Donalt,Canter,Gottlieb Group,dcanter0@nydailynews.com,9 Homewood Alley,,50335,Des Moines,Iowa,IA,515-601-4495\n";


    private MockMvc mvc;

    private FileUploadController fileUploadController;

    @Mock
    private DedupService mockDedupeService;

    @Mock
    private Result mockResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        fileUploadController = new FileUploadController(mockDedupeService);

        // MockMvc standalone approach
        mvc = MockMvcBuilders.standaloneSetup(fileUploadController)
                .build();
    }

    @Test
    public void willUploadContacts() throws Exception {

        // dedupeService.dedupe(contactsFile.getBytes())
        when(mockDedupeService.dedupe(any())).thenReturn(mockResult);

        MockMultipartFile contactsFile = new MockMultipartFile("file", "", "application/text", CONTACTS.getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/contacts")
                .file("file", contactsFile.getBytes())
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

}