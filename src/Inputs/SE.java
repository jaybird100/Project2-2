package Inputs;

import Skills.Skill;

public abstract class SE {
    Skill skill;
    public SE(Skill s){
        this.skill=s;
    }
    @Override
    public String toString() {
        return skill.toString();
    }
}
