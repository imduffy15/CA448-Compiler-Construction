/* Generated By:JJTree: Do not edit this line. ASTnumber.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTnumber extends SimpleNode {
  public ASTnumber(int id) {
    super(id);
  }

  public ASTnumber(SimpL p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SimpLVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=a8580423a9b3a13af52c8ecc3508f548 (do not edit this line) */
