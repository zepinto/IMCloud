package jobs;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

public class AddressesUpdater implements CloudJob {

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public FiniteDuration firstDelay() {
		return Duration.create(0, TimeUnit.SECONDS);
	}

	@Override
	public FiniteDuration periodicDelay() {
		return null;
	}

}
