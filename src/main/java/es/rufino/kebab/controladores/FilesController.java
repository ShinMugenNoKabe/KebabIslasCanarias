/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.controladores;

import es.rufino.kebab.upload.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Controller
class FilesController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        
        return ResponseEntity.ok().body(file);
    }
}
