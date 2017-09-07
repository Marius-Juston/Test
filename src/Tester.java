import java.awt.*;
import java.awt.event.KeyEvent;

public class Tester {
	public static void main(String[] args) throws AWTException {
		Robot robot = new Robot();

		for (int i =0;i < 10000; i++)
		{
			robot.keyPress(KeyEvent.VK_Y);
			robot.keyRelease(KeyEvent.VK_Y);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		}
	}
}