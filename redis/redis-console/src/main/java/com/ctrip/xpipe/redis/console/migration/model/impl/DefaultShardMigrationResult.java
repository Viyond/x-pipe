package com.ctrip.xpipe.redis.console.migration.model.impl;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ctrip.xpipe.redis.console.migration.model.ShardMigrationResult;
import com.ctrip.xpipe.redis.console.migration.model.ShardMigrationResultStatus;
import com.ctrip.xpipe.redis.console.migration.model.ShardMigrationStep;
import com.ctrip.xpipe.utils.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author shyin
 *
 * Dec 8, 2016
 */
@SuppressWarnings("serial")
public class DefaultShardMigrationResult implements Serializable, ShardMigrationResult{

	private ShardMigrationResultStatus status;
	private Map<ShardMigrationStep, Pair<Boolean, String>> steps = new ConcurrentHashMap<>(6);
	
	public DefaultShardMigrationResult() {
		status = ShardMigrationResultStatus.FAIL;
	}

	@Override
	public ShardMigrationResultStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(ShardMigrationResultStatus status) {
		this.status = status;
	}

	@Override
	public Map<ShardMigrationStep, Pair<Boolean, String>> getSteps() {
		return steps;
	}

	@Override
	public boolean stepTerminated(ShardMigrationStep step) {
		return steps.containsKey(step);
	}

	@Override
	public boolean stepSuccess(ShardMigrationStep step) {
		return stepTerminated(step) ? steps.get(step).getKey() : false;
	}

	@Override
	public void stepRetry(ShardMigrationStep step) {
		steps.remove(step);
	}

	@Override
	public void updateStepResult(ShardMigrationStep step, boolean success, String log) {
		steps.put(step, Pair.of(success, log));
	}

	@Override
	public void setSteps(Map<ShardMigrationStep, Pair<Boolean, String>> steps) {
		this.steps = steps;
	}

	@Override
	public boolean equals(Object obj) {

		if(!(obj instanceof DefaultShardMigrationResult)){
			return false;
		}

		DefaultShardMigrationResult other = (DefaultShardMigrationResult) obj;

		if(!(ObjectUtils.equals(status, other.status))){
			return false;
		}

		if(!(ObjectUtils.equals(steps, other.steps))){
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return ObjectUtils.hashCode(status, steps);
	}

	@Override
	public String toString() {
		return String.format("status:%s, steps:%s", status, steps);
	}
}
