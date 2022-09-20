package io.symphony.extension.config.rest;

import io.symphony.extension.config.data.PointConfig;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PointConfigRelProvider implements LinkRelationProvider {
	
	@Override
	public LinkRelation getCollectionResourceRelFor(final Class<?> type) {
		return LinkRelation.of("configs");
	}

	@Override
	public LinkRelation getItemResourceRelFor(Class<?> type) {
		return LinkRelation.of("config");
	}

	@Override
	public boolean supports(LookupContext delimiter) {
		return PointConfig.class.isAssignableFrom(delimiter.getType());
	}

}
