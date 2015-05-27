import java.util.HashMap;

public class Abs implements ParseExpr {
    Var var;
    ParseExpr body;
    public Abs(Var var, ParseExpr body){
        this.var = var;
        this.body = body;
    }

    @Override
    public int hashCode()
    {
        return var.hashCode() + body.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Abs a = (Abs) o;
        return a.var.equals(var) && a.body.equals(body);
    }

    @Override
    public String toString(){
        return String.format("\\%s.%s", var.toString(), body.toString());
    }

    @Override
    public void inferType(HashMap<ParseExpr, Type> map) {
        if(map.containsKey(var))
            throw new RuntimeException(String.format("Duplicate bound %s variable in lambda", var.toString()));
        body.inferType(map);
        var.inferType(map);
        map.put(this, new Arrow(map.get(var), map.get(body)));
    }
}
