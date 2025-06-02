package Commands;

import Managers.*;

public class Command99 implements Command {
    ManagerFacade facade;
    public Command99() {
        facade = ManagerFacade.getInstance();
    }

    @Override
    public void execute() {
       // facade.printArrayByOrder();
    }
}
