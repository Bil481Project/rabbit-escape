package rabbitescape.engine.behaviours;

import static rabbitescape.engine.ChangeDescription.State.RABBIT_CLIMBING_LEFT_CONTINUE_2;
import static rabbitescape.engine.ChangeDescription.State.RABBIT_CLIMBING_RIGHT_CONTINUE_2;
import static rabbitescape.engine.ChangeDescription.State.RABBIT_ENTERING_EXIT;
import static rabbitescape.engine.ChangeDescription.State.RABBIT_ENTERING_EXIT_CLIMBING_LEFT;
import static rabbitescape.engine.ChangeDescription.State.RABBIT_ENTERING_EXIT_CLIMBING_RIGHT;
import static rabbitescape.engine.Token.Type.explode;

import rabbitescape.engine.Behaviour;
import rabbitescape.engine.BehaviourTools;
import rabbitescape.engine.Exit;
import rabbitescape.engine.Rabbit;
import rabbitescape.engine.Thing;

import static rabbitescape.engine.Token.Type.*;
import static rabbitescape.engine.ChangeDescription.State.*;

import rabbitescape.engine.*;
import rabbitescape.engine.ChangeDescription.State;

public class Teleport extends Behaviour{

	
		
		public void cancel()
	    {
	    }

	    @Override
	    public boolean checkTriggered( Rabbit rabbit, World world )
	    {
	        BehaviourTools t = new BehaviourTools( rabbit, world );
	        return t.pickUpToken( teleport, true );
	    }

	    @Override
	    public State newState( BehaviourTools t, boolean triggered )
	    {
	        if ( triggered )
	        {
	            if ( t.rabbit.state == RABBIT_CLIMBING_LEFT_CONTINUE_2 )
	            {
	                return RABBIT_ENTERING_EXIT_CLIMBING_LEFT;
	            }
	            if ( t.rabbit.state == RABBIT_CLIMBING_RIGHT_CONTINUE_2 )
	            {
	                return RABBIT_ENTERING_EXIT_CLIMBING_RIGHT;
	            }
	            return RABBIT_ENTERING_EXIT;
	        }
	        return null;
	    }

	    @Override
	    public boolean behave( World world, Rabbit rabbit, State state )
	    {
	        if (
	               state == RABBIT_ENTERING_EXIT
	            || state == RABBIT_ENTERING_EXIT_CLIMBING_RIGHT
	            || state == RABBIT_ENTERING_EXIT_CLIMBING_LEFT
	           )
	        {
	            world.changes.saveRabbit( rabbit );
	            return true;
	        }
	        else
	        {
	            return false;
	        }
	    }
		
		
		
		
		

	

}
