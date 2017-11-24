package rabbitescape.engine.behaviours;

import static rabbitescape.engine.ChangeDescription.State.*;
import static rabbitescape.engine.Direction.*;
import static rabbitescape.engine.Token.Type.*;

import java.util.Map;

import rabbitescape.engine.*;
import rabbitescape.engine.ChangeDescription.State;

public class Jumping extends Behaviour{

    boolean hasAbility = false;
    public boolean abilityActive = false;
    private int jumpingCount = 1;
    private BehaviourTools t;
    @Override
    public boolean checkTriggered( Rabbit rabbit, World world )
    {
        BehaviourTools t = new BehaviourTools( rabbit, world );

        return !hasAbility && t.pickUpToken( jump, true );
    }

    int x = 0;
    int i = 0;
    @Override
    public State newState( BehaviourTools t, boolean triggered )
    {
        this.t = t;
        if ( triggered )
        {
            hasAbility = true;
        }

        if ( !hasAbility )
        {
            return null;
        }
        if(x == 0){
            
            if(t.blockAbove() != null){//ustunde blok varsa ziplayamayacak
                hasAbility = false;
            }
            else if(t.isOnUpSlope()){//egimli alandaysa
                t.rabbit.state = RABBIT_JUMPING_RIGHT_CONTINUE_1;
            }
            else{
                t.rabbit.state = RABBIT_JUMPING_RIGHT_START;
            }
            x++; 
        }
        
        
        
        switch ( t.rabbit.state )
        {
            case RABBIT_JUMPING_RIGHT_START:
            case RABBIT_JUMPING_LEFT_START:
                //ilk ziplamaya basladigi an
                //System.out.println("newState1");
                return newStateStart( t );
            case RABBIT_JUMPING_RIGHT_CONTINUE_1:
            case RABBIT_JUMPING_LEFT_CONTINUE_1:
                //ziplamaya devam ettigi anlar
                //System.out.println("newState2");
                return newStateCont1( t );
            case RABBIT_JUMPING_RIGHT_CONTINUE_2:
            case RABBIT_JUMPING_LEFT_CONTINUE_2:
                //ziplamaya devam ettigi anlar
                //System.out.println("newStat3");
                return newStateCont2( t );
            default:
                //tirmanmadigi butun durumlar
                //System.out.println("DEFAULT");
                //System.out.println("newState4");
                return newStateNotJumping( t );
        }
    }
    
    private State newStateStart( BehaviourTools t ){
        
        if ( jumpingCount == 1 )
        {
            //ziplamaya basladigi blok
            jumpingCount++;
            //System.out.println("jumpingCount1 : " + jumpingCount);
            return t.rl(RABBIT_JUMPING_RIGHT_CONTINUE_1, RABBIT_JUMPING_LEFT_CONTINUE_1);
        }
        else
        {
            //ziplama isleminin bitecegi nokta
            jumpingCount = 0;
            //System.out.println("jumpingCount2 : " + jumpingCount);
            return t.rl(RABBIT_JUMPING_RIGHT_END, RABBIT_JUMPING_LEFT_END);
        }
    }
   
    private State newStateCont1( BehaviourTools t )
    {
        jumpingCount++;
        return t.rl(RABBIT_JUMPING_RIGHT_CONTINUE_2, RABBIT_JUMPING_LEFT_CONTINUE_2);
    }

    private State newStateCont2( BehaviourTools t ){

        if ( jumpingCount == 1 )
        {
            jumpingCount++;
            return t.rl(RABBIT_JUMPING_RIGHT_CONTINUE_2, RABBIT_JUMPING_LEFT_CONTINUE_2);
        }
        else
        {
            jumpingCount = 0;
            return t.rl(RABBIT_JUMPING_RIGHT_END, RABBIT_JUMPING_LEFT_END);
        }
    }

    private State newStateNotJumping( BehaviourTools t )
    {
        //System.out.println("jumpingCountEnd : " + jumpingCount);
        if ( jumpingCount == 1 )
        {
            return t.rl(RABBIT_JUMPING_RIGHT_END, RABBIT_JUMPING_LEFT_END);
        }
        return null;
    }
    
    @Override
    public boolean behave( World world, Rabbit rabbit, State state )
    {     
        
        if(t.starAbove() != null && hasAbility){
            try
            {
                world.changes.removeStarAt( rabbit.x, rabbit.y - 1 );
            }
            catch ( Exception e )
            {
                System.out.println("Exception : " + e.getMessage());
            }
        }
        
        switch ( state )
        {
            case RABBIT_JUMPING_RIGHT_START:
            case RABBIT_JUMPING_LEFT_START:
            {
                System.out.println(1);
                abilityActive = true;
                return true;
            }
            case RABBIT_JUMPING_RIGHT_END:
            case RABBIT_JUMPING_LEFT_END:
            {
                System.out.println(2);
                abilityActive = false;
                return true;
            }
            case RABBIT_JUMPING_RIGHT_CONTINUE_1:
            case RABBIT_JUMPING_LEFT_CONTINUE_1:
            {
                System.out.println(3);
                --rabbit.y;//////////////////////////////////emin degilim
                abilityActive = false;
                return true;
            }
            case RABBIT_JUMPING_RIGHT_CONTINUE_2:
            case RABBIT_JUMPING_LEFT_CONTINUE_2:
            {
                System.out.println(4);
                abilityActive = false;
                return true;
            }
            case RABBIT_JUMPING_RIGHT_BANG_HEAD:
            case RABBIT_JUMPING_LEFT_BANG_HEAD:
            {
                System.out.println(5);
                //burayi bilmiyorum
                rabbit.dir = opposite( rabbit.dir );
                return true;
            }
            default:
            {
                //System.out.println(6);
                return false;
            }
        }
    }

    @Override
    public void cancel(){
        abilityActive = false;
    }
    
    @Override
    public void saveState( Map<String, String> saveState )
    {
        BehaviourState.addToStateIfTrue(
            saveState, "Jumping.hasAbility", hasAbility
        );

        BehaviourState.addToStateIfTrue(
            saveState, "Jumping.abilityActive", abilityActive
        );
    }

    @Override
    public void restoreFromState( Map<String, String> saveState )
    {
        hasAbility = BehaviourState.restoreFromState(
            saveState, "Jumping.hasAbility", hasAbility
        );

        abilityActive = BehaviourState.restoreFromState(
            saveState, "Jumping.abilityActive", abilityActive
        );
    }
}
