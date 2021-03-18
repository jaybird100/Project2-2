package Attributes;

public class Medication extends Attribute {
    String name;
    int amount;
    int dailyUsage;

    public Medication(String name, int amount, int dailyUsage) {
        super(false);
        this.name = name;
        this.amount = amount;
        this.dailyUsage = dailyUsage;
    }
    public Medication(){
        super(true);
    }
    @Override
    public String toString() {
        if(name==null){
            return "<MEDICATION>";
        }
        return name;
    }

    @Override
    public boolean equalsTo(Attribute input) {
        if (input instanceof Attributes.Medication) {
            Attributes.Medication c = (Attributes.Medication) (input);
            if (!name.equalsIgnoreCase(c.name))
                return false;
            if (amount != c.amount)
                return false;
            if (dailyUsage != c.dailyUsage)
                return false;
            return true;
        }
        return false;
    }
}
