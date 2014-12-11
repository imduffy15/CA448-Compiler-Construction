import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class SemanticCheckVisitor implements SimpLVisitor {

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
		node.childrenAccept(this, data);

		if(declaredFunctions.size() == calledFunctions.size()) {
			System.out.println("All functions were called");
		} else {
			System.out.println("All functions were not called");
		}

		for(String scope : symbolTable.keySet()) {
			HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
			for(String key : scopedSymbolTable.keySet()) {
				STC symbolTableChild = scopedSymbolTable.get(key);
				if(!symbolTableChild.isFunction()) {
					if(symbolTableChild.getData("written") == null && symbolTableChild.getData("readFrom") == null) {
						System.out.println(symbolTableChild.getIdentifier() + " in scope " + symbolTableChild.getScope() + " is not written to or read from");
					}
					else if(symbolTableChild.getData("written") == null && symbolTableChild.getData("readFrom") == true) {
						System.out.println(symbolTableChild.getIdentifier() + " in scope " + symbolTableChild.getScope() + " is not written to but is read from");
					}
					else if(symbolTableChild.getData("written") == true && symbolTableChild.getData("readFrom") == null) {
						System.out.println(symbolTableChild.getIdentifier() + " in scope " + symbolTableChild.getScope() + " is written but is not read from");
					}
					else if(symbolTableChild.getData("written") == true && symbolTableChild.getData("readFrom") == true) {
						System.out.println(symbolTableChild.getIdentifier()  + " in scope " + symbolTableChild.getScope() + " is written and read from");
					}
				}
			}
		}

		System.out.println();
		System.out.println("Symbol Table:");

		System.out.printf("%-15s%-15s%-15s%-15s%n", "Type", "Identifier", "Scope", "Data");
		for(String scope : symbolTable.keySet()) {
			HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
			for(String key : scopedSymbolTable.keySet()) {
				STC symbolTableChild = scopedSymbolTable.get(key);
				System.out.printf("%-15s%-15s%-15s%-15s%n", symbolTableChild.getType(), symbolTableChild.getIdentifier(), symbolTableChild.getScope(), symbolTableChild.getData());
			}
		}

		return null;
	}

	@Override
	public Object visit(ASTVarDecl node, Object data) {
		if(node.jjtGetValue() != null) {
			List<Object> values = (List) node.jjtGetValue();
			Token type = (Token) values.get(0);
			List<Token> identList = (List<Token>) values.get(1);

			for(int i=0;i<identList.size();i++) {
				HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
				Token identifier = identList.get(i);
				STC scopedVariable = new STC(identifier, type, scope);
				if(scopedSymbolTable == null) scopedSymbolTable = new HashMap<String, STC>();

				if (scopedSymbolTable.get(identifier.image) != null) {
					System.out.println("Error: Identifier " + identifier.image + " already declared in " + scope);
				} else {
					scopedSymbolTable.put(identifier.image, scopedVariable);
					symbolTable.put(scope, scopedSymbolTable);
					System.out.println("Variable definition logged: " + scopedVariable);
				}
			}

		}

		return null;
	}

	@Override
	public Object visit(ASTconst_decl node, Object data) {
		node.childrenAccept(this, data);
		System.out.println(node);
		return null;
	}

	@Override
	public Object visit(ASTFunction node, Object data) {

		if(node.jjtGetValue() != null) {
			List<Object> values = (List) node.jjtGetValue();
			Token type = (Token) values.get(0);
			Token identifier = (Token) values.get(1);
			List<Token> paramList = (List<Token>) values.get(2);

			STC function = new STC(identifier, type, scope, true);
			function.addData("paramList", paramList);

			HashMap<String, STC> globalSymbolTable = symbolTable.get(scope);
			if (globalSymbolTable == null) globalSymbolTable = new HashMap<String, STC>();

			if (globalSymbolTable.get(identifier.image) != null) {
				System.out.println("Error: Identifier " + identifier.image + " already declared in " + scope);
			} else {
				globalSymbolTable.put(identifier.image, function);
				symbolTable.put(scope, globalSymbolTable);
				declaredFunctions.add(identifier.image);
				System.out.println("Function definition logged: " + function);
			}

			String oldScope = scope;
			scope = "function-" + ++scopeCounter;
			System.out.println("Setting scope to " + scope);

			for(int i=0;i<paramList.size()-1;) {
				HashMap<String, STC> functionSymbolTable = symbolTable.get(scope);
				Token scopedIdentifier = paramList.get(i);
				Token scopedType = paramList.get(++i);
				STC scopedVariable = new STC(scopedIdentifier, scopedType, scope);
				if(functionSymbolTable == null) functionSymbolTable = new HashMap<String, STC>();

				if (functionSymbolTable.get(scopedIdentifier.image) != null) {
					System.out.println("Error: Identifier " + scopedIdentifier.image + " already declared in " + scope);
				} else {
					functionSymbolTable.put(scopedIdentifier.image, scopedVariable);
					symbolTable.put(scope, functionSymbolTable);
					System.out.println("Variable definition logged: " + scopedVariable);
				}
			}

			node.childrenAccept(this, data);
			System.out.println("Function finished.");
			System.out.println("Setting scope to " + oldScope);
			scope = oldScope;
		}

		return null;
	}

	@Override
	public Object visit(ASTParamList node, Object data) {
		node.childrenAccept(this, data);
		return null;
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

		if(node.jjtGetValue() != null) {

			List<Object> values = (List<Object>) node.jjtGetValue();
			Token identifier = (Token) values.get(0);
			List<Token> assignment = (List<Token>) values.get(1);

			HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);

			if(scopedSymbolTable != null) {
				STC scopedVariable = scopedSymbolTable.get(identifier.image);
				scopedVariable.addData("written", true);
			}


			node.childrenAccept(this, data);
		}

		return null;
	}

	@Override
	public Object visit(ASTEvaluation node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTFunctionCall node, Object data) {

		if(node.jjtGetValue() != null) {
			List<Object> values = (List<Object>) node.jjtGetValue();
			Token identifier = (Token) values.get(0);
			List<Token> arguments = (List<Token>) values.get(1);

			HashMap<String, STC> globalSymbolTable = symbolTable.get("global");
			if(globalSymbolTable == null) globalSymbolTable = new HashMap<String, STC>();

			STC possibleFunction = globalSymbolTable.get(identifier.image);
			if(possibleFunction != null) {
				if(possibleFunction.isFunction()) {
					calledFunctions.add(identifier.image);
					if(null != possibleFunction.getData("paramList")) {
						List<Token> paramList = (List<Token>) possibleFunction.getData("paramList");
						if(paramList.size() > 0) {
							if(paramList.size() / 2 != arguments.size()) {
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

			node.childrenAccept(this, data);
		}
		return null;
	}

	@Override
	public Object visit(ASTAddExpr node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTSubExpr node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTMultExpr node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTDivExpr node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTCondition node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTIdentList node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTArgList node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTNumber node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {

		if(node.jjtGetValue() != null) {

			Token identifier = (Token) node.jjtGetValue();

			HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
			HashMap<String, STC> globalSymbolTable = symbolTable.get("global");
			if(scopedSymbolTable.get(identifier.image) == null && globalSymbolTable.get(identifier.image) == null) {
				System.out.println("Error: attempt to use undeclared identifier " + identifier.image);
			}

			if(scopedSymbolTable.get(identifier.image) != null) {
				STC scopedVariable = scopedSymbolTable.get(identifier.image);
				scopedVariable.addData("readFrom", true);
				scopedSymbolTable.put(identifier.image, scopedVariable);
			}
			if(globalSymbolTable.get(identifier.image) != null) {
				STC globalVariable = globalSymbolTable.get(identifier.image);
				globalVariable.addData("readFrom", true);
				globalSymbolTable.put(identifier.image, globalVariable);
			}
		}

		node.childrenAccept(this, data);
		return null;
	}
}
