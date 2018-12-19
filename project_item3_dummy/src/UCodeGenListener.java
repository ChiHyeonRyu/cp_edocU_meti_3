import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import org.antlr.v4.runtime.tree.*;

/* Compiler(01): 201203405 Ryu Chi Hyeon */

public class UCodeGenListener extends MiniGoBaseListener {
	ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>();
	String space11 = "           ";
	int globalOffset = 1;
	int localOffset = 1;
	int paramOffset = 1;
	int localSize = 0;
	int labelNum = 0;
	boolean returnStmt = false;
	HashMap<String, Var> globalVar = new HashMap<>();
	HashMap<String, Var> localVar = new HashMap<>();
	
	/* Variable info */
	class Var {
		int base;
		int offset;
		boolean isParam;
		boolean isArray;
		
		Var(int base, int offset) {
			this.base = base;
			this.offset = offset;
		}
		
		Var(int base, int offset, boolean isArray) {
			this.base = base;
			this.offset = offset;
			this.isArray = isArray;
		}
		
		Var(int base, int offset, boolean isArray, boolean isParam) {
			this.base = base;
			this.offset = offset;
			this.isArray = isArray;
			this.isParam = isParam;
		}
	}

	/* Context Check */
	boolean isLITERAL(MiniGoParser.ExprContext ctx) {
		return ctx.getChildCount() == 1 && ctx.LITERAL(0) != null;
	}
	
	boolean isIDENT(MiniGoParser.ExprContext ctx) {
		return ctx.getChildCount() == 1 && ctx.IDENT() != null;	
	}

	boolean isRoundBracket(MiniGoParser.ExprContext ctx) {
		return ctx.getChildCount() == 3 && ctx.getChild(1) != ctx.args() && ctx.getChild(0).getText().equals("(");
	}

	boolean isArray(MiniGoParser.ExprContext ctx) {
		return ctx.getChildCount() == 4 && ctx.getChild(0).equals(ctx.IDENT()) && ctx.getChild(2) != ctx.args();
	}

	boolean isFunctionCall(MiniGoParser.ExprContext ctx) {
		return ctx.getChildCount() == 4 && ctx.getChild(1) != ctx.expr();
	}

	boolean isUnaryOperation(MiniGoParser.ExprContext ctx) {
		return ctx.getChildCount() == 2 && (ctx.getChild(0).getText().equals("-")
				|| ctx.getChild(0).getText().equals("+") || ctx.getChild(0).getText().equals("--")
				|| ctx.getChild(0).getText().equals("++") || ctx.getChild(0).getText().equals("!"));
	}

	boolean isBinaryOperation(MiniGoParser.ExprContext ctx) {
		return ctx.getChildCount() == 3 && ctx.getChild(1) != ctx.expr() && ctx.getChild(0) != ctx.LITERAL(0);
	}

	boolean isAssignmentOperation(MiniGoParser.ExprContext ctx) {
		return ctx.getChildCount() == 3 && ctx.getChild(0).equals(ctx.IDENT());
	}

	boolean isArrayAssignmentOperation(MiniGoParser.ExprContext ctx) {
		return ctx.getChildCount() == 6 && ctx.getChild(0).equals(ctx.IDENT());
	}

