package com.server.hkj.service;

import com.server.hkj.domain.HkjCategory;
import com.server.hkj.repository.HkjCategoryRepository;
import com.server.hkj.service.dto.HkjCategoryDTO;
import com.server.hkj.service.mapper.HkjCategoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjCategory}.
 */
@Service
@Transactional
public class HkjCategoryService {

    private static final Logger log = LoggerFactory.getLogger(HkjCategoryService.class);

    private final HkjCategoryRepository hkjCategoryRepository;

    private final HkjCategoryMapper hkjCategoryMapper;

    public HkjCategoryService(HkjCategoryRepository hkjCategoryRepository, HkjCategoryMapper hkjCategoryMapper) {
        this.hkjCategoryRepository = hkjCategoryRepository;
        this.hkjCategoryMapper = hkjCategoryMapper;
    }

    /**
     * Save a hkjCategory.
     *
     * @param hkjCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjCategoryDTO save(HkjCategoryDTO hkjCategoryDTO) {
        log.debug("Request to save HkjCategory : {}", hkjCategoryDTO);
        HkjCategory hkjCategory = hkjCategoryMapper.toEntity(hkjCategoryDTO);
        hkjCategory = hkjCategoryRepository.save(hkjCategory);
        return hkjCategoryMapper.toDto(hkjCategory);
    }

    /**
     * Update a hkjCategory.
     *
     * @param hkjCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjCategoryDTO update(HkjCategoryDTO hkjCategoryDTO) {
        log.debug("Request to update HkjCategory : {}", hkjCategoryDTO);
        HkjCategory hkjCategory = hkjCategoryMapper.toEntity(hkjCategoryDTO);
        hkjCategory.setIsPersisted();
        hkjCategory = hkjCategoryRepository.save(hkjCategory);
        return hkjCategoryMapper.toDto(hkjCategory);
    }

    /**
     * Partially update a hkjCategory.
     *
     * @param hkjCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjCategoryDTO> partialUpdate(HkjCategoryDTO hkjCategoryDTO) {
        log.debug("Request to partially update HkjCategory : {}", hkjCategoryDTO);

        return hkjCategoryRepository
            .findById(hkjCategoryDTO.getId())
            .map(existingHkjCategory -> {
                hkjCategoryMapper.partialUpdate(existingHkjCategory, hkjCategoryDTO);

                return existingHkjCategory;
            })
            .map(hkjCategoryRepository::save)
            .map(hkjCategoryMapper::toDto);
    }

    /**
     *  Get all the hkjCategories where HkjTemplate is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HkjCategoryDTO> findAllWhereHkjTemplateIsNull() {
        log.debug("Request to get all hkjCategories where HkjTemplate is null");
        return StreamSupport.stream(hkjCategoryRepository.findAll().spliterator(), false)
            .filter(hkjCategory -> hkjCategory.getHkjTemplate() == null)
            .map(hkjCategoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one hkjCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjCategoryDTO> findOne(Long id) {
        log.debug("Request to get HkjCategory : {}", id);
        return hkjCategoryRepository.findById(id).map(hkjCategoryMapper::toDto);
    }

    /**
     * Delete the hkjCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HkjCategory : {}", id);
        hkjCategoryRepository.deleteById(id);
    }
}
