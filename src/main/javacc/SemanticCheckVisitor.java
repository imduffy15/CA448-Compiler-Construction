import java.util.*;

public class SemanticCheckVisitor implements SimpLVisitor {

	public static String oldScope = "global";
	public static String scope = "global";
	public static int scopeCounter = 0;

	public static LinkedHashSet<String> declaredFunctions = new LinkedHashSet<>();
	public static LinkedHashSet<String> calledFunctions = new LinkedHashSet<>();

	public static HashMap<String, HashMap<String, STC>> symbolTable = new HashMap<String, HashMap<String, STC>>();

	@Override
	public Object visit(SimpleNode node, Object data) {
		System.out.println(node);
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTProgram node, Object data) {

		symbolTable.put(scope, new HashMap<String, STC>());

		node.childrenAccept(this, data);

		if (declaredFunctions.size() == calledFunctions.size()) {
			System.out.println("All functions were called");
		} else {
			System.out.println("All functions were not called");
		}

		for (String scope : symbolTable.keySet()) {
			HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
			for (String key : scopedSymbolTable.keySet()) {
				STC symbolTableChild = scopedSymbolTable.get(key);
				if (!symbolTableChild.isFunction()) {
					if (symbolTableChild.getData("written") == null && symbolTableChild.getData("readFrom") == null) {
						System.out.println(symbolTableChild.getIdentifier() + " in scope " + symbolTableChild.getScope() + " is not written to or read from");
					} else if (symbolTableChild.getData("written") == null && symbolTableChild.getData("readFrom") == true) {
						System.out.println(symbolTableChild.getIdentifier() + " in scope " + symbolTableChild.getScope() + " is not written to but is read from");
					} else if (symbolTableChild.getData("written") == true && symbolTableChild.getData("readFrom") == null) {
						System.out.println(symbolTableChild.getIdentifier() + " in scope " + symbolTableChild.getScope() + " is written but is not read from");
					} else if (symbolTableChild.getData("written") == true && symbolTableChild.getData("readFrom") == true) {
						System.out.println(symbolTableChild.getIdentifier() + " in scope " + symbolTableChild.getScope() + " is written and read from");
					}
				}
			}
		}

		System.out.println();
		System.out.println("Symbol Table:");

		System.out.printf("%-15s%-15s%-15s%-15s%n", "Type", "Identifier", "Scope", "Data");
		for (String scope : symbolTable.keySet()) {
			HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
			for (String key : scopedSymbolTable.keySet()) {
				STC symbolTableChild = scopedSymbolTable.get(key);
				System.out.printf("%-15s%-15s%-15s%-15s%n", symbolTableChild.getType(), symbolTableChild.getIdentifier(), symbolTableChild.getScope(), symbolTableChild.getData());
			}
		}

		return null;
	}

	@Override
	public Object visit(ASTVarDecl node, Object data) {

		List<Token> identList = (List<Token>) node.jjtGetChild(0).jjtAccept(this, data);
		Token type = (Token) node.jjtGetChild(1).jjtAccept(this, data);

		for (int i = 0; i < identList.size(); i++) {
			HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
			if (scopedSymbolTable == null) scopedSymbolTable = new HashMap<String, STC>();

			Token identifier = identList.get(i);
			STC variable = new STC(identifier, type, scope);

			if (scopedSymbolTable.get(identifier.image) != null) {
				System.out.println("Error: Identifier " + identifier.image + " already declared in " + scope);
			} else {
				scopedSymbolTable.put(identifier.image, variable);
				symbolTable.put(scope, scopedSymbolTable);
				System.out.println("Variable definition logged: " + variable);
			}
		}

		return null;
	}

	@Override
	public Object visit(ASTConstDecl node, Object data) {
		node.childrenAccept(this, data);
		System.out.println(node);
		return null;
	}

	@Override
	public Object visit(ASTFunctionDecl node, Object data) {

		Token type = (Token) node.jjtGetChild(0).jjtAccept(this, data);
		Token identifier = (Token) ((SimpleNode)node.jjtGetChild(1)).jjtGetValue();
		List<Token> paramList = (List<Token>) node.jjtGetChild(2).jjtAccept(this, data);
		Node functionBodyNode = node.jjtGetChild(3);

		STC function = new STC(identifier, type, scope, true);
		function.addData("paramList", paramList);

		HashMap<String, STC> globalSymbolTable = symbolTable.get(scope);

		if (globalSymbolTable.get(identifier.image) != null) {
			System.out.println("Error: Identifier " + identifier.image + " already declared in " + scope);
		} else {
			globalSymbolTable.put(identifier.image, function);
			symbolTable.put(scope, globalSymbolTable);
			declaredFunctions.add(identifier.image);
			System.out.println("Function definition logged: " + function);
		}

		oldScope = scope;
		scope = "function-" + ++scopeCounter;
		System.out.println("Setting scope to " + scope);

		for (int i = 0; i < paramList.size() - 1; ) {
			HashMap<String, STC> functionSymbolTable = symbolTable.get(scope);
			Token paramIdentifier = paramList.get(i);
			Token paramType = paramList.get(++i);
			STC scopedVariable = new STC(paramIdentifier, paramType, scope);
			if (functionSymbolTable == null) functionSymbolTable = new HashMap<String, STC>();

			if (functionSymbolTable.get(paramIdentifier.image) != null) {
				System.out.println("Error: Identifier " + paramType.image + " already declared in " + scope);
			} else {
				functionSymbolTable.put(paramIdentifier.image, scopedVariable);
				symbolTable.put(scope, functionSymbolTable);
				System.out.println("Variable definition logged: " + scopedVariable);
			}
		}

		functionBodyNode.jjtAccept(this, data);

		return null;
	}

