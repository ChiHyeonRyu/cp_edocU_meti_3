import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ControlFlowAnalysis {
	private int lineCount = 1;
	private int totalLineNum;
	private HashMap<Integer, String> Leaders = new HashMap<>();
	private HashMap<Integer, ArrayList<String>> BasicBlocks = new HashMap<>();
	private HashMap<Integer, ArrayList<Integer>> CFG = new HashMap<>();
	private int[] leaderLineNum;

	// 1. Find Leaders in the U-code program
	public void findLeader(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;

			// Find Reader (1): First instruction of program
			line = br.readLine();
			Leaders.put(lineCount, line);
			lineCount++;

			while ((line = br.readLine()) != null) {
				// Find Reader (2): Target instruction of branch
				if (line.charAt(0) == '$') {
					Leaders.put(lineCount, line);

				}
				// Find Reader (3): Next instruction of branch instruction
				else if (line.charAt(12) == 'j') {
					line = br.readLine();
					lineCount++;
					Leaders.put(lineCount, line);
				}
				lineCount++;
			}

			totalLineNum = lineCount - 1;
			lineCount = 1;

			// Print Leaders
			System.out.println("------------------ Leader ------------------");
			leaderLineNum = new int[Leaders.size() + 1];
			int leaderNumber = 0;
			for (int i = 1; i <= totalLineNum; i++) {
				if (Leaders.containsKey(i)) {
					System.out.printf("%3d: %s\n", i, Leaders.get(i));
					leaderLineNum[leaderNumber] = i;
					leaderNumber++;
				}
			}
			leaderLineNum[leaderLineNum.length - 1] = totalLineNum;

			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("::: I/O error");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("::: I/O error");
			System.exit(1);
		}
	}

	// 2. Make Basic Blocks using leaders
	public void makeBasicBlock(String fileName) {
		ArrayList<String> ucodes;

		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;

			int leaderSize = Leaders.size();
			for (int i = 0; i < leaderSize; i++) {
				ucodes = new ArrayList<>();
				while ((lineCount == totalLineNum) || lineCount < leaderLineNum[i + 1]) {
					line = br.readLine();
					if (!Leaders.containsKey(lineCount)) {
						if (line.charAt(0) == ' ') {
							line = line.substring(11, line.length());
						}
					}
					ucodes.add(line);
					lineCount++;
				}
				BasicBlocks.put(i + 1, ucodes);
			}

			// Print Basic Blocks
			System.out.println("\n---------------- Basic Block ----------------");
			for (int i = 0; i < BasicBlocks.size(); i++) {
				int lnfirst = leaderLineNum[i];
				int lnlast = 0;
				if (i != BasicBlocks.size()) {
					lnlast = leaderLineNum[i + 1] - 1;
					if (i == BasicBlocks.size() - 1) {
						lnlast = leaderLineNum[i + 1];
					}
				}
				System.out.printf("BB%d :: %3d ~ %3d: %s\n", i + 1, lnfirst, lnlast, BasicBlocks.get(i + 1));
			}

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

	// 3. Make CFG using Basic Blocks
	public void makeCFG() {
		ArrayList<Integer> entryToBB1 = new ArrayList<>();
		entryToBB1.add(1);
		CFG.put(0, entryToBB1);
		for (int i = 1; i <= BasicBlocks.size(); i++) {
			ArrayList<Integer> targetBBList = new ArrayList<>();
			ArrayList<String> BB = BasicBlocks.get(i);
			String lastCode = BB.get(BB.size() - 1);
			String label = lastCode;
			int targetBB;

			// Branch (1): fjp
			if (lastCode.charAt(0) == 'f' && lastCode.charAt(1) == 'j') {
				if (i != BasicBlocks.size()) {
					targetBBList.add(i + 1);
				} else {
					targetBBList.add(-1);
				}
				label = label.substring(4, label.length());
				targetBB = findTargetBB(label);
				targetBBList.add(targetBB);

			}
			// Branch (2): ujp
			else if (lastCode.charAt(0) == 'u' && lastCode.charAt(1) == 'j') {
				label = label.substring(4, label.length());
				targetBB = findTargetBB(label);
				targetBBList.add(targetBB);
			}
			// Normal Code & 'Exit'
			else {
				if (i != BasicBlocks.size()) {
					targetBBList.add(i + 1);
				} else {
					targetBBList.add(-1);
				}
			}
			CFG.put(i, targetBBList);
		}
	}

	// Method: find label of branch target
	public int findTargetBB(String label) {
		String targetLB = null;
		for (int i = 1; i <= BasicBlocks.size(); i++) {
			targetLB = BasicBlocks.get(i).get(0);
			int lbLen = 0;
			int j = 0;
			while (targetLB.charAt(j) != ' ') {
				lbLen++;
				j++;
			}
			targetLB = targetLB.substring(0, lbLen);
			if (targetLB.equals(label)) {
				return i;
			}
		}
		return 0;
	}

	// 4. Print CFG
	public void drawCFG() {
		makeCFG();
		System.out.println("\n---------------- Control Flow Graph ----------------");
		System.out.printf("Entry -> BB1\n");
		for (int i = 1; i <= CFG.size(); i++) {
			if (CFG.get(i).contains(-1) && CFG.get(i).size() == 1) {
				System.out.printf("BB%d -> Exit", i); // Last node has only 'Exit'
				return;
			}
			for (int j = 0; j < CFG.get(i).size(); j++) {
				if (j == (CFG.get(i).size() - 1)) {
					System.out.printf("BB%d -> BB%d", i, CFG.get(i).get(j));
				} else if (CFG.get(i).get(j) == -1) // Last node has 'Exit' and other nodes
					System.out.printf("BB%d -> Exit", i);
				else {
					System.out.printf("BB%d -> BB%d , ", i, CFG.get(i).get(j));
				}
			}
			System.out.println();
		}
	}
}
