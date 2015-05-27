
public class Arrow implements Type {
    Type from;
    Type to;
    boolean generic;

    public Arrow(Arrow a, boolean generic) {
        this.from = a.from;
        this.to = a.to;
        this.generic = generic;
    }

    @Override
    public Type clone()
    {
        return new Arrow(from.clone(), to.clone(), generic);
    }

    @Override
    public boolean generic() { return generic;}

    @Override
    public void changeName() {
        if(generic) {
            from.changeName();
            to.changeName();
            generic = false;
        }
    }
    @Override
    public void setGeneric() {
        generic = true;
        from.setGeneric();
        to.setGeneric();
    }
    public Arrow(Type from, Type to, boolean generic) {
        this.from = from;
        this.to = to;
        this.generic = generic;
    }


    public Arrow(Type from, Type to) {
        this.from = from;
        this.to = to;
        this.generic = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arrow a = (Arrow) o;
        return from.equals(a.from) && to.equals(a.to);
    }


    @Override
    public String toString()
    {
        return String.format("(%s)->%s", from.toString(), to.toString());
    }
}
