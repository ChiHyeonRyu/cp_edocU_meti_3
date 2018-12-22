import java.io.IOException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class UcodeGenerator {
	public static final String FILE_GO = "test.go";
	public static final String FILE_UCODE = "ucode.uco";
	
	public static void main(String... args) throws IOException {
		MiniGoLexer lexer = new MiniGoLexer(CharStreams.fromFileName(FILE_GO));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MiniGoParser parser = new MiniGoParser(tokens);
		ParseTree tree = parser.program();
		
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new UCodeGenListener(), tree);
		
		System.out.println("\n\n=============== Control Flow Analysis ===============\n");
		ControlFlowAnalysis cfa = new ControlFlowAnalysis();
		cfa.findLeader(FILE_UCODE);
		cfa.makeBasicBlock(FILE_UCODE);
		cfa.drawCFG();
	}
}
