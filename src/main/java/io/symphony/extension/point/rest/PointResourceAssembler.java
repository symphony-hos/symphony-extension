package io.symphony.extension.point.rest;

import io.symphony.common.point.data.Point;
import io.symphony.common.point.data.color.ColorPoint;
import io.symphony.common.point.data.quantity.QuantityPoint;
import io.symphony.common.point.data.state.*;
import io.symphony.common.point.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class PointResourceAssembler extends RepresentationModelAssemblerSupport<Point, PointModel> {

	private final EntityLinks links;
	
	public PointResourceAssembler(@Autowired EntityLinks links) {
		super(PointController.class, PointModel.class);
		this.links = links;
	}

	@Override
	public PointModel toModel(Point entity) {
		PointModel model = this.instantiateModel(entity);
		model.setLabels(entity.getLabels());
		model.setAccess(entity.getAccess());
		model.add(links.linkToItemResource(PointModel.class, entity.getId()).withSelfRel());
		// model.add(linkTo(methodOn(PointController.class).configRelation(entity.getConfig().getId())).withRel("config"));
		return model;
	}

	@Override
	protected PointModel instantiateModel(Point entity) {
		assert(entity != null);
		
		if (entity instanceof QuantityPoint)
			return new QuantityPointModel((QuantityPoint) entity);
		
		else if (entity instanceof SwitchPoint)
			return new SwitchPointModel((SwitchPoint) entity);
		
		else if (entity instanceof MotionPoint)
			return new MotionPointModel((MotionPoint) entity);
		
		else if (entity instanceof VerticalDirectionPoint)
			return new VerticalDirectionPointModel((VerticalDirectionPoint) entity);
		
		else if (entity instanceof ColorPoint)
			return new ColorPointModel((ColorPoint) entity);
		
		else if (entity instanceof ContactPoint)
			return new ContactPointModel((ContactPoint) entity);
		
		else if (entity instanceof AlarmPoint)
			return new AlarmPointModel((AlarmPoint) entity);
		
		throw new RuntimeException("Unknown point type: " + entity.getClass().getCanonicalName());
	}
}
