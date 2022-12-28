package com.devsuperior.bds02.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.EventRepository;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;

@Service
public class EventService {

	@Autowired
	private EventRepository repository;

	@Transactional(readOnly = true)
	public Page<EventDTO> findAllPaged(Pageable pageable) {
		Page<Event> list = repository.findAll(pageable);
		return list.map(x -> new EventDTO(x));
	}

	@Transactional(readOnly = true)
	public EventDTO findById(Long id) {
		Optional<Event> obj = repository.findById(id);
		Event entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		return new EventDTO(entity);
	}

	@Transactional
	public EventDTO update(Long id, EventDTO dto) {

		try {
		Event entity = repository.getOne(id);

		entity.setName(dto.getName());
		entity.setDate(dto.getDate());
		entity.setUrl(dto.getUrl());
		entity.getCity().setId(dto.getCityId());

		entity = repository.save(entity);

		return new EventDTO(entity);
		
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Not found");
		}
	}
}
