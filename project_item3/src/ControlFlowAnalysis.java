import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ControlFlowAnalysis {
	public int lineCount = 1;
	public int totalLineNum;
	public HashMap<Integer, ArrayList<String>> BasicBlocks = new HashMap<>();
	public HashMap<Integer, String> Leader = new HashMap<>();

	public void findLeader(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;

			// Find Reader (1): First instruction of program
			line = br.readLine();
			// line = line.substring(11, line.length());
			Leader.put(lineCount, line);
			lineCount++;

			while ((line = br.readLine()) != null) {
				// Find Reader (2): Target instruction of branch
				if (line.charAt(0) == '$') {
					Leader.put(lineCount, line);

				}
				// Find Reader (3): Next instruction of branch instruction
				else if (line.charAt(12) == 'j') {
					line = br.readLine();
					lineCount++;
					Leader.put(lineCount, line);
				}
				lineCount++;
			}

			System.out.println("------------------ Leader ------------------");
			for (int i = 1; i <= 28; i++) {
				if (Leader.containsKey(i)) {
					System.out.printf("%3d: %s\n", i, Leader.get(i));
				}
			}
			
			totalLineNum = lineCount - 1;
			lineCount = 1;
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("::: I/O error");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("::: I/O error");
			System.exit(1);
		}
	}

	public void makeBasicBlock(String fileName) {
	
	}
}
