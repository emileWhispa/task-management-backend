package com.example.taskmanagement.controllers;

import com.example.taskmanagement.models.Attachment;
import com.example.taskmanagement.repository.AttachmentRepository;
import com.example.taskmanagement.services.FilesStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("download")
public class DownloadController {
    private final AttachmentRepository attachmentRepository;
    private final FilesStorageService filesStorageService;

    public DownloadController(AttachmentRepository attachmentRepository, FilesStorageService filesStorageService) {
        this.attachmentRepository = attachmentRepository;
        this.filesStorageService = filesStorageService;
    }

    @GetMapping("attachment/{id}")
    public Object download(@PathVariable("id") long id, RedirectAttributes ra, HttpServletRequest request) throws IOException
    {
        Optional<Attachment> attachmentOptional = attachmentRepository.findById(id);

        if (attachmentOptional.isEmpty()) {
            ra.addFlashAttribute("error", "Attachment is not found!");

            return "redirect:" + request.getHeader("Referer");
        }

        Attachment attachment = attachmentOptional.get();

        Resource resource = filesStorageService.load("attachments/" + attachment.getName());
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
