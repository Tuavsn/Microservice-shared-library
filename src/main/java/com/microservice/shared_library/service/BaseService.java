package com.microservice.shared_library.service;

import com.microservice.shared_library.dto.BaseDTO;
import com.microservice.shared_library.exception.NotFoundException;
import com.microservice.shared_library.mapper.BaseMapper;
import com.microservice.shared_library.model.BaseEntityAudit;
import com.microservice.shared_library.repository.BaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class BaseService<Model extends BaseEntityAudit, DTO extends BaseDTO, ID extends UUID> {
    private final BaseRepository<Model, ID> repository;
    private final BaseMapper<Model, DTO, ID> mapper;

    public List<DTO> findAll() {
        return mapper.toDTO((List<Model>) repository.findAll());
    }

    public Page<DTO> findAll(Pageable pageable, String search) {
        return repository.findContaining(pageable, "%" + search + "%").map(mapper::toDTO);
    }

    public List<DTO> findById(Set<ID> ids) {
        boolean anyNotFound = ids.stream().anyMatch((id) -> !repository.existsById(id));
        if (anyNotFound) {
            throw new NotFoundException("ID not found");
        }
        return mapper.toDTO((List<Model>) repository.findAllById(ids));
    }

    @Transactional
    public DTO save(DTO dto) {
        ID id = (ID)dto.getId();
        if (id != null && !repository.existsById(id)) {
            throw new NotFoundException("ID not found");
        }
        return this.forceSave(dto);
    }

    @Transactional
    public DTO forceSave(DTO dto) {
        Model model = mapper.toModel(dto);
        return mapper.toDTO(repository.save(model));
    }

    @Transactional
    public void delete(Set<ID> ids) {
        boolean anyNotFound = ids.stream().anyMatch((id) -> !repository.existsById(id));
        if (anyNotFound) {
            throw new NotFoundException("ID not found");
        }
        repository.softDeleteByIds(ids);
    }
}
