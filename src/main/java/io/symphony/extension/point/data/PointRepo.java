package io.symphony.extension.point.data;

import io.symphony.common.point.data.Point;
import org.springframework.data.repository.CrudRepository;

public interface PointRepo extends CrudRepository<Point, String>{

}
