import java.util.HashMap;
import java.util.Map;


public class App implements ParseExpr {
    ParseExpr first;
    ParseExpr second;
    public App(ParseExpr first, ParseExpr second)
    {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return String.format("(%s %s)", first.toString(), second.toString());
    }

    @Override
    public int hashCode(){
        return first.hashCode() + second.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        App a = (App) o;
        return a.first.equals(first) && a.second.equals(second);
    }

    static Type substituteTyVar(String name, Type t1, Type t2)
    {
        if(t2 instanceof TyVar) {
            TyVar tv = (TyVar) t2;
            if(tv.name.equals(name))
                return t1;
            return t2;
        }

        if(t2 instanceof Arrow) {
            Arrow a = (Arrow) t2;
            return new Arrow(substituteTyVar(name, t1, a.from), substituteTyVar(name, t1, a.to));
        }
        throw null;//new RuntimeException(String.format("Can't substitute TyVar % % %", name, t1, t2));
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    static boolean tvInArr(TyVar tv, Arrow arr)
    {
        if(arr.from instanceof Arrow)
            return tvInArr(tv, (Arrow) arr.from);
        if(arr.to instanceof Arrow)
            return tvInArr(tv, (Arrow) arr.to);
        if(arr.from instanceof TyVar)
            return tv.equals((TyVar) arr.from);
        if(arr.to instanceof TyVar)
            return tv.equals((TyVar) arr.to);
        return false;
    }

    static void unifyTypes(Type t1, Type t2, HashMap<ParseExpr, Type> env)
    {

        if(t1 instanceof TyVar) {
            TyVar tv = (TyVar) t1;
            for(Map.Entry<ParseExpr, Type> entry : env.entrySet())
            {
                env.put(entry.getKey(), substituteTyVar(tv.name, t2, env.get(entry.getKey())));
            }

        } else if(t1 instanceof Arrow &&  t2 instanceof Arrow) {

            Arrow a1 = (Arrow) t1;
            ParseExpr t1_key = getKeyByValue(env, t1);
            Arrow a2 = (Arrow) t2;
            ParseExpr t2_key = getKeyByValue(env, t2);
            unifyTypes(a1.from, a2.from, env);
            if(t1_key != null)
                a1 =(Arrow) env.get(t1_key);
            if(t2_key != null)
                a2 =(Arrow) env.get(t2_key);
            unifyTypes(a1.to, a2.to, env);

        } else if(t1 instanceof Arrow && t2 instanceof TyVar) {
            Arrow arrow = (Arrow) t1;
            TyVar tv = (TyVar) t2;
            if(tvInArr(tv, arrow))
                throw new RuntimeException(String.format("Can't substitute %s with %s", t1, t2));

            for(Map.Entry<ParseExpr, Type> entry : env.entrySet())
            {
                env.put(entry.getKey(), substituteTyVar(tv.name, t1, env.get(entry.getKey())));
            }
        }
        else
            throw new RuntimeException(String.format("Can't unify type %s with %s", t1.toString(), t2.toString()));
    }

    static boolean areTypesCompatible(Type t1, Type t2)
    {
        if(t2 instanceof TyVar) {
            return true;
        }
        if(t1 instanceof Arrow && t2 instanceof Arrow)
        {
            Arrow a1 = (Arrow) t1;
            Arrow a2 = (Arrow) t2;
            return areTypesCompatible(a1.from, a2.from) && areTypesCompatible(a1.to, a2.to);
        }
        return false;
    }

    @Override
    public void inferType(HashMap<ParseExpr, Type> map) {
        if(second instanceof Abs) {
            HashMap<ParseExpr, Type> new_env = new HashMap<>(map);
            second.inferType(new_env);
            Type t = new_env.get(second);
            second = new Var("anonimous" + String.valueOf(Var.Count));
            Var.Count += 1;
            map.put(second, t);
        }else {
            second.inferType(map);
        }

        if (first instanceof Var) {

            if(!map.containsKey(first)) {
                TyVar tv = new TyVar();
                map.put(first, new Arrow(map.get(second), tv));
                map.put(this, tv);

            } else {

                unifyTypes(new Arrow(map.get(second), new TyVar()), map.get(first), map);
                Arrow arrow = (Arrow) map.get(first);
                map.put(this, arrow.to);
            }

        } else if (first instanceof App) {

            first.inferType(map);
            unifyTypes(map.get(first), new Arrow(map.get(second), new TyVar()), map);
            Arrow arrow = (Arrow) map.get(first);
            map.put(this, arrow.to);

        } else if (first instanceof Abs) {
            HashMap<ParseExpr, Type> new_env = new HashMap<>(map);
            first.inferType(new_env);

            Type t = new_env.get(first);
            first = new Var("anonimous" + String.valueOf(Var.Count));
            Var.Count += 1;
            map.put(first, t);

            Arrow arrow = (Arrow) map.get(first);
            //if(areTypesCompatible(arrow.from, map.get(second))) {
            unifyTypes(arrow.from, map.get(second), map);
            map.put(this, ((Arrow) map.get(first)).to);

            //} else {
            //    throw new RuntimeException(
            //            String.format("Can't apply %s to %s. Incompatible types",
            //                    first.toString(), second.toString()));
            //}
        }
    }
}
