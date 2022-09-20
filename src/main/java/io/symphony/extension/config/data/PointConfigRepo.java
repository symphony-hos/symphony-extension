package io.symphony.extension.config.data;

import io.symphony.common.point.data.Point;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PointConfigRepo<T extends PointConfig> extends CrudRepository<T, String>{

    Optional<T> findByPoint(Point point);

}
