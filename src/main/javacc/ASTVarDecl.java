/* Generated By:JJTree: Do not edit this line. ASTVarDecl.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTVarDecl extends SimpleNode {
  public ASTVarDecl(int id) {
    super(id);
  }

  public ASTVarDecl(SimpL p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SimpLVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e699d081469bcfd5720466cba6edac3c (do not edit this line) */
