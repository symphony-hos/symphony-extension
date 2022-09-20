package io.symphony.extension.point.rest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.core.EvoInflectorLinkRelationProvider;
import org.springframework.stereotype.Component;

import io.symphony.common.point.data.Point;
import io.symphony.common.point.model.PointModel;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PointRelProvider extends EvoInflectorLinkRelationProvider implements LinkRelationProvider {
	
	@Override
	public LinkRelation getCollectionResourceRelFor(final Class<?> type) {
		return super.getCollectionResourceRelFor(Point.class);
	}

	@Override
	public LinkRelation getItemResourceRelFor(Class<?> type) {
		return super.getItemResourceRelFor(Point.class);
	}

	@Override
	public boolean supports(LookupContext delimiter) {
		return PointModel.class.isAssignableFrom(delimiter.getType());
	}

}
