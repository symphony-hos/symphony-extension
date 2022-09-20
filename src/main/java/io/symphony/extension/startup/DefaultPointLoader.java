package io.symphony.extension.startup;

import io.symphony.common.point.data.Point;
import io.symphony.extension.config.data.PointConfig;
import io.symphony.extension.config.data.PointConfigRepo;
import io.symphony.extension.point.data.PointRepo;
import io.symphony.extension.properties.ExtensionProperties;
import io.symphony.extension.properties.PointProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultPointLoader<T extends PointProperties> implements PointLoader {

    private final Logger logger = LoggerFactory.getLogger(DefaultPointLoader.class);


    private final ExtensionProperties<T> properties;

    private final PointConfigRepo configRepo;

    private final PointRepo pointRepo;

    @Override
    public void load() {
        if (properties.getPoints() == null)
            return;
        logger.info("Loading {} points.", properties.getPoints().size());
        properties.getPoints().forEach(this::loadPoint);
    }

    protected void loadPoint(T props) {
        logger.info("Loading point {} from properties", props.getId());
        Optional<PointConfig> configOpt = configRepo.findById(props.getId());

        // If a config with the same ID already exists, delete it and the point it refers to
        if (configOpt.isPresent()) {
            PointConfig config = configOpt.get();
            if (config.getPoint() != null)
                pointRepo.delete(config.getPoint());
            configRepo.delete(config);
        }

        Point point = props.toPoint();
        point = pointRepo.save(point);

        PointConfig config = props.toPointConfig();
        config.setPoint(point);
        configRepo.save(config);
    }

}
