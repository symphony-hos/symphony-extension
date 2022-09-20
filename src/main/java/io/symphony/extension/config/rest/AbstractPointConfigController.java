package io.symphony.extension.config.rest;


import io.symphony.extension.config.data.PointConfig;
import io.symphony.extension.config.data.PointConfigRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ExposesResourceFor(PointConfig.class)
@Controller
@ResponseBody
@RequestMapping("/configs")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Import(PointConfigRelProvider.class)
public abstract class AbstractPointConfigController<T extends PointConfig> {
	
	private final EntityLinks links;
	
	private final PointConfigRepo<T> configRepo;

	private final PointConfigUpdater updateHandler;
	

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<T>>> all() {
		Set<EntityModel<T>> models = StreamSupport.stream(configRepo.findAll().spliterator(), false)
		  .map(c -> toModel(c))
		  .collect(Collectors.toSet());
				
		CollectionModel<EntityModel<T>> result = CollectionModel.of(models);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<EntityModel<T>> create(@RequestBody T config) {
		T cfg = configRepo.save(config);
		return new ResponseEntity<>(toModel(cfg), HttpStatus.CREATED);
	}
	
	
	@GetMapping("{id}")
	public ResponseEntity<EntityModel<T>> one(@PathVariable String id) {
		if (id == null)
			throw new RuntimeException("Point config not found");

		T entity = configRepo.findById(id).orElseThrow(() -> new RuntimeException("Point not found"));
		return new ResponseEntity<>(toModel(entity), HttpStatus.OK);
	}

	@PutMapping("{id}")
	public ResponseEntity<EntityModel<T>> overwrite(@RequestBody T config, @PathVariable String id) {
		Optional<T> opt = configRepo.findById(id);

		if (opt.isPresent()) {
			T cfg = opt.get();
			updateHandler.updateAll(config, cfg);
			cfg = configRepo.save(cfg);
			return new ResponseEntity<>(toModel(cfg), HttpStatus.OK);
		}

		throw new RuntimeException("Not found.");
	}

	@PatchMapping("{id}")
	public ResponseEntity<EntityModel<T>> edit(@RequestBody T config, @PathVariable String id) {
		Optional<T> opt = configRepo.findById(id);
		
		if (opt.isPresent()) {
			T cfg = opt.get();
			updateHandler.updateNonNull(config, cfg);
			cfg = configRepo.save(cfg);
			return new ResponseEntity<>(toModel(cfg), HttpStatus.OK);
		}
		
		throw new RuntimeException("Not found.");
	}
	
	
	private EntityModel<T> toModel(T entity) {
		EntityModel<T> model = EntityModel.of(entity);
		model.add(links.linkForItemResource(PointConfig.class, entity.getId()).withSelfRel());
		return model;
	}

}
