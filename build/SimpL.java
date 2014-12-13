/* SimpL.java */
/* Generated By:JJTree&JavaCC: Do not edit this line. SimpL.java */
import java.util.*;

class SimpL/*@bgen(jjtree)*/implements SimpLTreeConstants, SimpLConstants {/*@bgen(jjtree)*/
  protected static JJTSimpLState jjtree = new JJTSimpLState();
    public static void main(String[] args) throws ParseException {

      SimpL parser = new SimpL(System.in);
      ASTProgram program = parser.program();

      System.out.println("Abstract Syntax Tree:");

      program.dump(" ");

          System.out.println();
          System.out.println("Semantic Checker:");
          SemanticCheckVisitor semanticCheckVisitor = new SemanticCheckVisitor();
          program.jjtAccept(semanticCheckVisitor, null);

    }

  static final public ASTProgram program() throws ParseException {/*@bgen(jjtree) Program */
  ASTProgram jjtn000 = new ASTProgram(JJTPROGRAM);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case CONST:
        case VAR:{
          ;
          break;
          }
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
        decl();
      }
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case BOOL:
        case INT:
        case REAL:
        case STRING:
        case VOID:{
          ;
          break;
          }
        default:
          jj_la1[1] = jj_gen;
          break label_2;
        }
        function();
      }
      main_prog();
      jj_consume_token(0);
jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
{if ("" != null) return jjtn000;}
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
    throw new Error("Missing return statement in function");
  }

  static final public void decl() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case VAR:{
      var_decl();
      break;
      }
    case CONST:{
      const_decl();
      break;
      }
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void var_decl() throws ParseException {/*@bgen(jjtree) VarDecl */
  ASTVarDecl jjtn000 = new ASTVarDecl(JJTVARDECL);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(VAR);
      ident_list();
      jj_consume_token(COLON);
      type();
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case COMMA:{
          ;
          break;
          }
        default:
          jj_la1[3] = jj_gen;
          break label_3;
        }
        jj_consume_token(COMMA);
        ident_list();
        jj_consume_token(COLON);
        type();
      }
      jj_consume_token(SEMICOLON);
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final public void const_decl() throws ParseException {/*@bgen(jjtree) ConstDecl */
  ASTConstDecl jjtn000 = new ASTConstDecl(JJTCONSTDECL);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(CONST);
      identifier();
      jj_consume_token(COLON);
      type();
      jj_consume_token(EQ);
      expression();
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case COMMA:{
          ;
          break;
          }
        default:
          jj_la1[4] = jj_gen;
          break label_4;
        }
        jj_consume_token(COMMA);
        identifier();
        jj_consume_token(COLON);
        type();
        jj_consume_token(EQ);
        expression();
      }
      jj_consume_token(SEMICOLON);
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final public void function() throws ParseException {/*@bgen(jjtree) FunctionDecl */
  ASTFunctionDecl jjtn000 = new ASTFunctionDecl(JJTFUNCTIONDECL);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      type();
      identifier();
      jj_consume_token(LPAREN);
      param_list();
      jj_consume_token(RPAREN);
      functionBody();
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final public void functionBody() throws ParseException {/*@bgen(jjtree) FunctionBody */
  ASTFunctionBody jjtn000 = new ASTFunctionBody(JJTFUNCTIONBODY);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(LBRACE);
      label_5:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case CONST:
        case VAR:{
          ;
          break;
          }
        default:
          jj_la1[5] = jj_gen;
          break label_5;
        }
        decl();
      }
      label_6:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case LBRACE:
        case SEMICOLON:
        case QUESTION:
        case EXCLAMATION:
        case IF:
        case WHILE:
        case IDENTIFIER:{
          ;
          break;
          }
        default:
          jj_la1[6] = jj_gen;
          break label_6;
        }
        statement();
        jj_consume_token(SEMICOLON);
      }
      jj_consume_token(RETURN);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ADD:
      case SUB:
      case FALSE:
      case TRUE:
      case NUMBER:
      case IDENTIFIER:{
        expression();
        break;
        }
      default:
        jj_la1[7] = jj_gen;

      }
      jj_consume_token(SEMICOLON);
      jj_consume_token(RBRACE);
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final public void param_list() throws ParseException {/*@bgen(jjtree) ParamList */
  ASTParamList jjtn000 = new ASTParamList(JJTPARAMLIST);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFIER:{
        identifier();
        jj_consume_token(COLON);
        type();
        label_7:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case COMMA:{
            ;
            break;
            }
          default:
            jj_la1[8] = jj_gen;
            break label_7;
          }
          jj_consume_token(COMMA);
          identifier();
          jj_consume_token(COLON);
          type();
        }
        break;
        }
      default:
        jj_la1[9] = jj_gen;
