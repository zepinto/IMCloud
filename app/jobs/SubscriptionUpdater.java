package jobs;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import controllers.Iridium;

public class SubscriptionUpdater implements CloudJob {

	private String lastSubscriber = null;
	private Date lastSendDate = null;
	
	@Override
	public void run() {
		if (Iridium.subscriber != null) {
			if (!lastSubscriber.equals(Iridium.subscriber)) {
				// iridium subscriber has changed... send all updates!
			}
		}
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
