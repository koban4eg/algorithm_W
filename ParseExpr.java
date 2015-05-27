import java.util.HashMap;

public interface ParseExpr {
    public void inferType(HashMap<ParseExpr, Type> map);
}
