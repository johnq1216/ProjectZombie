package threadAndTimers;

public class Timer {

	private long startTime;
	private long timeAccum;
	private long seconds;
	private long timeSet, msSet;
	private boolean abs;
	private long onPauseTime, onResumeTime, totalPauseTime;

	// will count up starting from zero
	// public Timer() {
	// timeLeft = 0;
	// startTime = System.currentTimeMillis();
	// abs = true;
	// }

	// will count down starting with the number supplied in paramater
	public Timer(int timeSet) {
		startTime = System.currentTimeMillis();
		this.timeSet = timeSet;
		onPauseTime = onResumeTime = System.currentTimeMillis();
		totalPauseTime = 0;
	}

	public Timer(long msSet) {
		startTime = System.currentTimeMillis();
		this.msSet = msSet;
		onPauseTime = 0;
		onPauseTime = onResumeTime = System.currentTimeMillis();
		totalPauseTime = 0;
	}

	public long getSeconds() {
		timeAccum = (System.currentTimeMillis() - startTime - totalPauseTime) / 1000;
		return timeSet - timeAccum;
	}

	public long getMS() {
		timeAccum = (System.currentTimeMillis() - startTime - (totalPauseTime));
		return msSet - timeAccum;
	}

	public void setMS(long l) {
		msSet = l;
		reset();
	}

	public void reset() {
		startTime = System.currentTimeMillis();
		onPauseTime = 0;
		onResumeTime = 0;
		totalPauseTime = 0;
	}

	public void onPauseTime() {
		onPauseTime = System.currentTimeMillis();
	}

	public void onResumeTime() {
		onResumeTime = System.currentTimeMillis();
		totalPauseTime = totalPauseTime + (onResumeTime - onPauseTime);
	}
}
