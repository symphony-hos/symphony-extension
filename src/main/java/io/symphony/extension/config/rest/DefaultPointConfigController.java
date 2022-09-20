package io.symphony.extension.config.rest;


import io.symphony.extension.config.data.PointConfig;
import io.symphony.extension.config.data.PointConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.stereotype.Controller;

@Controller
public class DefaultPointConfigController extends AbstractPointConfigController<PointConfig> {

	public DefaultPointConfigController(@Autowired EntityLinks links, @Autowired PointConfigRepo<PointConfig> configRepo, @Autowired PointConfigUpdater updateHandler) {
		super(links, configRepo, updateHandler);
	}

}