jjtree.closeNodeScope(jjtn000, true);
                                                                             jjtc000 = false;

      }
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final public void type() throws ParseException {/*@bgen(jjtree) Type */
  ASTType jjtn000 = new ASTType(JJTTYPE);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case INT:{
        jj_consume_token(INT);
jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
jjtn000.value = token;
        break;
        }
      case BOOL:{
        jj_consume_token(BOOL);
jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
jjtn000.value = token;
        break;
        }
      case REAL:{
        jj_consume_token(REAL);
jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
jjtn000.value = token;
        break;
        }
      case STRING:{
        jj_consume_token(STRING);
jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
jjtn000.value = token;
        break;
        }
      case VOID:{
        jj_consume_token(VOID);
jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
jjtn000.value = token;
        break;
        }
      default:
        jj_la1[10] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final public void main_prog() throws ParseException {/*@bgen(jjtree) Main */
  ASTMain jjtn000 = new ASTMain(JJTMAIN);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(MAIN);
      jj_consume_token(LBRACE);
      label_8:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case CONST:
        case VAR:{
          ;
          break;
          }
        default:
          jj_la1[11] = jj_gen;
          break label_8;
        }
        decl();
      }
      label_9:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case LBRACE:
        case SEMICOLON:
        case QUESTION:
        case EXCLAMATION:
        case IF:
        case WHILE:
        case IDENTIFIER:{
          ;
          break;
          }
        default:
          jj_la1[12] = jj_gen;
          break label_9;
        }
        statement();
        jj_consume_token(SEMICOLON);
      }
      jj_consume_token(RBRACE);
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final public void statement() throws ParseException {
    if (jj_2_1(2)) {
      assignment();
    } else {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFIER:{
        functionCall();
        break;
        }
      case EXCLAMATION:{
        jj_consume_token(EXCLAMATION);
        expression();
        break;
        }
      case QUESTION:{
        jj_consume_token(QUESTION);
        identifier();
        break;
        }
      case LBRACE:{
        jj_consume_token(LBRACE);
        label_10:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case LBRACE:
          case SEMICOLON:
          case QUESTION:
          case EXCLAMATION:
          case IF:
          case WHILE:
          case IDENTIFIER:{
            ;
            break;
            }
          default:
            jj_la1[13] = jj_gen;
            break label_10;
          }
          statement();
          jj_consume_token(SEMICOLON);
        }
        jj_consume_token(RBRACE);
        break;
        }
      case IF:{
        jj_consume_token(IF);
        condition();
        jj_consume_token(THEN);
        statement();
        jj_consume_token(ELSE);
        statement();
        break;
        }
      case WHILE:{
        jj_consume_token(WHILE);
        condition();
        jj_consume_token(DO);
        statement();
        break;
        }
      default:
        jj_la1[14] = jj_gen;

      }
    }
  }

  static final public void assignment() throws ParseException {/*@bgen(jjtree) Assignment */
  ASTAssignment jjtn000 = new ASTAssignment(JJTASSIGNMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      identifier();
      jj_consume_token(ASSIGN);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ADD:
      case SUB:
      case FALSE:
      case TRUE:
      case NUMBER:
      case IDENTIFIER:{
        expression();
        break;
        }
      case STRINGS:{
        string();
        break;
        }
      default:
        jj_la1[15] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  static final public void functionCall() throws ParseException {/*@bgen(jjtree) FunctionCall */
  ASTFunctionCall jjtn000 = new ASTFunctionCall(JJTFUNCTIONCALL);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      identifier();
      jj_consume_token(LPAREN);
      arg_list();
      jj_consume_token(RPAREN);
    } catch (Throwable jjte000) {
if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  static final public void expression() throws ParseException {
    if (jj_2_2(2)) {
      functionCall();
    } else {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ADD:
      case SUB:
      case FALSE:
      case TRUE:
      case NUMBER:
      case IDENTIFIER:{
        term();
        addSubExpression();
        break;
        }
      default:
        jj_la1[16] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  static final public void addSubExpression() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ADD:{
      jj_consume_token(ADD);
      expression();
ASTAddExpr jjtn001 = new ASTAddExpr(JJTADDEXPR);
                         boolean jjtc001 = true;
                         jjtree.openNodeScope(jjtn001);
      try {
jjtree.closeNodeScope(jjtn001,  2);
                         jjtc001 = false;
jjtn001.value = new Token(ADD, tokenImage[ADD]);
      } finally {
if (jjtc001) {
                           jjtree.closeNodeScope(jjtn001,  2);
                         }
      }
      break;
      }
    case SUB:{
      jj_consume_token(SUB);
      expression();
ASTSubExpr jjtn002 = new ASTSubExpr(JJTSUBEXPR);
                           boolean jjtc002 = true;
                           jjtree.openNodeScope(jjtn002);
      try {
jjtree.closeNodeScope(jjtn002,  2);
                           jjtc002 = false;
jjtn002.value = new Token(SUB, tokenImage[SUB]);
      } finally {
if (jjtc002) {
                             jjtree.closeNodeScope(jjtn002,  2);
                           }
      }
      break;
      }
    default:
      jj_la1[17] = jj_gen;

    }
  }

  static final public void term() throws ParseException {
    fragment();
    multDivModExpression();
  }

  static final public void multDivModExpression() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case MULT:{
      jj_consume_token(MULT);
ASTMultExpr jjtn001 = new ASTMultExpr(JJTMULTEXPR);
           boolean jjtc001 = true;
           jjtree.openNodeScope(jjtn001);
      try {
        expression();
      } catch (Throwable jjte001) {
if (jjtc001) {
             jjtree.clearNodeScope(jjtn001);
             jjtc001 = false;
           } else {
             jjtree.popNode();
           }
           if (jjte001 instanceof RuntimeException) {
             {if (true) throw (RuntimeException)jjte001;}
           }
           if (jjte001 instanceof ParseException) {
             {if (true) throw (ParseException)jjte001;}
           }
           {if (true) throw (Error)jjte001;}
      } finally {
if (jjtc001) {
             jjtree.closeNodeScope(jjtn001,  2);
           }
      }
      break;
      }
    case DIV:{
      jj_consume_token(DIV);
ASTDivExpr jjtn002 = new ASTDivExpr(JJTDIVEXPR);
            boolean jjtc002 = true;
            jjtree.openNodeScope(jjtn002);
      try {
        expression();
      } catch (Throwable jjte002) {
if (jjtc002) {
              jjtree.clearNodeScope(jjtn002);
              jjtc002 = false;
            } else {
              jjtree.popNode();
            }
            if (jjte002 instanceof RuntimeException) {
              {if (true) throw (RuntimeException)jjte002;}
            }
            if (jjte002 instanceof ParseException) {
              {if (true) throw (ParseException)jjte002;}
            }
            {if (true) throw (Error)jjte002;}
      } finally {
if (jjtc002) {
              jjtree.closeNodeScope(jjtn002,  2);
            }
      }
      break;
      }
    case MOD:{
      jj_consume_token(MOD);
ASTModExpr jjtn003 = new ASTModExpr(JJTMODEXPR);
            boolean jjtc003 = true;
            jjtree.openNodeScope(jjtn003);
      try {
        expression();
      } catch (Throwable jjte003) {
if (jjtc003) {
              jjtree.clearNodeScope(jjtn003);
              jjtc003 = false;
            } else {
              jjtree.popNode();
            }
            if (jjte003 instanceof RuntimeException) {
              {if (true) throw (RuntimeException)jjte003;}
            }
            if (jjte003 instanceof ParseException) {
              {if (true) throw (ParseException)jjte003;}
            }
            {if (true) throw (Error)jjte003;}
      } finally {
if (jjtc003) {
              jjtree.closeNodeScope(jjtn003,  2);
            }
      }
      break;
      }
    default:
      jj_la1[18] = jj_gen;

    }
  }

  static final public void fragment() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case IDENTIFIER:{
      identifier();
      break;
      }
    case NUMBER:{
      number();
      break;
      }
    case FALSE:
    case TRUE:{
      bool();
      break;
      }
    case ADD:{
      jj_consume_token(ADD);
ASTAddFrag jjtn001 = new ASTAddFrag(JJTADDFRAG);
            boolean jjtc001 = true;
            jjtree.openNodeScope(jjtn001);
      try {
        fragment();
      } catch (Throwable jjte001) {
if (jjtc001) {
              jjtree.clearNodeScope(jjtn001);
              jjtc001 = false;
            } else {
              jjtree.popNode();
            }
            if (jjte001 instanceof RuntimeException) {
              {if (true) throw (RuntimeException)jjte001;}
            }
            if (jjte001 instanceof ParseException) {
              {if (true) throw (ParseException)jjte001;}
            }
            {if (true) throw (Error)jjte001;}
      } finally {
if (jjtc001) {
              jjtree.closeNodeScope(jjtn001, true);
            }
      }
      break;
      }
    case SUB:{
      jj_consume_token(SUB);
ASTSubFrag jjtn002 = new ASTSubFrag(JJTSUBFRAG);
            boolean jjtc002 = true;
            jjtree.openNodeScope(jjtn002);
      try {
        fragment();
      } catch (Throwable jjte002) {
if (jjtc002) {
              jjtree.clearNodeScope(jjtn002);
              jjtc002 = false;
            } else {
              jjtree.popNode();
            }
            if (jjte002 instanceof RuntimeException) {
              {if (true) throw (RuntimeException)jjte002;}
            }
            if (jjte002 instanceof ParseException) {
              {if (true) throw (ParseException)jjte002;}
            }
            {if (true) throw (Error)jjte002;}
      } finally {
if (jjtc002) {
              jjtree.closeNodeScope(jjtn002, true);
            }
      }
      break;
      }
    default:
      jj_la1[19] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void condition() throws ParseException {/*@bgen(jjtree) Condition */
  ASTCondition jjtn000 = new ASTCondition(JJTCONDITION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case NOT:{
        jj_consume_token(NOT);
        expression();
        break;
        }
      case ADD:
      case SUB:
      case FALSE:
      case TRUE:
      case NUMBER:
      case IDENTIFIER:{
        expression();
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case EQ:{
          jj_consume_token(EQ);
          break;
          }
        case NOTEQ:{
          jj_consume_token(NOTEQ);
          break;
          }
        case LT:{
          jj_consume_token(LT);
          break;
          }
        case GT:{
          jj_consume_token(GT);
          break;
          }
        case GE:{
          jj_consume_token(GE);
          break;
          }
        case LE:{
          jj_consume_token(LE);
          break;
          }
        case AND:{
          jj_consume_token(AND);
          break;
          }
        case OR:{
          jj_consume_token(OR);
          break;
          }
        default:
          jj_la1[20] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        expression();
        break;
        }
      default:
        jj_la1[21] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final public void ident_list() throws ParseException {/*@bgen(jjtree) IdentList */
  ASTIdentList jjtn000 = new ASTIdentList(JJTIDENTLIST);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      identifier();
      list_();
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  static final public void arg_list() throws ParseException {/*@bgen(jjtree) ArgList */
  ASTArgList jjtn000 = new ASTArgList(JJTARGLIST);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFIER:{
        identifier();
        list_();
        break;
        }
      default:
        jj_la1[22] = jj_gen;
jjtree.closeNodeScope(jjtn000, true);
                             jjtc000 = false;

      }
    } catch (Throwable jjte000) {
if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  static final public void list_() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case COMMA:{
      jj_consume_token(COMMA);
      identifier();
      list_();
      break;
      }
    default:
      jj_la1[23] = jj_gen;

    }
  }

  static final public void number() throws ParseException {/*@bgen(jjtree) Number */
  ASTNumber jjtn000 = new ASTNumber(JJTNUMBER);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(NUMBER);
jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
jjtn000.value = token;
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  static final public void string() throws ParseException {/*@bgen(jjtree) String */
  ASTString jjtn000 = new ASTString(JJTSTRING);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(STRINGS);
jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
jjtn000.value = token;
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  static final public void bool() throws ParseException {/*@bgen(jjtree) Boolean */
  ASTBoolean jjtn000 = new ASTBoolean(JJTBOOLEAN);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case TRUE:{
        jj_consume_token(TRUE);
jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
jjtn000.value = token;
        break;
        }
      case FALSE:{
        jj_consume_token(FALSE);
jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
jjtn000.value = token;
        break;
        }
      default:
        jj_la1[24] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  static final public void identifier() throws ParseException {/*@bgen(jjtree) Identifier */
  ASTIdentifier jjtn000 = new ASTIdentifier(JJTIDENTIFIER);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(IDENTIFIER);
jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
jjtn000.value = token;
    } finally {
if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  static private boolean jj_2_1(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  static private boolean jj_2_2(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  static private boolean jj_3R_11()
 {
    if (jj_3R_13()) return true;
    if (jj_scan_token(ASSIGN)) return true;
    return false;
  }

  static private boolean jj_3_2()
 {
    if (jj_3R_12()) return true;
    return false;
  }

  static private boolean jj_3_1()
 {
    if (jj_3R_11()) return true;
    return false;
  }

  static private boolean jj_3R_12()
 {
    if (jj_3R_13()) return true;
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  static private boolean jj_3R_13()
 {
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public SimpLTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private Token jj_scanpos, jj_lastpos;
  static private int jj_la;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[25];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x0,0x0,0x0,0x10000,0x10000,0x0,0x624000,0x1800000,0x10000,0x0,0x0,0x0,0x624000,0x624000,0x604000,0x1800000,0x1800000,0x1800000,0xe000000,0x1800000,0xf0000000,0x1800000,0x0,0x10000,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x40010,0x8a208,0x40010,0x0,0x0,0x40010,0x500100,0x620080,0x0,0x400000,0x8a208,0x40010,0x500100,0x500100,0x500100,0xe20080,0x620080,0x0,0x0,0x620080,0x1007,0x620880,0x400000,0x0,0x20080,};
   }
  static final private JJCalls[] jj_2_rtns = new JJCalls[2];
  static private boolean jj_rescan = false;
  static private int jj_gc = 0;

  /** Constructor with InputStream. */
  public SimpL(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public SimpL(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new SimpLTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 25; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 25; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public SimpL(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new SimpLTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 25; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 25; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public SimpL(SimpLTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 25; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(SimpLTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 25; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  @SuppressWarnings("serial")
  static private final class LookaheadSuccess extends java.lang.Error { }
  static final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  static private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;
  static private int[] jj_lasttokens = new int[100];
  static private int jj_endpos;

  static private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[62];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 25; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 62; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

  static private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 2; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  static private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}