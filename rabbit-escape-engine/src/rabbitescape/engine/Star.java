package rabbitescape.engine;

import rabbitescape.engine.util.LookupItem2D;
import rabbitescape.engine.util.Position;

public class Star implements LookupItem2D{

    public final int x;
    public final int y;
    
    public Star(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    @Override
    public Position getPosition()
    {
        return new Position( x, y );
    }

}
