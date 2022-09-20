package io.symphony.extension.event;

import java.util.Set;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.symphony.common.messages.command.Command;
import io.symphony.common.messages.command.PointCommand;
import io.symphony.common.messages.command.PointCommand.DataType;
import io.symphony.common.messages.command.PublishAll;
import io.symphony.common.point.data.Point;
import io.symphony.extension.point.data.PointRepo;
import io.symphony.extension.sync.Point2SystemSynchronizer;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommandExecutor {

	private final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
	
	
	private final Point2SystemSynchronizer syncer;
	
	private final PointPublisher publisher;
	
	private final PointRepo repo;
	
	
	public void execute(Command command) {
		if (command instanceof PublishAll) {
			long count = StreamSupport.stream(repo.findAll().spliterator(), false)
				.peek(p -> publisher.publish(p))
				.count();
			logger.debug("Queued {} points for publishing", count);
		} else if (command instanceof PointCommand) {
			executePointCommand((PointCommand) command);
		}
	}
	
	private void executePointCommand(PointCommand command) {
		if (command.getSelector() == null) {
			logger.info("Command is not selecting any points: {}", command);
			return;
		}
		
		for (Point p: repo.findAll()) {
			
			if (!command.getSelector().select(p).isSelected()) {
				continue;
			}
			
			Set<DataType> changes = command.execute(p);
			logger.debug("Executing command {} on point {} yielded result: {}", command.getType(), p.getId(), changes);
			if (changes.contains(DataType.DATA))
				syncer.toSystem(p);
			if (changes.contains(DataType.METADATA))
				repo.save(p);
		}
	}
		
}
