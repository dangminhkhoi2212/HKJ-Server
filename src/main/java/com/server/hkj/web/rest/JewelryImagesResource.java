package com.server.hkj.web.rest;

import com.server.hkj.service.JewelryImageService;
import com.server.hkj.service.dto.JewelryImageDTO;
import java.util.List;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/jewelry-images")
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class JewelryImagesResource {

    JewelryImageService jewelryImageService;

    JewelryImagesResource(JewelryImageService jewelryImageService) {
        this.jewelryImageService = jewelryImageService;
    }

    @GetMapping("")
    public ResponseEntity<List<JewelryImageDTO>> getJewelryImage(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        Page<JewelryImageDTO> page = jewelryImageService.getJewelryAndImage(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
