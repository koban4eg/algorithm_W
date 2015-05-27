import java.util.HashMap;

public class Var implements ParseExpr {
    String name;
    static char count = '0';

    public Var(String name) {
        this.name = name;
    }

    public void changeName()
    {
        name = name + count;
        count += 1;
    }



    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Var v = (Var) o;
        return name.equals(v.name);
    }

    @Override
    public void inferType(HashMap<ParseExpr, Type> map)
    {
        if(!map.containsKey(this)) {
            map.put(this, new TyVar());
            return;
        }

        Type t = map.get(this);
        if(map.get(this) instanceof Arrow && t.generic())
        {
            changeName();
            Type n = t.clone();
            n.changeName();
            TyVar.Current += 1;
            map.put(new Var(name), n);
        }
    }
}
