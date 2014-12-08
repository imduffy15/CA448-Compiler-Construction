import java.util.Hashtable;

public class TypeCheckingVisitor implements SimpLVisitor {
	@Override
	public Object visit(SimpleNode node, Object data) {
		throw new RuntimeException("Visit SimpleNode");
	}

	@Override
	public Object visit(ASTprogram node, Object data) {
		node.jjtGetChild(0).jjtAccept(this, data);
		return DataType.Program;
	}

	@Override
	public Object visit(ASTvar_decl node, Object data) {
		return(node.jjtGetChild(0).jjtAccept(this, data));
	}

	@Override
	public Object visit(ASTconst_decl node, Object data) {
		return(node.jjtGetChild(0).jjtAccept(this, data));
	}

	@Override
	public Object visit(ASTparam_list node, Object data) {
		return(node.jjtGetChild(0).jjtAccept(this, data));
	}

	@Override
	public Object visit(ASTexpression node, Object data) {
		return(node.jjtGetChild(0).jjtAccept(this, data));
	}

	@Override
	public Object visit(ASTnumber node, Object data) {
		return DataType.TypeInteger;
	}

	@Override
	public Object visit(ASTidentifier node, Object data) {
		@SuppressWarnings("unchecked")
		Hashtable<Object, STC> ST = (Hashtable<Object, STC>) data;
		STC hashTableEntry = ST.get(node.value);

		System.out.println(hashTableEntry);

//		if(hashTableEntry.type.equals("Int")) {
//			return DataType.TypeInteger;
//		} else if(hashTableEntry.type.equals("Bool")) {
//			return DataType.TypeBoolean;
//		} else if(hashTableEntry.type.equals("String")) {
//			return DataType.TypeString;
//		} else if(hashTableEntry.type.equals("Real")) {
//			return DataType.TypeReal;
//		}

		return DataType.TypeUnknown;
	}
}
