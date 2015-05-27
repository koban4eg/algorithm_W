import org.antlr.v4.runtime.tree.ParseTree;

public class ExprBuilder {
    public static ParseExpr build(ParseTree ctx) {

        if (ctx.getClass() == AlgoWParser.ParenthesisContext.class) {
            AlgoWParser.ParenthesisContext parenthesisContext = (AlgoWParser.ParenthesisContext)ctx;
            return build(parenthesisContext.getChild(1));
        }

        if (ctx.getClass() == AlgoWParser.VarContext.class) {
            AlgoWParser.VarContext varContext = (AlgoWParser.VarContext)ctx;
            return new Var(varContext.ID().toString());
        }

        if (ctx.getClass() == AlgoWParser.AppContext.class) {
            AlgoWParser.AppContext appContext = (AlgoWParser.AppContext)ctx;
            return new App(build(appContext.getChild(0)), build(appContext.getChild(1)));
        }

        if (ctx.getClass() == AlgoWParser.AbsContext.class) {
            AlgoWParser.AbsContext absContext = (AlgoWParser.AbsContext)ctx;
            String var = absContext.getChild(1).toString();
            return new Abs(new Var(var), build(absContext.getChild(3)));
        }

        if (ctx.getClass() == AlgoWParser.LetContext.class) {
            AlgoWParser.LetContext letContext = (AlgoWParser.LetContext)ctx;
            String var = letContext.getChild(1).toString();
            return new Let(new Var(var), build(letContext.getChild(3)), build(letContext.getChild(5)));
        }

        throw new RuntimeException("no rule");
    }
}
