/* Generated By:JavaCC: Do not edit this line. SimpLVisitor.java Version 6.0_1 */
public interface SimpLVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(ASTprogram node, Object data);
  public Object visit(ASTvar_decl node, Object data);
  public Object visit(ASTconst_decl node, Object data);
  public Object visit(ASTparam_list node, Object data);
  public Object visit(ASTexpression node, Object data);
  public Object visit(ASTnumber node, Object data);
  public Object visit(ASTidentifier node, Object data);
}
/* JavaCC - OriginalChecksum=c613982301ba3802cc23180b6e2a7a6b (do not edit this line) */