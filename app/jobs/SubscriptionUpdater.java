package jobs;

import java.util.concurrent.TimeUnit;

import play.Logger;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

public class SubscriptionUpdater implements CloudJob {

	@Override
	public void run() {
		Logger.info("Update subscribers?");
	}

	@Override
	public FiniteDuration firstDelay() {
		return Duration.create(20, TimeUnit.SECONDS);
	}

	@Override
	public FiniteDuration periodicDelay() {
		return Duration.create(5, TimeUnit.MINUTES);
	}

}
