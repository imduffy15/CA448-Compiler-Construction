/* Generated By:JJTree: Do not edit this line. ASTArgList.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTArgList extends SimpleNode {
  public ASTArgList(int id) {
    super(id);
  }

  public ASTArgList(SimpL p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SimpLVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=d0fb1c94d575544c1ced089f2ab898fc (do not edit this line) */
