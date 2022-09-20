package io.symphony.extension.properties;

import io.symphony.common.point.data.Access;
import io.symphony.common.point.data.Point;
import io.symphony.common.point.data.color.ColorPoint;
import io.symphony.common.point.data.quantity.QuantityPoint;
import io.symphony.common.point.data.state.*;
import io.symphony.extension.config.data.PointConfig;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

@Data
@Validated
@NoArgsConstructor
@SuperBuilder
public class PointProperties {
		
	@Getter
	public static enum PointType {
		Quantity(QuantityPoint.class),
		Switch(SwitchPoint.class),
		Motion(MotionPoint.class),
		VerticalDirection(VerticalDirectionPoint.class),
		Alarm(AlarmPoint.class),
		Contact(ContactPoint.class),
		Color(ColorPoint.class);
		
		private Class<? extends Point> clazz;
		
		private PointType(Class<? extends Point> clazz) {
			this.clazz = clazz;
		}
		
	}

	@NotNull
	private String id;
	
	private Map<String, String> labels;

	@NotNull
	private PointType type;

	private Object value;

	@NotNull
	@NotEmpty
	private Set<Access> access = Access.READ_WRITE;


	public PointConfig toPointConfig() {
		return PointConfig.builder()
			.id(getId())
			.build();
	}

	public Point toPoint()  {
		try {
			Point point = getType().getClazz().getDeclaredConstructor().newInstance();
			point.setId(getId());
			point.setLabels(getLabels());
			point.setValueAsString(getValue() != null ? getValue().toString() : null);
			point.setAccess(getAccess());
			return point;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
