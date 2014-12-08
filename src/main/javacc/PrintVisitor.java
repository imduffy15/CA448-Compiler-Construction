public class PrintVisitor implements SimpLVisitor
{

	@Override
	public Object visit(SimpleNode node, Object data) {
		throw new RuntimeException("Visit SimpleNode");
	}

	@Override
	public Object visit(ASTprogram node, Object data) {
		node.jjtGetChild(0).jjtAccept(this, data);
		System.out.println(";");
		return data;
	}

	@Override
	public Object visit(ASTnumber node, Object data) {
		System.out.println(node.value);
		return data;
	}

	@Override
	public Object visit(ASTidentifier node, Object data) {
		System.out.println(node.value);
		return data;
	}
}