	@Override
	public Object visit(ASTFunctionBody node, Object data) {
		node.childrenAccept(this, data);
		System.out.println("Function finished.");
		System.out.println("Setting scope to " + oldScope);
		scope = oldScope;
		return null;
	}

	@Override
	public Object visit(ASTParamList node, Object data) {
		List<Token> paramList = new ArrayList();

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			paramList.add((Token) ((SimpleNode)node.jjtGetChild(i)).jjtGetValue());
		}

		return paramList;
	}

	@Override
	public Object visit(ASTType node, Object data) {
		return node.jjtGetValue();
	}

	@Override
	public Object visit(ASTMain node, Object data) {
		scope = "main";
		System.out.println("Setting scope to main");
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTAssignment node, Object data) {

		Token identifier = (Token) node.jjtGetChild(0).jjtAccept(this, data);

		List<Token> assignment;

		if (node.jjtGetChild(1).jjtAccept(this, data) instanceof Token) {
			assignment = new ArrayList<Token>();
			assignment.add((Token) node.jjtGetChild(1).jjtAccept(this, data));
		} else {
			assignment = (List<Token>) node.jjtGetChild(1).jjtAccept(this, data);
		}

		HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);

		STC scopedVariable = scopedSymbolTable.get(identifier.image);
		if(scopedVariable != null) {
			scopedVariable.addData("written", true);
			scopedVariable.addData("value", assignment);
		}

		HashMap<String, STC> globalSymbolTable = symbolTable.get("global");

		STC globalVariable = globalSymbolTable.get(identifier.image);
		if(globalVariable != null) {
			globalVariable.addData("written", true);
			globalVariable.addData("value", assignment);
		}


		return null;
	}

	@Override
	public Object visit(ASTFunctionCall node, Object data) {

		Token identifier = (Token) node.jjtGetChild(0).jjtAccept(this, data);
		List<Token> arguments = (List<Token>) node.jjtGetChild(1).jjtAccept(this, data);

		HashMap<String, STC> globalSymbolTable = symbolTable.get("global");

		STC possibleFunction = globalSymbolTable.get(identifier.image);
		if (possibleFunction != null) {
			if (possibleFunction.isFunction()) {
				calledFunctions.add(identifier.image);
				if (null != possibleFunction.getData("paramList")) {
					List<Token> paramList = (List<Token>) possibleFunction.getData("paramList");
					if (paramList.size() > 0) {
						if (paramList.size() / 2 != arguments.size()) {
							System.out.println("Error: incorrect number of function arguments");
						}
					}
				}
			} else {
				System.out.println("Error: no such function " + identifier);
			}
		} else {
			System.out.println("Error: no such function " + identifier);
		}

		return null;
	}

	@Override
	public Object visit(ASTAddExpr node, Object data) {
		return Arrays.asList(node.jjtGetChild(0).jjtAccept(this, data), node.jjtGetValue(), node.jjtGetChild(1).jjtAccept(this, data));
	}

	@Override
	public Object visit(ASTSubExpr node, Object data) {
		return Arrays.asList(node.jjtGetChild(0).jjtAccept(this, data), node.jjtGetValue(), node.jjtGetChild(1).jjtAccept(this, data));
	}

	@Override
	public Object visit(ASTMultExpr node, Object data) {
		return Arrays.asList(node.jjtGetChild(0).jjtAccept(this, data), node.jjtGetValue(), node.jjtGetChild(1).jjtAccept(this, data));
	}

	@Override
	public Object visit(ASTDivExpr node, Object data) {
		return Arrays.asList(node.jjtGetChild(0).jjtAccept(this, data), node.jjtGetValue(), node.jjtGetChild(1).jjtAccept(this, data));
	}

	@Override
	public Object visit(ASTModExpr node, Object data) {
		return Arrays.asList(node.jjtGetChild(0).jjtAccept(this, data), node.jjtGetValue(), node.jjtGetChild(1).jjtAccept(this, data));
	}

	@Override
	public Object visit(ASTIdentList node, Object data) {
		List<Token> identList = new ArrayList();

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			identList.add((Token) ( (SimpleNode) node.jjtGetChild(i)).jjtGetValue());
		}

		return identList;
	}

	@Override
	public Object visit(ASTArgList node, Object data) {
		List<Token> argList = new ArrayList();

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			argList.add((Token) ((SimpleNode)node.jjtGetChild(i)).jjtGetValue());
		}

		return argList;
	}

	@Override
	public Object visit(ASTNumber node, Object data) {
		return node.jjtGetValue();
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {

		Token identifier = (Token) node.jjtGetValue();

		HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
		HashMap<String, STC> globalSymbolTable = symbolTable.get("global");

		if (scopedSymbolTable != null && globalSymbolTable != null) {
			if (scopedSymbolTable.get(identifier.image) == null && globalSymbolTable.get(identifier.image) == null) {
				System.out.println("Error: attempt to use undeclared identifier " + identifier.image);
			}
		}

		if (scopedSymbolTable != null) {
			if (scopedSymbolTable.get(identifier.image) != null) {
				STC scopedVariable = scopedSymbolTable.get(identifier.image);
				scopedVariable.addData("readFrom", true);
				scopedSymbolTable.put(identifier.image, scopedVariable);
			}
		}

		if (globalSymbolTable != null) {
			if (globalSymbolTable.get(identifier.image) != null) {
				STC globalVariable = globalSymbolTable.get(identifier.image);
				globalVariable.addData("readFrom", true);
				globalSymbolTable.put(identifier.image, globalVariable);
			}
		}

		return node.jjtGetValue();
	}
}
