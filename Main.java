import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception{
        AlgoWLexer lexer = new AlgoWLexer( new ANTLRFileStream(args[0]));
        CommonTokenStream tokens = new CommonTokenStream( lexer );
        AlgoWParser parser = new AlgoWParser( tokens );
        ParseTree tree = parser.expr();

        ParseExpr expr = ExprBuilder.build(tree);
        HashMap<ParseExpr, Type> env = new HashMap<ParseExpr, Type>();
        try {
            expr.inferType(env);
        } catch (Exception e) {
            System.out.println("Expression has no type");
        }
        System.out.println(env.get(expr));
    }
}
