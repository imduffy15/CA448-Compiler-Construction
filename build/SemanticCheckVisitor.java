import java.util.*;

@SuppressWarnings("unchecked")
public class SemanticCheckVisitor implements SimpLVisitor {


    private final static String GLOBAL_SCOPE = "GLOBAL_SCOPE";
    private static final LinkedHashSet<String> calledFunctions = new LinkedHashSet<>();
    private static final LinkedHashSet<String> declaredFunctions = new LinkedHashSet<>();
    private static final HashMap<String, HashMap<String, STC>> symbolTable = new HashMap<>();
    private static String oldScope = GLOBAL_SCOPE;
    private static String scope = GLOBAL_SCOPE;
    private static int scopeCounter = 0;

    @Override
    public Object visit(SimpleNode node, Object data) {
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
                if (symbolTableChild.getKind() != DataType.FUNCTION) {
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

        System.out.printf("%-15s%-15s%-15s%-15s%-15s%n", "Kind", "Type", "Identifier", "Scope", "Data");
        for (String scope : symbolTable.keySet()) {
            HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
            for (String key : scopedSymbolTable.keySet()) {
                STC symbolTableChild = scopedSymbolTable.get(key);
                System.out.printf("%-15s%-15s%-15s%-15s%-15s%n", symbolTableChild.getKind(), symbolTableChild.getType(), symbolTableChild.getIdentifier(), symbolTableChild.getScope(), symbolTableChild.getData());
            }
        }

        return null;
    }

    @Override
    public Object visit(ASTVarDecl node, Object data) {

        List<Token> identList = (List<Token>) node.jjtGetChild(0).jjtAccept(this, data);
        Token type = (Token) node.jjtGetChild(1).jjtAccept(this, data);

        for (Token identifier : identList) {
            HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
            if (scopedSymbolTable == null) scopedSymbolTable = new HashMap<>();

            STC variable = new STC(identifier, type, scope, DataType.VAR);

            if (scopedSymbolTable.get(identifier.image) != null) {
                System.out.println("Error: Identifier " + identifier.image + " already declared in " + scope);
            } else {
                scopedSymbolTable.put(identifier.image, variable);
                symbolTable.put(scope, scopedSymbolTable);
            }
        }

        return null;
    }

    @Override
    public Object visit(ASTConstDecl node, Object data) {
        HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
        if(scopedSymbolTable == null) scopedSymbolTable = new HashMap<String, STC>();

        for (int i = 0; i < node.jjtGetNumChildren(); i=i+3) {
        	Token identifier = (Token) node.jjtGetChild(i).jjtAccept(this, data);
        	Token type = (Token) node.jjtGetChild(i+1).jjtAccept(this, data);
        	List<Token> assignment;
            if (node.jjtGetChild(i+2).jjtAccept(this, data) instanceof Token) {
                assignment = new ArrayList<>();
                assignment.add((Token) node.jjtGetChild(i).jjtAccept(this, data));
            } else {
                assignment = ((List<Token>) node.jjtGetChild(i).jjtAccept(this, data));
            }
            STC variable = new STC(identifier, type, scope, DataType.CONST);
            variable.addData("value", assignment);

	        if (scopedSymbolTable.get(identifier.image) != null) {
	            System.out.println("Error: Identifier " + identifier.image + " already declared in " + scope);
	        } else {
	            scopedSymbolTable.put(identifier.image, variable);
	            symbolTable.put(scope, scopedSymbolTable);
	        }

        }

        return null;
    }

    @Override
    public Object visit(ASTFunctionDecl node, Object data) {

        Token type = (Token) node.jjtGetChild(0).jjtAccept(this, data);
        Token identifier = (Token) ((SimpleNode) node.jjtGetChild(1)).jjtGetValue();
        List<Token> paramList = (List<Token>) node.jjtGetChild(2).jjtAccept(this, data);
        Node functionBodyNode = node.jjtGetChild(3);

        STC function = new STC(identifier, type, scope, DataType.FUNCTION);
        function.addData("paramList", paramList);

        HashMap<String, STC> globalSymbolTable = symbolTable.get(scope);

        if (globalSymbolTable.get(identifier.image) != null) {
            System.out.println("Error: Identifier " + identifier.image + " already declared in " + scope);
        } else {
            globalSymbolTable.put(identifier.image, function);
            symbolTable.put(scope, globalSymbolTable);
            declaredFunctions.add(identifier.image);
        }

        oldScope = scope;
        scope = "function-" + ++scopeCounter;

        for (int i = 0; i < paramList.size() - 1; ) {
            HashMap<String, STC> functionSymbolTable = symbolTable.get(scope);
            Token paramIdentifier = paramList.get(i);
            Token paramType = paramList.get(++i);
            STC scopedVariable = new STC(paramIdentifier, paramType, scope, DataType.VAR);
            if (functionSymbolTable == null) functionSymbolTable = new HashMap<>();

            if (functionSymbolTable.get(paramIdentifier.image) != null) {
                System.out.println("Error: Identifier " + paramType.image + " already declared in " + scope);
            } else {
                functionSymbolTable.put(paramIdentifier.image, scopedVariable);
                symbolTable.put(scope, functionSymbolTable);
            }
        }

        functionBodyNode.jjtAccept(this, data);

        return null;
    }

    @Override
    public Object visit(ASTFunctionBody node, Object data) {
        node.childrenAccept(this, data);
        scope = oldScope;
        return null;
    }

    @Override
    public Object visit(ASTParamList node, Object data) {
        List<Token> paramList = new ArrayList();

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            paramList.add((Token) ((SimpleNode) node.jjtGetChild(i)).jjtGetValue());
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
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTAssignment node, Object data) {

        Token identifier = (Token) node.jjtGetChild(0).jjtAccept(this, data);

        List<Token> assignment = null;

        Object obj = node.jjtGetChild(1).jjtAccept(this, data);
        if (obj instanceof Token) {
        	assignment = new ArrayList<>();
            assignment.add((Token) obj);
        } else {
            assignment = (List<Token>) obj;
        }

        HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);

        STC scopedVariable = scopedSymbolTable.get(identifier.image);
        if (scopedVariable != null) {
            scopedVariable.addData("written", true);
            scopedVariable.addData("value", assignment);

            if(scopedVariable.getData("value") != null) {
                checkType(scopedVariable, scopedVariable.getData("value"));
            }
        }

        HashMap<String, STC> globalSymbolTable = symbolTable.get(GLOBAL_SCOPE);

        STC globalVariable = globalSymbolTable.get(identifier.image);
        if (globalVariable != null) {
            globalVariable.addData("written", true);
            globalVariable.addData("value", assignment);

            if(globalVariable.getData("value") != null) {
                checkType(globalVariable, globalVariable.getData("value"));
            }
        }


        return null;
    }