	/* program */
	@Override
	public void exitProgram(MiniGoParser.ProgramContext ctx) {
		String decl = null;
		try {
			FileWriter fw = new FileWriter("[01][201203405][·ùÄ¡Çö][03][1].uco");
			
			for (int i = 0; i < ctx.getChildCount(); i++) {
				decl = newTexts.get(ctx.decl(i));
				newTexts.put(ctx, decl);
				fw.write(newTexts.get(ctx) + "\n");
				System.out.println(newTexts.get(ctx));
			}
			
			String mainCall = "";
			mainCall += space11 + "bgn " + globalVar.size() + "\n";
			mainCall += space11 + "ldp\n";
			mainCall += space11 + "call main\n";
			mainCall += space11 + "end";
			
			fw.write(mainCall);
			System.out.print(mainCall);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* decl */
	@Override
	public void exitDecl(MiniGoParser.DeclContext ctx) {
		String var_decl = null, fun_decl = null;
		// decl (1): var_decl
		if (ctx.getChild(0).equals(ctx.var_decl())) {
			var_decl = newTexts.get(ctx.var_decl());
			newTexts.put(ctx, var_decl);
		}
		// decl (2): fun_decl
		if (ctx.getChild(0).equals(ctx.fun_decl())) {
			fun_decl = newTexts.get(ctx.fun_decl());
			newTexts.put(ctx, fun_decl);
		}
	}

	/* var_decl */
	@Override
	public void exitVar_decl(MiniGoParser.Var_declContext ctx) {
		int base = 1;
		// var_decl (1): VAR IDENT type_spec
		if (ctx.getChildCount() == 3) {
			newTexts.put(ctx, space11 + "sym " + base + " " + globalOffset + " " + 1);
			globalVar.put(ctx.IDENT(0).getText(), new Var(base, globalOffset, false));
			globalOffset++;
		}
		// var_decl (2): VAR IDENT ',' IDENT type_spec
		if (ctx.getChildCount() == 5) {
			String temp = "";
			for (int i = 0; i < 2; i++) {
				temp += space11 + "sym " + base + " " + globalOffset + " " + 1;
				if (i == 0)
					temp += "\n";
				globalVar.put(ctx.IDENT(0).getText(), new Var(base, globalOffset, false));
				globalOffset++;
			}
			newTexts.put(ctx, temp);
		}
		// var_decl (3): VAR IDENT '[' LITERAL ']' type_spec
		if (ctx.getChildCount() == 6) {
			String LITERAL = ctx.LITERAL().getText();
			newTexts.put(ctx, space11 + "sym " + base + " " + globalOffset + " " + LITERAL);
			globalVar.put(ctx.IDENT(0).getText(), new Var(base, globalOffset, true));
			globalOffset += Integer.parseInt(LITERAL);
		}
	}

	/* type_spec */
	@Override
	public void exitType_spec(MiniGoParser.Type_specContext ctx) {
		String INT = null, VOID = null, NULL = null;
		// type_spec (3):
		if (ctx.getChild(0) == null) {
			NULL = "";
			newTexts.put(ctx, NULL);
		}
		// type_spec (1): INT
		else if (ctx.getChild(0).getText().equals(ctx.INT().getText())) { 
			INT = ctx.INT().getText();
			newTexts.put(ctx, INT);
		} 
		// type_spec (2): VOID
		else if (ctx.getChild(1).getText().equals(ctx.VOID().getText())) {
			VOID = ctx.VOID().getText();
			newTexts.put(ctx, VOID);
		}
	}

	/* fun_decl */
	@Override
	public void exitFun_decl(MiniGoParser.Fun_declContext ctx) {
		String IDENT = null, params = null, compound_stmt = null;
		int blockNumber = 2;
		int lexicalLevel = 2;
		String space = "";
		for(int i = 0 ; i < 11 - ctx.IDENT().getText().length(); i++)
            space += " ";
		int localSize = this.localOffset - 1;
		int arguSize;
		if (ctx.params().getChildCount() != 0)
			arguSize = ctx.params().getChildCount() / 2 + 1;
		else
			arguSize = 0;
		
		// fun_delc (1): FUNC IDENT '(' params ')' type_spec compound_stmt
		if (ctx.getChildCount() == 7) {  
			IDENT = ctx.IDENT().getText();
			params = newTexts.get(ctx.params());
			compound_stmt = newTexts.get(ctx.compound_stmt());
			int p1 = localSize + arguSize;
			if (!params.equals("")) {
				p1 -= arguSize;
				if (returnStmt == true)
					newTexts.put(ctx, IDENT + space + "proc " + p1 + " " + blockNumber + " " + lexicalLevel + "\n" + params + "\n" + compound_stmt + "\n" + space11 + "end");
				else
					newTexts.put(ctx, IDENT + space + "proc " + p1 + " " + blockNumber + " " + lexicalLevel + "\n" + params + "\n" + compound_stmt + "\n" + space11 + "ret" + "\n" + space11 + "end");
			} else {
				if (returnStmt == true)
					newTexts.put(ctx, IDENT + space + "proc " + p1 + " " + blockNumber + " " + lexicalLevel + "\n" + compound_stmt + "\n" + space11 + "end");
				else
					newTexts.put(ctx, IDENT + space + "proc " + p1 + " " + blockNumber + " " + lexicalLevel + "\n" + compound_stmt + "\n" + space11 + "ret" + "\n" + space11 + "end");
			}
		} 
		// fun_delc (2): FUNC IDENT '(' params ')' '(' type_spec ',' type_spec ')' compound_stmt;
		else if (ctx.getChildCount() == 11) { 
			IDENT = ctx.IDENT().getText();
			params = newTexts.get(ctx.params());
			compound_stmt = newTexts.get(ctx.compound_stmt());
			int p1 = localSize + arguSize;
			if (!params.equals("")) {
				p1 -= arguSize;
				if (returnStmt == true)
					newTexts.put(ctx, IDENT + space + "proc " + p1 + " " + blockNumber + " " + lexicalLevel + "\n" + params + "\n" + compound_stmt + "\n" + space11 + "end");
				else
					newTexts.put(ctx, IDENT + space + "proc " + p1 + " " + blockNumber + " " + lexicalLevel + "\n" + params + "\n" + compound_stmt + "\n" + space11 + "ret" + "\n" + space11 + "end");
			} else {
				if (returnStmt == true)
					newTexts.put(ctx, IDENT + space + "proc " + p1 + " " + blockNumber + " " + lexicalLevel + "\n" + compound_stmt + "\n" + space11 + "end");
				else
					newTexts.put(ctx, IDENT + space + "proc " + p1 + " " + blockNumber + " " + lexicalLevel + "\n" + params + "\n" + compound_stmt + "\n" + space11 + "ret" + "\n" + space11 + "end");
			}		
		}
		
		this.localOffset = 1;
		this.returnStmt = false;
	}

	/* params */
	@Override
	public void exitParams(MiniGoParser.ParamsContext ctx) {
		String params = ""; // params (1): 
		for (int i = 0; i < ctx.param().size(); i++) { // params (2): param(',' param)*
				params += newTexts.get(ctx.param(i)) + "\n";
			}
		if (!params.equals("")) {
			params = params.substring(0, params.length() - 1);
		}
		newTexts.put(ctx, params);
		paramOffset = 1;
	}

	/* param */
	@Override
	public void exitParam(MiniGoParser.ParamContext ctx) {
		String IDENT = null;
		int base = 2;
		// param (1): IDENT type_spec
		if (ctx.getChildCount() == 2) { 			
			IDENT = ctx.IDENT().getText();
			newTexts.put(ctx, space11 + "sym " + base + " " + paramOffset + " " + 1);
			localVar.put(IDENT, new Var(base, paramOffset, false, true));
			paramOffset++;
			localOffset++;
		}
		// param (2): IDENT '[' ']' type_spec
		if (ctx.getChildCount() == 4) { 
			IDENT = ctx.IDENT().getText();
			newTexts.put(ctx, space11 + "sym " + base + " " + paramOffset + " " + 1);
			localVar.put(ctx.IDENT().getText(), new Var(base, paramOffset, true, true));
			paramOffset++;
			localOffset++;
		}
	}

	/* stmt */
	@Override
	public void exitStmt(MiniGoParser.StmtContext ctx) {
		String expr_stmt = null, compound_stmt = null, assign_stmt = null, if_stmt = null, for_stmt = null, return_stmt = null;
		// stmt (1): expr_stmt
		if (ctx.getChild(0).equals(ctx.expr_stmt())) {
			expr_stmt = newTexts.get(ctx.expr_stmt());
			newTexts.put(ctx, expr_stmt);
		}
		// stmt (2): compound_stmt
		if (ctx.getChild(0).equals(ctx.compound_stmt())) { 
			compound_stmt = newTexts.get(ctx.compound_stmt());
			newTexts.put(ctx, compound_stmt);
		}
		// stmt (3): assign_stmt
		if (ctx.getChild(0).equals(ctx.assign_stmt())) {
			assign_stmt = newTexts.get(ctx.assign_stmt());
			newTexts.put(ctx, assign_stmt);
		}
		// stmt (4): if_stmt
		if (ctx.getChild(0).equals(ctx.if_stmt())) {
			if_stmt = newTexts.get(ctx.if_stmt());
			newTexts.put(ctx, if_stmt);
		}
		// stmt (5): for_stmt
		if (ctx.getChild(0).equals(ctx.for_stmt())) {
			for_stmt = newTexts.get(ctx.for_stmt());
			newTexts.put(ctx, for_stmt);
		}
		// stmt (6): return_stmt
		if (ctx.getChild(0).equals(ctx.return_stmt())) {
			return_stmt = newTexts.get(ctx.return_stmt());
			newTexts.put(ctx, return_stmt);
		}
	}

	/* expr_stmt */
	@Override
	public void exitExpr_stmt(MiniGoParser.Expr_stmtContext ctx) {
		// expr_stmt (1): expr
		String expr = null;
		expr = newTexts.get(ctx.expr());
		newTexts.put(ctx, expr);
	}
	
	/* assign_stmt */
	@Override
	public void exitAssign_stmt(MiniGoParser.Assign_stmtContext ctx) {
		String IDENT0 = null, IDENT1 = null, LITERAL0 = null, LITERAL1 = null, expr0 = null, expr1 = null;
		int base = 2;
		// assign_stmt (1): VAR IDENT ',' IDENT type_spec '=' LITERAL ',' LITERAL
		if (ctx.getChildCount() == 9) {
			IDENT0 = ctx.IDENT(0).getText();
			IDENT1 = ctx.IDENT(1).getText();
			LITERAL0 = ctx.LITERAL(0).getText();
			LITERAL1 = ctx.LITERAL(1).getText();
			
			String assignStmt = "";
			localVar.put(IDENT0, new Var(base, localOffset));
			assignStmt += space11 + "sym " + base + " " + localOffset + " " + 1 + "\n";
			
			localOffset++;
			localVar.put(IDENT1, new Var(base, localOffset));
			assignStmt += space11 + "sym " + base + " " + localOffset + " " + 1 + "\n";
			
			assignStmt += space11 + "ldc " + LITERAL0 + "\n";
			assignStmt += space11 + "str " + localVar.get(IDENT0).base + " " + localVar.get(IDENT0).offset + "\n";
			
			assignStmt += space11 + "ldc " + LITERAL1 + "\n";
			assignStmt += space11 + "str " + localVar.get(IDENT1).base + " " + localVar.get(IDENT1).offset;
			
			newTexts.put(ctx, assignStmt);
			localOffset++;
		}
		// assign_stmt (2): VAR IDENT type_spec '=' expr
		if (ctx.getChildCount() == 5) {
			IDENT0 = ctx.IDENT(0).getText();
			expr0 = newTexts.get(ctx.expr(0));
			
			String assignStmt = "";
			localVar.put(IDENT0, new Var(base, localOffset));
			assignStmt += space11 + "sym " + base + " " + localOffset + " " + 1 + "\n";
			assignStmt += expr0 + "\n" + space11 + "str " + localVar.get(IDENT0).base + " " + localVar.get(IDENT0).offset;
			newTexts.put(ctx, assignStmt);
			localOffset++;
		}
		// assign_stmt (3): IDENT type_spec '=' expr
		if (ctx.getChildCount() == 4) {
			IDENT0 = ctx.IDENT(0).getText();
			expr0 = newTexts.get(ctx.expr(0));
			
			if (localVar.containsKey(IDENT0))
				newTexts.put(ctx, expr0 + "\n" + space11 + "str " + localVar.get(IDENT0).base + " " + localVar.get(IDENT0).offset);
			else if (globalVar.containsKey(IDENT0))
				newTexts.put(ctx, expr0 + "\n" + space11 + "str " + globalVar.get(IDENT0).base + " " + globalVar.get(IDENT0).offset);
		}
		
		// assign_stmt (4): IDENT '[' expr ']' '=' expr
		if (ctx.getChildCount() == 6) {
			IDENT0 = ctx.IDENT(0).getText();
			expr0 = newTexts.get(ctx.expr(0));
			expr1 = newTexts.get(ctx.expr(1));
			
			String assignStmt = "";
			if (localVar.containsKey(IDENT0)) {
				assignStmt += expr0 + "\n";
				assignStmt += space11 + "lda " + localVar.get(IDENT0).base + " " + localVar.get(IDENT0).offset + "\n";
				assignStmt += space11 + "add\n";
				assignStmt += expr1 + "\n";
				assignStmt += space11 + "sti";
			}
			else if (globalVar.containsKey(IDENT0)) {
				assignStmt += expr0 + "\n";
				assignStmt += space11 + "lda " + globalVar.get(IDENT0).base + " " + globalVar.get(IDENT0).offset + "\n";
				assignStmt += space11 + "add\n";
				assignStmt += expr1 + "\n";
				assignStmt += space11 + "sti";
			}		
			newTexts.put(ctx, assignStmt);
		}
	}
	
	/* compound_stmt */
	@Override
	public void exitCompound_stmt(MiniGoParser.Compound_stmtContext ctx) {
		// compound_stmt (1): '{' local_decl* stmt* '}'
		String localDeclStmt = "";
		for (int i = 0; i < ctx.local_decl().size(); i++) {
			localDeclStmt += newTexts.get(ctx.local_decl(i)) + "\n";
		}
		for (int i = 0; i < ctx.stmt().size(); i++) {
			localDeclStmt += newTexts.get(ctx.stmt(i)) + "\n";
		}
		if (!localDeclStmt.equals(""))
			newTexts.put(ctx, localDeclStmt.substring(0, localDeclStmt.length() - 1));
		else
			newTexts.put(ctx, localDeclStmt);
	}
	
	/* if_stmt */
	@Override
	public void exitIf_stmt(MiniGoParser.If_stmtContext ctx) {
		String expr = null, compound_stmt0 = null, compound_stmt1 = null;
		int ifElseLabelNum = labelNum++;
		String ifstmt = "";
		expr = newTexts.get(ctx.expr());
		ifstmt += expr + "\n";
		ifstmt += space11 + "fjp $$" + ifElseLabelNum + "\n";
		
		String spaceElse = "";
		for(int i = 0 ; i < 9 - Integer.toString(ifElseLabelNum).length(); i++)
			spaceElse += " ";
		
		// if_stmt (1): IF expr compound_stmt
		if (ctx.getChildCount() == 3) { 
			compound_stmt0 = newTexts.get(ctx.compound_stmt(0));
			ifstmt += compound_stmt0 + "\n";
			ifstmt += "$$" + ifElseLabelNum + spaceElse + "nop";
		} 
		// if_stmt (2): IF expr compound_stmt ELSE compound_stmt
		else {
			int ifEndLabelNum = labelNum++;
			
			String spaceEnd = "";
			for(int i = 0 ; i < 9 - Integer.toString(ifEndLabelNum).length(); i++)
				spaceEnd += " ";
	
			compound_stmt0 = newTexts.get(ctx.compound_stmt(0));
			compound_stmt1 = newTexts.get(ctx.compound_stmt(1));
			ifstmt += compound_stmt0 + "\n";
			ifstmt += space11 + "ujp $$" + ifEndLabelNum + "\n";
			ifstmt += "$$" + ifElseLabelNum + spaceElse + "nop\n";
			ifstmt += compound_stmt1 + "\n";
			ifstmt += "$$" + ifEndLabelNum + spaceEnd + "nop";
		}
		newTexts.put(ctx, ifstmt);
	}

	/* for_stmt */
	@Override
	public void exitFor_stmt(MiniGoParser.For_stmtContext ctx) {
		// for_stmt (1): FOR expr compound_stmt
		String expr = null, compound_stmt = null;
		int startLabelNum = labelNum++;
		int endLabelNum = labelNum++;
		expr = newTexts.get(ctx.expr());
		compound_stmt = newTexts.get(ctx.compound_stmt());
		String spaceSLN = "";
		String spaceELN = "";
		for(int i = 0 ; i < 9 - Integer.toString(startLabelNum).length(); i++)
			spaceSLN += " ";
		for(int i = 0 ; i < 9 - Integer.toString(endLabelNum).length(); i++)
			spaceELN += " ";
		
		String inst = "";
		inst = "$$" + startLabelNum + spaceSLN + "nop\n";
		inst += expr + "\n";
		inst += space11 + "fjp $$" + endLabelNum + "\n"; 
		inst += compound_stmt + "\n";
		inst += space11 + "ujp $$" + startLabelNum + "\n";
		inst += "$$" + endLabelNum + spaceELN + "nop";
		
		newTexts.put(ctx, inst);
	}

	/* return_stmt */
	@Override
	public void exitReturn_stmt(MiniGoParser.Return_stmtContext ctx) {
		String expr0 = null, expr1 = null;

		if (ctx.getChildCount() == 4) { // return_stmt (1): RETURN expr ',' expr
			expr0 = newTexts.get(ctx.expr(0));
			expr1 = newTexts.get(ctx.expr(1));
			newTexts.put(ctx, expr0 + "\n" + expr1 + "\n" + space11 + "retv");
		}
		if (ctx.getChildCount() == 2) { // return_stmt (2): RETURN expr	
			expr0 = newTexts.get(ctx.expr(0));
			newTexts.put(ctx, expr0 + "\n" + space11 + "retv");
		}
		if (ctx.getChildCount() == 1) { // return_stmt (3): RETURN
			newTexts.put(ctx, space11 + "ret");
		}
		returnStmt = true;
	}
	
	/* local_decl */
	@Override
	public void exitLocal_decl(MiniGoParser.Local_declContext ctx) {
		int base = 2;
		// local_decl (1): VAR IDENT type_spec
		if (ctx.getChildCount() == 3) {
			newTexts.put(ctx, space11 + "sym " + base + " " + localOffset + " " + 1);
			localVar.put(ctx.IDENT().getText(), new Var(base, localOffset, false));
			localOffset++;
		}
		// local_decl (2): VAR IDENT '[' LITERAL ']' type_spec
		if (ctx.getChildCount() == 6) {
			String LITERAL = ctx.LITERAL().getText();
			newTexts.put(ctx, space11 + "sym " + base + " " + localOffset + " " + LITERAL);
			localVar.put(ctx.IDENT().getText(), new Var(base, localOffset, true));
			localOffset += Integer.parseInt(LITERAL);
		}
	}

	/* expr */
	@Override
	public void exitExpr(MiniGoParser.ExprContext ctx) {
		String s1 = null, s2 = null, op = null, expr0 = null, expr1 = null, IDENT = null, args = null;
		if (isLITERAL(ctx)) { // expr (2): (LITERAL|IDENT)
			s1 = ctx.LITERAL(0).getText();
			newTexts.put(ctx, space11 + "ldc " + s1);
		} else if (isIDENT(ctx)) { // expr (2): (LITERAL|IDENT)
			s1 = ctx.IDENT().getText();
			if (localVar.containsKey(s1)) {
				if (localVar.get(s1).isArray == false)
					newTexts.put(ctx, space11 + "lod " + localVar.get(s1).base + " " + localVar.get(s1).offset);
				else if (localVar.get(s1).isArray == true)
					newTexts.put(ctx, space11 + "lda " + localVar.get(s1).base + " " + localVar.get(s1).offset);
			} else if (globalVar.containsKey(s1)) {
				if (globalVar.get(s1).isArray == false)
					newTexts.put(ctx, space11 + "lod " + globalVar.get(s1).base + " " + globalVar.get(s1).offset);
				else if (globalVar.get(s1).isArray == true)
					newTexts.put(ctx, space11 + "lda " + globalVar.get(s1).base + " " + globalVar.get(s1).offset);
			}
		} else if (isRoundBracket(ctx)) { // expr (3): '(' expr ')'
			expr0 = newTexts.get(ctx.expr(0));
			newTexts.put(ctx, expr0);
		} else if (isArray(ctx)) { // expr (4): IDENT '[' expr ']'
			s1 = ctx.IDENT().getText();
			expr0 = newTexts.get(ctx.expr(0));
			if (localVar.containsKey(s1)) {
				if (localVar.get(s1).isParam == true)
					newTexts.put(ctx, expr0 + "\n" + space11 + "lod " + localVar.get(s1).base + " " + localVar.get(s1).offset + "\n" + space11 + "add\n" + space11 + "ldi");
				else if (localVar.get(s1).isParam == false)
					newTexts.put(ctx, expr0 + "\n" + space11 + "lda " + localVar.get(s1).base + " " + localVar.get(s1).offset + "\n" + space11 + "add\n" + space11 + "ldi");
			} else if (globalVar.containsKey(s1)) {
				if (globalVar.get(s1).isParam == true)
					newTexts.put(ctx, expr0 + "\n" + space11 + "lod " + globalVar.get(s1).base + " " + globalVar.get(s1).offset + "\n" + space11 + "add\n" + space11 + "ldi");
				else if (globalVar.get(s1).isParam == false)
					newTexts.put(ctx, expr0 + "\n" + space11 + "lda " + globalVar.get(s1).base + " " + globalVar.get(s1).offset + "\n" + space11 + "add\n" + space11 + "ldi");
			}
		} else if (isFunctionCall(ctx)) { // expr (1): IDENT '(' args ')'
			IDENT = ctx.IDENT().getText();
			args = newTexts.get(ctx.args());
			String FuncCall = "";
			FuncCall += space11 + "ldp\n";
			FuncCall += args + "\n";
			FuncCall += space11 + "call " + IDENT;
			newTexts.put(ctx, FuncCall);
		} else if (isUnaryOperation(ctx)) { // expr (6): op=('-'|'+'|'--'|'++'|'!') expr 
			op = ctx.getChild(0).getText();
			s1 = newTexts.get(ctx.expr(0)) + "\n";
			
			if (op.equals("-"))
				s1 += space11 + "neg\n";
			else if (op.equals("--")) 
				s1 += space11 + "dec\n" + space11 + "str " + localVar.get(ctx.expr(0).getText()).base + " "  + localVar.get(ctx.expr(0).getText()).offset;
			else if (op.equals("++")) 
				s1 += space11 + "inc\n" + space11 + "str " + localVar.get(ctx.expr(0).getText()).base + " "  + localVar.get(ctx.expr(0).getText()).offset;
			else if (op.equals("!")) 
				s1 += space11 + "notop\n";
			
			newTexts.put(ctx, s1);
		} else if (isBinaryOperation(ctx)) { // expr (7,8,9): expr op expr
			s1 = newTexts.get(ctx.expr(0));
			s2 = newTexts.get(ctx.expr(1));
			op = ctx.getChild(1).getText();
			String biUcode;
			
			if (op.equals("*")) 
				biUcode = space11 + "mult";
			else if (op.equals("/")) 
				biUcode = space11 + "div";
			else if (op.equals("%")) 
				biUcode = space11 + "mod";
			else if (op.equals("+")) 
				biUcode = space11 + "add";
			else if (op.equals("-")) 
				biUcode = space11 + "sub";
			else if (op.equals("==")) 
				biUcode = space11 + "eq";
			else if (op.equals("!=")) 
				biUcode = space11 + "ne";
			else if (op.equals("<=")) 
				biUcode = space11 + "le";
			else if (op.equals("<")) 
				biUcode = space11 + "lt";
			else if (op.equals(">=")) 
				biUcode = space11 + "ge";
			else if (op.equals(">")) 
				biUcode = space11 + "gt";
			else if (op.equals("and")) 
				biUcode = space11 + "and";
			else 
				biUcode = space11 + "or";
			newTexts.put(ctx, s1 + "\n" + s2 + "\n" + biUcode);
		}
		if (isAssignmentOperation(ctx)) { // expr (11): IDENT '=' expr
			s1 = ctx.IDENT().getText();
			s2 = newTexts.get(ctx.expr(0));
			if (localVar.containsKey(s1))
				newTexts.put(ctx, s2 + "\n" + space11 + "str " + localVar.get(s1).base + " " + localVar.get(s1).offset);
			else if (globalVar.containsKey(s1))
				newTexts.put(ctx, s2 + "\n" + space11 + "str " + globalVar.get(s1).base + " " + globalVar.get(s1).offset);
		}
		if (isArrayAssignmentOperation(ctx)) { // expr (12): IDENT '[' expr ']' '=' expr;
			s1 = ctx.IDENT().getText();
			expr0 = newTexts.get(ctx.expr(0));
			expr1 = newTexts.get(ctx.expr(1));
						
			if (localVar.containsKey(s1)) {
				if (localVar.get(s1).isParam == true)
					newTexts.put(ctx, expr0 + "\n" + space11 + "lod " + localVar.get(s1).base + " " + localVar.get(s1).offset + "\n" + space11 + "add\n" + expr1 + "\n" + space11 + "sti");
				else if (localVar.get(s1).isParam == false)
					newTexts.put(ctx, expr0 + "\n" + space11 + "lda " + localVar.get(s1).base + " " + localVar.get(s1).offset + "\n" + space11 + "add\n" + expr1 + "\n" + space11 + "sti");
			} else if (globalVar.containsKey(s1)) {
				if (globalVar.get(s1).isParam == true)
					newTexts.put(ctx, expr0 + "\n" + space11 + "lod " + globalVar.get(s1).base + " " + globalVar.get(s1).offset + "\n" + space11 + "add\n" + expr1 + "\n" + space11 + "sti");
				else if (globalVar.get(s1).isParam == false)
					newTexts.put(ctx, expr0 + "\n" + space11 + "lda " + globalVar.get(s1).base + " " + globalVar.get(s1).offset + "\n" + space11 + "add\n" + expr1 + "\n" + space11 + "sti");
			}
		}
	}

	/* args */
	@Override
	public void exitArgs(MiniGoParser.ArgsContext ctx) {
		String args = ""; // args (2)
		for (int i = 0; i < ctx.expr().size(); i++) { // args (1)
				args += newTexts.get(ctx.expr(i)) + "\n";
		}
		if(!args.equals("")) {
			args = args.substring(0, args.length() - 1);
		} 
		newTexts.put(ctx, args);
	}
}