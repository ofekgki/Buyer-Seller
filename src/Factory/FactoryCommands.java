package Factory;

import Commands.*;

public class FactoryCommands
{

    public Command createCommand(int numOfCommand){
        switch(numOfCommand){
            case 99:
                return new Command99();
            case 100:
                return new Command100();
            case 101:
                return new Command101();
            case 102:
                return new Command102();
            case 103:
                return new Command103();
            default:
                System.out.println("Wrong input!");
                return null;
        }

    }


}