    private void checkType(STC scopedVariable, Object values) {
        HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
        HashMap<String, STC> globalSymbolTable = symbolTable.get(GLOBAL_SCOPE);
        List<Object> valuesList = (List) values;

        for(Object obj : valuesList) {
            if(obj instanceof Token) {
                Token token = (Token) obj;

                if(token.kind == SimpLConstants.IDENTIFIER) {
                    STC resolved = null;
                    if(scopedSymbolTable.get(token.image) != null) {
                        resolved = scopedSymbolTable.get(token.image);
                    } else if(globalSymbolTable.get(token.image) != null) {
                        resolved = globalSymbolTable.get(token.image);
                    }
                    if(resolved != null) {
                        if(resolved.getType().kind != scopedVariable.getType().kind) {
                            System.out.println("Error: Right hand side of " + scopedVariable.getIdentifier() + " does not match type " + scopedVariable.getType());
                            return;
                        }
                    }
                } else {
                    if(token.kind != scopedVariable.getType().kind) {
                        System.out.println("Error: Right hand side of " + scopedVariable.getIdentifier() + " does not match type " + scopedVariable.getType());
                        return;
                    }
                }
            } else {
                checkType(scopedVariable, obj);
            }
        }
    }

    @Override
    public Object visit(ASTFunctionCall node, Object data) {

        Token identifier = (Token) node.jjtGetChild(0).jjtAccept(this, data);
        List<Token> arguments = (List<Token>) node.jjtGetChild(1).jjtAccept(this, data);

        HashMap<String, STC> globalSymbolTable = symbolTable.get(GLOBAL_SCOPE);

        STC possibleFunction = globalSymbolTable.get(identifier.image);
        if (possibleFunction != null) {
            if (possibleFunction.getKind() == DataType.FUNCTION) {
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

        return identifier;
    }

    private void checkArgumentsBooleanOperators(Object obj) {
    	if(!(obj instanceof Token)) return;
    	Token token = (Token) obj;
		HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
    	HashMap<String, STC> globalSymbolTable = symbolTable.get(GLOBAL_SCOPE);
		STC symbolTableReference = null;

		if(scopedSymbolTable.get(token.image) != null) {
			symbolTableReference = scopedSymbolTable.get(token.image);
		} else if(globalSymbolTable.get(token.image) != null) {
			symbolTableReference = globalSymbolTable.get(token.image);
		}
		if(symbolTableReference != null) {
    		if(symbolTableReference.getKind() == DataType.VAR) {
    			if(symbolTableReference.getType().kind == SimpLConstants.BOOL) {
    				System.out.println(symbolTableReference.getIdentifier() + " is a variable and boolean");
    			}
    		} else if(symbolTableReference.getKind() == DataType.CONST) {
    			if(symbolTableReference.getType().kind == SimpLConstants.BOOL) {
    				System.out.println(symbolTableReference.getIdentifier() + " is a const and boolean");
    			}			
    		}
		}

    }

    private void checkArgumentsMathOperators(Object obj) {
    	if(!(obj instanceof Token)) return;
    	Token token = (Token) obj;

		HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
    	HashMap<String, STC> globalSymbolTable = symbolTable.get(GLOBAL_SCOPE);
		STC symbolTableReference = null;

		if(scopedSymbolTable.get(token.image) != null) {
			symbolTableReference = scopedSymbolTable.get(token.image);
		} else if(globalSymbolTable.get(token.image) != null) {
			symbolTableReference = globalSymbolTable.get(token.image);
		}
		if(symbolTableReference != null) {
    		if(symbolTableReference.getKind() == DataType.VAR) {
    			if(symbolTableReference.getType().kind == SimpLConstants.INT) {
    				System.out.println(symbolTableReference.getIdentifier() + " is a variable and int");
    			} else if(symbolTableReference.getType().kind == SimpLConstants.REAL) {
    				System.out.println(symbolTableReference.getIdentifier() + " is a variable and real");
    			}
    		} else if(symbolTableReference.getKind() == DataType.CONST) {
    			if(symbolTableReference.getType().kind == SimpLConstants.INT) {
    				System.out.println(symbolTableReference.getIdentifier() + " is a const and int");
    			} else if(symbolTableReference.getType().kind == SimpLConstants.REAL) {
    				System.out.println(symbolTableReference.getIdentifier() + " is a const and real");
    			}    			
    		}
		}

    }

    @Override
    public Object visit(ASTAddExpr node, Object data) {
    	checkArgumentsMathOperators(node.jjtGetChild(0).jjtAccept(this, data));
    	checkArgumentsMathOperators(node.jjtGetChild(1).jjtAccept(this, data));
        return Arrays.asList(node.jjtGetChild(0).jjtAccept(this, data), node.jjtGetChild(1).jjtAccept(this, data));
    }

    @Override
    public Object visit(ASTSubExpr node, Object data) {
    	checkArgumentsMathOperators((Token) node.jjtGetChild(0).jjtAccept(this, data));
    	checkArgumentsMathOperators((Token) node.jjtGetChild(1).jjtAccept(this, data));
        return Arrays.asList(node.jjtGetChild(0).jjtAccept(this, data), node.jjtGetChild(1).jjtAccept(this, data));
    }

    @Override
    public Object visit(ASTMultExpr node, Object data) {
    	checkArgumentsMathOperators((Token) node.jjtGetChild(0).jjtAccept(this, data));
    	checkArgumentsMathOperators((Token) node.jjtGetChild(1).jjtAccept(this, data));
        return Arrays.asList(node.jjtGetChild(0).jjtAccept(this, data), node.jjtGetChild(1).jjtAccept(this, data));
    }

    @Override
    public Object visit(ASTDivExpr node, Object data) {
    	checkArgumentsMathOperators((Token) node.jjtGetChild(0).jjtAccept(this, data));
    	checkArgumentsMathOperators((Token) node.jjtGetChild(1).jjtAccept(this, data));
        return Arrays.asList(node.jjtGetChild(0).jjtAccept(this, data), node.jjtGetChild(1).jjtAccept(this, data));
    }

    @Override
    public Object visit(ASTModExpr node, Object data) {
    	checkArgumentsMathOperators((Token) node.jjtGetChild(0).jjtAccept(this, data));
    	checkArgumentsMathOperators((Token) node.jjtGetChild(1).jjtAccept(this, data));
        return Arrays.asList(node.jjtGetChild(0).jjtAccept(this, data), node.jjtGetChild(1).jjtAccept(this, data));
    }

    @Override
    public Object visit(ASTCondition node, Object data) {
    	checkArgumentsBooleanOperators(node.jjtGetChild(0).jjtAccept(this, data));
    	checkArgumentsBooleanOperators(node.jjtGetChild(1).jjtAccept(this, data));
        return Arrays.asList(node.jjtGetChild(0).jjtAccept(this, data), node.jjtGetChild(1).jjtAccept(this, data));
    }

    @Override
    public Object visit(ASTAddFrag node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTSubFrag node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTIdentList node, Object data) {
        List<Token> identList = new ArrayList();

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            identList.add((Token) ((SimpleNode) node.jjtGetChild(i)).jjtGetValue());
        }

        return identList;
    }

    @Override
    public Object visit(ASTArgList node, Object data) {
        List<Token> argList = new ArrayList();

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            argList.add((Token) node.jjtGetChild(i).jjtAccept(this, data));
        }

        return argList;
    }

    @Override
    public Object visit(ASTNumber node, Object data) {

        Token value = (Token) node.jjtGetValue();

        if (value.image.contains(".")) {
            value.kind = SimpLConstants.REAL;
        } else {
            value.kind = SimpLConstants.INT;
        }

        return value;
    }

    @Override
    public Object visit(ASTBoolean node, Object data) {
        Token value = (Token) node.jjtGetValue();
        value.kind = SimpLConstants.BOOL;
        return value;
    }

    @Override
    public Object visit(ASTString node, Object data) {
        Token value = (Token) node.jjtGetValue();
        value.kind = SimpLConstants.STRING;
        return value;
    }

    @Override
    public Object visit(ASTIdentifier node, Object data) {

        Token identifier = (Token) node.jjtGetValue();

        HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
        HashMap<String, STC> globalSymbolTable = symbolTable.get(GLOBAL_SCOPE);

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
