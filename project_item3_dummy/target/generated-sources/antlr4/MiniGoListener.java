// Generated from MiniGo.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MiniGoParser}.
 */
public interface MiniGoListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(MiniGoParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(MiniGoParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDecl(MiniGoParser.DeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDecl(MiniGoParser.DeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#var_decl}.
	 * @param ctx the parse tree
	 */
	void enterVar_decl(MiniGoParser.Var_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#var_decl}.
	 * @param ctx the parse tree
	 */
	void exitVar_decl(MiniGoParser.Var_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#type_spec}.
	 * @param ctx the parse tree
	 */
	void enterType_spec(MiniGoParser.Type_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#type_spec}.
	 * @param ctx the parse tree
	 */
	void exitType_spec(MiniGoParser.Type_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#fun_decl}.
	 * @param ctx the parse tree
	 */
	void enterFun_decl(MiniGoParser.Fun_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#fun_decl}.
	 * @param ctx the parse tree
	 */
	void exitFun_decl(MiniGoParser.Fun_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#params}.
	 * @param ctx the parse tree
	 */
	void enterParams(MiniGoParser.ParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#params}.
	 * @param ctx the parse tree
	 */
	void exitParams(MiniGoParser.ParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(MiniGoParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(MiniGoParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(MiniGoParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(MiniGoParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#expr_stmt}.
	 * @param ctx the parse tree
	 */
	void enterExpr_stmt(MiniGoParser.Expr_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#expr_stmt}.
	 * @param ctx the parse tree
	 */
	void exitExpr_stmt(MiniGoParser.Expr_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#assign_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAssign_stmt(MiniGoParser.Assign_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#assign_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAssign_stmt(MiniGoParser.Assign_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCompound_stmt(MiniGoParser.Compound_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCompound_stmt(MiniGoParser.Compound_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_stmt(MiniGoParser.If_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_stmt(MiniGoParser.If_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#for_stmt}.
	 * @param ctx the parse tree
	 */
	void enterFor_stmt(MiniGoParser.For_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#for_stmt}.
	 * @param ctx the parse tree
	 */
	void exitFor_stmt(MiniGoParser.For_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#return_stmt}.
	 * @param ctx the parse tree
	 */
	void enterReturn_stmt(MiniGoParser.Return_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#return_stmt}.
	 * @param ctx the parse tree
	 */
	void exitReturn_stmt(MiniGoParser.Return_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#local_decl}.
	 * @param ctx the parse tree
	 */
	void enterLocal_decl(MiniGoParser.Local_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#local_decl}.
	 * @param ctx the parse tree
	 */
	void exitLocal_decl(MiniGoParser.Local_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(MiniGoParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(MiniGoParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniGoParser#args}.
	 * @param ctx the parse tree
	 */
	void enterArgs(MiniGoParser.ArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniGoParser#args}.
	 * @param ctx the parse tree
	 */
	void exitArgs(MiniGoParser.ArgsContext ctx);
}