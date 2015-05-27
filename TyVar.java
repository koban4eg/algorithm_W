/**
 * Created by nik on 26.05.15.
 */
public class TyVar implements Type {
    static char Current = 'a';


    @Override
    public Type clone()
    {
        return new TyVar(new String(name), generic);
    }

    TyVar(String name, boolean generic)
    {
        this.name = name;
        this.generic = generic;
    }

    String name;
    boolean generic;
    public TyVar()
    {
        name = "" + Current;
        generic = true;
        Current +=1;
    }
    public void setGeneric() {}

    @Override
    public boolean generic() { return generic;}
    @Override
    public void changeName() {
        if(generic) {
            name = name + Current;
            generic = false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TyVar tv = (TyVar) o;
        return name.equals(tv.name);
    }


    @Override
    public String toString()
    {
        //return String.valueOf(name.hashCode());
        return name;
    }
}
