import com.thalmic.myo.*;
import com.thalmic.myo.enums.*;
import java.awt.*;
import java.awt.event.*;

public class DataCollector implements DeviceListener {
	private static final double SCALE = 18.0;
	private double rollW;
	private double pitchW;
	private double yawW;
	private Pose currentPose;
	private Arm whichArm;
	private Myo[] myos=new Myo[2];
	private MyoPaint parent;

	public DataCollector(MyoPaint p) {
		parent=p;
		rollW = 0;
		pitchW = 0;
		yawW = 0;
		currentPose = new Pose();
	}

	@Override
	public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
		Quaternion normalized = rotation.normalized();
		if(myo.equals(myos[0]))
		{
			double pitch = Math.asin(2.0f * (normalized.getW() * normalized.getY() - normalized.getZ() * normalized.getX()));
			double yaw = Math.atan2(2.0f * (normalized.getW() * normalized.getZ() + normalized.getX() * normalized.getY()), 1.0f - 2.0f * (normalized.getY() * normalized.getY() + normalized.getZ() * normalized.getZ()));
			pitchW = ((pitch + Math.PI / 2.0) / Math.PI * SCALE);
			yawW = ((yaw + Math.PI) / (Math.PI * 2.0) * SCALE);
		}
		else if(myo.equals(myos[1]))
		{
			double roll = Math.atan2(2.0f * (normalized.getW() * normalized.getX() + normalized.getY() * normalized.getZ()), 1.0f - 2.0f * (normalized.getX() * normalized.getX() + normalized.getY() * normalized.getY()));
			rollW = ((roll + Math.PI) / (Math.PI * 2.0) * SCALE);
		}
	}

	@Override
	public void onPose(Myo myo, long timestamp, Pose pose) {
		currentPose = pose;
		try
		{
			Robot robot=new Robot();
			if (currentPose.getType() == PoseType.FIST&&myo.equals(myos[1]))
				robot.mousePress(InputEvent.BUTTON1_MASK);
			else
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			if(currentPose.getType()==PoseType.WAVE_OUT&&myo.equals(myos[1]))
				parent.openMenu();
		}
		catch(Exception e)
		{
			System.err.println("Error: ");
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void onArmRecognized(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
		whichArm = arm;
	}

	@Override
	public void onArmLost(Myo myo, long timestamp) {
		whichArm = null;
	}

	@Override
	public void onAccelerometerData(Myo myo, long timestamp, Vector3 accel) {
	}

	@Override
	public void onConnect(Myo myo, long timestamp, FirmwareVersion firmwareVersion) {
		storeMyo(myo);
	}

	@Override
	public void onDisconnect(Myo myo, long timestamp) {
	}

	@Override
	public void onPair(Myo myo, long timestamp, FirmwareVersion firmwareVersion) {
		storeMyo(myo);
	}
	public void storeMyo(Myo myo)
	{
		Myo temp=myos[0];
		if(temp!=null&&temp!=myo)
		{
			temp=myos[1];
			if(temp!=null&&temp!=myo)
			{
				System.out.println("More than 1 connected. Closing");
				System.exit(0);
			}
			myos[1]=myo;
		}
		else
			myos[0]=myo;
	}
	@Override
	public void onUnpair(Myo myo, long timestamp) {
	}

	@Override
	public void onGyroscopeData(Myo myo, long timestamp, Vector3 gyro) {
	}

	@Override
	public void onRssi(Myo myo, long timestamp, int rssi) {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("\r");

		String xDisplay = String.format("[%s%s]", repeatCharacter('*', (int) rollW), repeatCharacter(' ', (int) (SCALE - rollW)));
		String yDisplay = String.format("[%s%s]", repeatCharacter('*', (int) pitchW), repeatCharacter(' ', (int) (SCALE - pitchW)));
		String zDisplay = String.format("[%s%s]", repeatCharacter('*', (int) yawW), repeatCharacter(' ', (int) (SCALE - yawW)));

		String poseString = null;
		if (whichArm != null) {
			String poseTypeString = currentPose.getType()
					.toString();
			poseString = String.format("[%s][%s%" + (SCALE - poseTypeString.length()) + "s]", whichArm == Arm.ARM_LEFT ? "L" : "R", poseTypeString, " ");
		} else {
			poseString = String.format("[?][%14s]", " ");
		}

		builder.append(xDisplay);
		builder.append(yDisplay);
		builder.append(zDisplay);
		builder.append(poseString);
		return builder.toString();
	}

	private String repeatCharacter(char character, int numOfTimes) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < numOfTimes; i++) {
			builder.append(character);
		}
		return builder.toString();
	}
	public double getPitch()
	{
		return pitchW;
	}
	public double getRoll()
	{
		return rollW;
	}
	public double getYaw()
	{	
		return yawW;
	}
}