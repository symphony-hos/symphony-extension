package io.symphony.extension.point.rest;

import io.symphony.common.exception.PointAccessDeniedException;
import io.symphony.common.exception.PointNotFoundException;
import io.symphony.common.point.IStatePoint;
import io.symphony.common.point.data.Access;
import io.symphony.common.point.data.Point;
import io.symphony.common.point.data.color.ColorPoint;
import io.symphony.common.point.data.quantity.QuantityPoint;
import io.symphony.common.point.data.state.*;
import io.symphony.common.point.model.*;
import io.symphony.extension.point.data.PointRepo;
import io.symphony.extension.sync.Point2SystemSynchronizer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ExposesResourceFor(PointModel.class)
@Controller
@ResponseBody
@RequestMapping("/points")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PointController {
	
	private Logger logger = LoggerFactory.getLogger(PointController.class);
	
	
	private final PointRepo pointRepo;
	
	private final PointResourceAssembler assembler;

	private final Point2SystemSynchronizer syncer;

	@GetMapping
	public ResponseEntity<CollectionModel<PointModel>> all() {
		CollectionModel<PointModel> result = assembler.toCollectionModel(pointRepo.findAll());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("{id}")
	public ResponseEntity<PointModel> one(@PathVariable String id) {
		Point entity = pointRepo.findById(id).orElseThrow(() -> new PointNotFoundException(id));
		return new ResponseEntity<>(assembler.toModel(entity), HttpStatus.OK);
	}

	@PatchMapping("{id}")
	public ResponseEntity<PointModel> edit(@RequestBody PointModel model, @PathVariable String id) {
		Point point = pointRepo.findById(id).orElseThrow(() -> new PointNotFoundException(id));

		/*
		 * Copy the common properties that can be set via the API and save the changes.
		 * - labels YES - accessMode NO, value must come from external system - type NO,
		 * defined by model
		 */
		if (model.getLabels() != null)
			point.getLabels().putAll(model.getLabels());

		point = pointRepo.save(point);

		/*
		 * Now we must distinguish between the different types of PointModel and figure
		 * out if we must invoke the synchronizer. Usually, the synchronizer must be
		 * called when the value / state has changed.
		 */
		if (updatePointValue(point, model)) {
			logger.debug("Dispatching change of point {} to synchronizer", point.getId());
			syncer.toSystem(point);
		}

		return new ResponseEntity<>(assembler.toModel(point), HttpStatus.OK);
	}

	private boolean updatePointValue(Point p, PointModel m) {
		if (!p.getAccess().contains(Access.WRITE))
			throw new PointAccessDeniedException(p.getId());

		if (p instanceof QuantityPoint && m instanceof QuantityPointModel)
			return updateQuantityValue((QuantityPoint) p, (QuantityPointModel) m);
			
		else if (p instanceof IStatePoint && m instanceof StatePointModel)
			return updateStateValue((IStatePoint<?>) p, (StatePointModel<?>) m);
		
		else if (p instanceof ColorPoint && m instanceof ColorPointModel)
			return updateColorValue((ColorPoint) p, (ColorPointModel) m);
		
		logger.warn("Don't know how to handle " + p.getClass().getCanonicalName() + " and " + m.getClass().getCanonicalName());
		
		throw new RuntimeException("Unable to handle point update for point " + p.getId());
	}

	private boolean updateQuantityValue(QuantityPoint p, QuantityPointModel m) {
		if (m.getValue() != null && !m.getValue().equals(p.getValue())) {
			p.setValue(m.getValue());
			return true;
		}
		return false;
	}
	
	private boolean updateStateValue(IStatePoint<?> point, StatePointModel<?> model) {
		if (model.getValue() == null || model.getValue().equals(point.getValue())) {
			logger.info("Model is either null or states are equal");
			return false;
		}
		
		if (point instanceof SwitchPoint && model instanceof SwitchPointModel) {
			SwitchPoint p = (SwitchPoint) point;
			SwitchPointModel m = (SwitchPointModel) model;
			p.setValue(m.getValue());
			return true;
			
		} else if (point instanceof MotionPoint) {
			MotionPoint p = (MotionPoint) point;
			MotionPointModel m = (MotionPointModel) model;
			p.setValue(m.getValue());
			return true;
			
		} else if (point instanceof VerticalDirectionPoint) {
			VerticalDirectionPoint p = (VerticalDirectionPoint) point;
			VerticalDirectionPointModel m = (VerticalDirectionPointModel) model;
			p.setValue(m.getValue());
			return true;
			
		} else if (point instanceof AlarmPoint) {
			AlarmPoint p = (AlarmPoint) point;
			AlarmPointModel m = (AlarmPointModel) model;
			p.setValue(m.getValue());
			return true;
			
		} else if (point instanceof ContactPoint) {
			ContactPoint p = (ContactPoint) point;
			ContactPointModel m = (ContactPointModel) model;
			p.setValue(m.getValue());
			return true;
		}

		logger.warn("Unable to update point {} from model {}", point.getClass().getCanonicalName(), model.getClass().getCanonicalName());
		return false;
	}
	
	private boolean updateColorValue(ColorPoint p, ColorPointModel m) {
		if (m.getValue() != null && !m.getValue().equals(p.getValue())) {
			p.setValue(m.getValue());
			return true;
		}
		return false;
	}

}
