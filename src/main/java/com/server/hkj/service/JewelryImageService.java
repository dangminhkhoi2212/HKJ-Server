package com.server.hkj.service;

import com.server.hkj.repository.JewelryImageRepository;
import com.server.hkj.service.dto.JewelryImageDTO;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class JewelryImageService {

    JewelryImageRepository jewelryImageRepository;

    public JewelryImageService(JewelryImageRepository jewelryImageRepository) {
        this.jewelryImageRepository = jewelryImageRepository;
    }

    public Page<JewelryImageDTO> getJewelryAndImage(Pageable page) {
        return jewelryImageRepository.getJewelryImage(page);
    }
}
