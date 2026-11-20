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

        LinkedHashSet<String> uncalledFunctions = new LinkedHashSet<>(declaredFunctions);
        uncalledFunctions.removeAll(calledFunctions);
        if (uncalledFunctions.isEmpty()) {
            System.out.println("All functions were called");
        } else {
            for (String function : uncalledFunctions) {
                System.out.println("Error: function " + function + " is declared but never called");
            }
        }

        for (String scope : symbolTable.keySet()) {
            HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
            for (String key : scopedSymbolTable.keySet()) {
                STC symbolTableChild = scopedSymbolTable.get(key);
                if (symbolTableChild.getKind() != DataType.FUNCTION) {
                    if (symbolTableChild.getData("written") == null && symbolTableChild.getData("readFrom") == null) {
                        System.out.println(symbolTableChild.getIdentifier() + " in scope " + symbolTableChild.getScope() + " is not written to or read from");
                    } else if (symbolTableChild.getData("written") == null && Boolean.TRUE.equals(symbolTableChild.getData("readFrom"))) {
                        System.out.println(symbolTableChild.getIdentifier() + " in scope " + symbolTableChild.getScope() + " is not written to but is read from");
                    } else if (Boolean.TRUE.equals(symbolTableChild.getData("written")) && symbolTableChild.getData("readFrom") == null) {
                        System.out.println(symbolTableChild.getIdentifier() + " in scope " + symbolTableChild.getScope() + " is written but is not read from");
                    } else if (Boolean.TRUE.equals(symbolTableChild.getData("written")) && Boolean.TRUE.equals(symbolTableChild.getData("readFrom"))) {
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
            // Read the identifier token directly: visiting the Identifier node would
            // look it up (and report it as undeclared) before it has been declared.
            Token identifier = (Token) ((SimpleNode) node.jjtGetChild(i)).jjtGetValue();
            Token type = (Token) node.jjtGetChild(i+1).jjtAccept(this, data);

            // The value is the third node of each (identifier, type, expression) triple.
            Object value = node.jjtGetChild(i+2).jjtAccept(this, data);
            List<Object> assignment;
            if (value instanceof Token) {
                assignment = new ArrayList<>();
                assignment.add(value);
            } else {
                assignment = (List<Object>) value;
            }
            STC variable = new STC(identifier, type, scope, DataType.CONST);
            variable.addData("value", assignment);
            // A constant is initialised by its declaration.
            variable.addData("written", true);
            checkType(variable, assignment);

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
        symbolTable.put(scope, new HashMap<String, STC>());

        // paramList holds (identifier, type) pairs: step over both entries each time.
        for (int i = 0; i + 1 < paramList.size(); i += 2) {
            HashMap<String, STC> functionSymbolTable = symbolTable.get(scope);
            Token paramIdentifier = paramList.get(i);
            Token paramType = paramList.get(i + 1);
            STC scopedVariable = new STC(paramIdentifier, paramType, scope, DataType.VAR);
            // A parameter is written to by the caller.
            scopedVariable.addData("written", true);
            if (functionSymbolTable == null) functionSymbolTable = new HashMap<>();

            if (functionSymbolTable.get(paramIdentifier.image) != null) {
                System.out.println("Error: Identifier " + paramIdentifier.image + " already declared in " + scope);
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
        symbolTable.put(scope, new HashMap<String, STC>());
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTAssignment node, Object data) {

        // Read the target's token directly. Visiting the Identifier node would mark
        // the variable as "read from" when it is only being written to.
        Token identifier = (Token) ((SimpleNode) node.jjtGetChild(0)).jjtGetValue();
        HashMap<String, STC> currentSymbolTable = symbolTable.get(scope);
        if ((currentSymbolTable == null || currentSymbolTable.get(identifier.image) == null)
                && symbolTable.get(GLOBAL_SCOPE).get(identifier.image) == null) {
            System.out.println("Error: attempt to use undeclared identifier " + identifier.image);
        }

        List<Token> assignment = null;

        Object obj = node.jjtGetChild(1).jjtAccept(this, data);
        if (obj instanceof Token) {
        	assignment = new ArrayList<>();
            assignment.add((Token) obj);
        } else {
            assignment = (List<Token>) obj;
        }

        // Resolve the target the same way reads are resolved: the current scope
        // first, then the global scope. A local declaration shadows a global one.
        STC target = null;
        HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
        if (scopedSymbolTable != null) {
            target = scopedSymbolTable.get(identifier.image);
        }
        if (target == null) {
            target = symbolTable.get(GLOBAL_SCOPE).get(identifier.image);
        }

        // An undeclared target has already been reported when its Identifier node was visited.
        if (target != null) {
            if (target.getKind() != DataType.VAR) {
                System.out.println("Error: cannot assign to " + identifier.image + ", it is a "
                        + target.getKind().toString().toLowerCase() + " not a variable");
            } else {
                target.addData("written", true);
                target.addData("value", assignment);
                checkType(target, assignment);
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
    public Object visit(ASTInput node, Object data) {
        // "?x" reads a value from input into x, so x is written to (not read from).
        Token identifier = (Token) ((SimpleNode) node.jjtGetChild(0)).jjtGetValue();

        STC target = null;
        HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
        if (scopedSymbolTable != null) {
            target = scopedSymbolTable.get(identifier.image);
        }
        if (target == null) {
            target = symbolTable.get(GLOBAL_SCOPE).get(identifier.image);
        }

        if (target == null) {
            System.out.println("Error: attempt to use undeclared identifier " + identifier.image);
        } else {
            target.addData("written", true);
        }

        return null;
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
                    // paramList holds (identifier, type) pairs, so it has two entries per parameter.
                    int expected = paramList.size() / 2;
                    if (expected != arguments.size()) {
                        System.out.println("Error: incorrect number of function arguments for " + identifier.image
                                + ": expected " + expected + ", got " + arguments.size());
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

    	// Literal operands: Number and Boolean nodes set the token kind to the literal's type.
    	if(token.kind != SimpLConstants.IDENTIFIER) {
    		if(token.kind != SimpLConstants.INT && token.kind != SimpLConstants.REAL) {
    			System.out.println("Error: arithmetic operand " + token.image + " must be int or real but is "
    					+ SimpLConstants.tokenImage[token.kind].replace("\"", ""));
    		}
    		return;
    	}

		HashMap<String, STC> scopedSymbolTable = symbolTable.get(scope);
    	HashMap<String, STC> globalSymbolTable = symbolTable.get(GLOBAL_SCOPE);
		STC symbolTableReference = null;

		if(scopedSymbolTable != null && scopedSymbolTable.get(token.image) != null) {
			symbolTableReference = scopedSymbolTable.get(token.image);
		} else if(globalSymbolTable.get(token.image) != null) {
			symbolTableReference = globalSymbolTable.get(token.image);
		}
		// Undeclared identifiers are reported by visit(ASTIdentifier).
		if(symbolTableReference == null) return;

		int type = symbolTableReference.getType().kind;
		String kind = symbolTableReference.getKind() == DataType.VAR ? "variable" : symbolTableReference.getKind().toString().toLowerCase();
		if(type == SimpLConstants.INT) {
			System.out.println(symbolTableReference.getIdentifier() + " is a " + kind + " and int");
		} else if(type == SimpLConstants.REAL) {
			System.out.println(symbolTableReference.getIdentifier() + " is a " + kind + " and real");
		} else {
			System.out.println("Error: arithmetic operand " + token.image + " must be int or real but "
					+ kind + " " + token.image + " is " + symbolTableReference.getType().image);
		}
    }

    private Object visitArithmeticExpr(SimpleNode node, Object data) {
        // Visit each operand exactly once. An operand may itself be a nested
        // expression, in which case it returns a List rather than a Token.
        Object left = node.jjtGetChild(0).jjtAccept(this, data);
        Object right = node.jjtGetChild(1).jjtAccept(this, data);
        checkArgumentsMathOperators(left);
        checkArgumentsMathOperators(right);
        return Arrays.asList(left, right);
    }

    private Object visitArithmeticExpr(SimpleNode node, Object data) {
        // Visit each operand exactly once. An operand may itself be a nested
        // expression, in which case it returns a List rather than a Token.
        Object left = node.jjtGetChild(0).jjtAccept(this, data);
        Object right = node.jjtGetChild(1).jjtAccept(this, data);
        checkArgumentsMathOperators(left);
        checkArgumentsMathOperators(right);
        return Arrays.asList(left, right);
    }

    @Override
    public Object visit(ASTAddExpr node, Object data) {
        return visitArithmeticExpr(node, data);
    }

    @Override
    public Object visit(ASTSubExpr node, Object data) {
        return visitArithmeticExpr(node, data);
    }

    @Override
    public Object visit(ASTMultExpr node, Object data) {
        return visitArithmeticExpr(node, data);
    }

    @Override
    public Object visit(ASTDivExpr node, Object data) {
        return visitArithmeticExpr(node, data);
    }

    @Override
    public Object visit(ASTModExpr node, Object data) {
        return visitArithmeticExpr(node, data);
    }

    @Override
    public Object visit(ASTCondition node, Object data) {
    	checkArgumentsBooleanOperators(node.jjtGetChild(0).jjtAccept(this, data));
    	checkArgumentsBooleanOperators(node.jjtGetChild(1).jjtAccept(this, data));
        return Arrays.asList(node.jjtGetChild(0).jjtAccept(this, data), node.jjtGetChild(1).jjtAccept(this, data));
    }

    @Override
    public Object visit(ASTAddFrag node, Object data) {
        // A unary sign doesn't change the operand's type, so pass the operand's value through.
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTSubFrag node, Object data) {
        // A unary sign doesn't change the operand's type, so pass the operand's value through.
        return node.jjtGetChild(0).jjtAccept(this, data);
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
