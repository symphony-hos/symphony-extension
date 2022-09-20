package io.symphony.extension.point.rest;

import io.symphony.common.point.model.PointModel;
import io.symphony.extension.config.data.PointConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RootController {
	
	private final EntityLinks links;
	
	@GetMapping
	public ResponseEntity<EntityModel<Map<String, Object>>> getRoot() {
		EntityModel<Map<String, Object>> em = EntityModel.of(Map.of());
		em.add(links.linkToCollectionResource(PointModel.class).withRel("points"));
		em.add(links.linkToCollectionResource(PointConfig.class).withRel("configs"));
		return new ResponseEntity<EntityModel<Map<String, Object>>>(em, HttpStatus.OK);
	}

}