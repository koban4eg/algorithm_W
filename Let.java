import java.util.HashMap;
import java.util.Map;

public class Let implements ParseExpr {
    Var var;
    ParseExpr defExpr;
    ParseExpr inExpr;
    public Let(Var var, ParseExpr defExpr, ParseExpr inExpr){
        this.var = var;
        this.defExpr = defExpr;
        this.inExpr = inExpr;
    }

    @Override
    public String toString() {

        return String.format("let %s = %s in %s", var.toString(), defExpr.toString(), inExpr.toString());
    }

    @Override
    public int hashCode(){

        return var.hashCode() + defExpr.hashCode() + inExpr.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Let let = (Let) o;
        return let.var.equals(var) && let.defExpr.equals(defExpr) && let.inExpr.equals(inExpr);
    }

    @Override
    public void inferType(HashMap<ParseExpr, Type> map) {
        if(map.containsKey(var))
            throw new RuntimeException(String.format("Duplicate bound %s variable in let", var.toString()));
        HashMap<ParseExpr, Type> env1 = new HashMap<ParseExpr, Type>(map);
        defExpr.inferType(env1);
        HashMap<ParseExpr, Type> env2 = new HashMap<ParseExpr, Type>(map);

        if(defExpr instanceof Abs) {
            Arrow a = new Arrow((Arrow) env1.get(defExpr), true);
            a.setGeneric();
            env2.put(var, a);
        } else {
            env2.put(var, env1.get(defExpr));
        }
        inExpr.inferType(env2);
        map.put(this, env2.get(inExpr));

    }
}
