package Inputs;

import Actions.Action;

public abstract class SE {
    Action skill;
    public SE(Action s){
        this.skill=s;
    }
    @Override
    public String toString() {
        return skill.toString();
    }
}
