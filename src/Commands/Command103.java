package Commands;

import Managers.*;

public class Command103 implements Command {
    ManagerFacade facade;
    public Command103() {
        facade = ManagerFacade.getInstance();
    }

    @Override
    public void execute() {
       // facade.printSortedWithLambda();

    }
}
