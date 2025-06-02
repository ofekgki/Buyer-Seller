package Commands;

import Managers.*;

public class Command100 implements Command {
    ManagerFacade facade;
    public Command100() {
        facade = ManagerFacade.getInstance();
    }

    @Override
    public void execute() {
     //   facade.printArrayByMap();

    }
}
